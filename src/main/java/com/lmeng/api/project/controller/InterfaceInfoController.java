package com.lmeng.api.project.controller;

import com.alibaba.nacos.api.naming.pojo.healthcheck.impl.Http;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.lmeng.api.project.exception.BusinessException;
import com.lmeng.api.project.exception.ThrowUtils;
import com.lmeng.api.project.model.vo.InterfaceInfoVO;
import com.lmeng.api.project.service.UserInterfaceInfoService;
import com.lmeng.api.project.service.UserService;
import com.lmeng.api.project.service.InterfaceInfoService;
import com.lmeng.apicommon.common.*;
import com.lmeng.api.project.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.lmeng.api.project.model.dto.interfaceInfo.InterfaceInfoInvokeRequest;
import com.lmeng.api.project.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.lmeng.api.project.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.lmeng.api.project.model.enums.InterfaceStatusEnum;
import com.lmeng.apicommon.constant.CommonConstant;
import com.lmeng.apicommon.entity.InterfaceInfo;
import com.lmeng.apicommon.entity.User;
import com.lmeng.apicommon.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 请求接口
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1.校验请求参数，将请求参数复制到请求实体类
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        //2.校验接口参数是否合法
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        //3.获取登录用户
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        //4.保存接口信息，如果失败抛出异常
        boolean result = interfaceInfoService.save(interfaceInfo);
        if(!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"保存接口信息失败");
        }
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除接口
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1.获取当前用户
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        //2.判断接口是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        //3.仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) || !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1.将旧接口信息拷贝到新的接口类中
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        //2.对接口的参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        //3.判断原来的接口是否存在
        long id = interfaceInfoUpdateRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        //4.如果不存在抛出异常
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        //5.更新接口信息
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取接口信息
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfoVO> getInterfaceInfoById(long id, HttpServletRequest request) {
        //校验信息
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        if(userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //1.获取接口信息
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        //2.将接口信息复制到接口对象
        InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        BeanUtils.copyProperties(interfaceInfo,interfaceInfoVO);
        //3.查询到剩余接口调用次数信息并设置到接口视图对象
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId",id);
        queryWrapper.eq("userId",userId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(queryWrapper);
        if(userInterfaceInfo != null) {
            interfaceInfoVO.setAvailablePieces(userInterfaceInfo.getLeftNum().toString());
        }
        return ResultUtils.success(interfaceInfoVO);
    }

    /**
     * 分页获取接口列表
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1.将接口参数拷贝到接口查询类
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // description 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 发布接口（管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
    public BaseResponse<Boolean> onlineInterfaceInfoById(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        //1.判断接口是否存在
        if(idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        //2.判断接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if(interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口不存在!");
        }

        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();

        Object res = invokeInterfaceInfo(interfaceInfo.getSdk(), interfaceInfo.getName(), interfaceInfo.getRequestParams(), accessKey, secretKey);
        if (res == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (res.toString().contains("Error request")) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统接口内部异常");
        }

        InterfaceInfo updateInterfaceInfo = new InterfaceInfo();
        updateInterfaceInfo.setId(id);
        updateInterfaceInfo.setStatus(InterfaceStatusEnum.ONLINE.getValue());
        //仅本人或者管理员才可以修改
        boolean result = interfaceInfoService.updateById(updateInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 下线接口（管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/offline")
    public BaseResponse<Boolean> offlineInterfaceInfoById(@RequestBody IdRequest idRequest,HttpServletRequest request) {
        //1.判断接口是否存在
        if(idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        //2.判断接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if(interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口不存在!");
        }

        //3.修改数据库
        InterfaceInfo newInterfaceInfo = new InterfaceInfo();
        newInterfaceInfo.setId(id);
        newInterfaceInfo.setStatus(InterfaceStatusEnum.OFFLINE.getValue());
        //仅本人或者管理员才可以修改
        boolean res = interfaceInfoService.updateById(newInterfaceInfo);
        //5.返回
        return ResultUtils.success(res);
    }

    /**
     * 调用接口
     * @param interfaceInfoInvokeRequest
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfoById(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request) {
        //1.判断接口是否存在
        if(interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long interfaceInfoId = interfaceInfoInvokeRequest.getId();
        //2.判断接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        if(interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"接口不存在!");
        }
        //3.判断接口是否可以调用（上线状态）
        if(interfaceInfo.getStatus() == InterfaceStatusEnum.OFFLINE.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口已下线!");
        }

        //4.校验用户对该接口的调用次数是否>0
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId",interfaceInfoId);
        queryWrapper.eq("userId",userId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(queryWrapper);
        if(userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口不存在!");
        }
        Integer leftNum = userInterfaceInfo.getLeftNum();
        if(leftNum <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"无可用接口调用次数!");
        }

        //5.获取登录用户密钥
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        String requestParams= interfaceInfoInvokeRequest.getUserRequestParams();
        //6.调用对应客户端SDK
        Object res = invokeInterfaceInfo(interfaceInfo.getSdk(), interfaceInfo.getName(), requestParams, accessKey, secretKey);
        if (res == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (res.toString().contains("Error request")) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "调用错误，请检查参数和接口调用次数！");
        }
        return ResultUtils.success(res);
    }

    /**
     * 通过反射机制调用指定类的指定方法
     * @param classPath  类路径
     * @param methodName  方法名
     * @param userRequestParams  请求参数
     * @param accessKey  用户密钥
     * @param secretKey  密钥密码
     * @return
     */
    private Object invokeInterfaceInfo(String classPath, String methodName, String userRequestParams,
                                       String accessKey, String secretKey) {
        try {
            //根据提供的类路径加载对应的类。
            Class<?> clientClazz = Class.forName(classPath);
            // 1. 获取构造器，参数为ak,sk
            Constructor<?> binApiClientConstructor = clientClazz.getConstructor(String.class, String.class);
            // 2. 构造出对应的客户端，并传入密钥
            Object apiClient =  binApiClientConstructor.newInstance(accessKey, secretKey);

            // 3. 找到要调用的方法
            Method[] methods = clientClazz.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    // 3.1 获取参数类型列表
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 0) {
                        // 如果没有参数，直接调用
                        return method.invoke(apiClient);
                    }
                    //如果有参数，将JSON 字符串转换为相应的参数类型对象
                    Gson gson = new Gson();
                    Object parameter = gson.fromJson(userRequestParams, parameterTypes[0]);
                    //将参数对象传递给该方法，调用
                    return method.invoke(apiClient, parameter);
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("调用方法出错，错误：" + e.toString());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "调用方法出错！");
        }
    }


}
