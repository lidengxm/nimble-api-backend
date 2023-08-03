package com.lmeng.api.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.lmeng.api.project.annotation.AuthCheck;
import com.lmeng.api.project.constant.CommonConstant;
import com.lmeng.api.project.constant.UserConstant;
import com.lmeng.api.project.exception.BusinessException;
import com.lmeng.api.project.exception.ThrowUtils;
import com.lmeng.api.project.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.lmeng.api.project.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.lmeng.api.project.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.lmeng.api.project.common.BaseResponse;
import com.lmeng.api.project.common.DeleteRequest;
import com.lmeng.api.project.common.ErrorCode;
import com.lmeng.api.project.common.ResultUtils;
import com.lmeng.api.project.service.UserInterfaceInfoService;
import com.lmeng.api.project.service.UserService;
import com.lmeng.apicommon.model.entity.User;
import com.lmeng.apicommon.model.entity.UserInterfaceInfo;
import com.lmeng.apicommon.service.InnerUserInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 请求接口
 */
@RestController
@RequestMapping("/userInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService UserInterfaceInfoService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        if (userInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1.校验请求参数，将请求参数复制到请求实体类
        UserInterfaceInfo UserInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoAddRequest, UserInterfaceInfo);
        //2.校验接口参数是否合法
        UserInterfaceInfoService.validUserInterfaceInfo(UserInterfaceInfo, true);
        //3.获取登录用户
        User loginUser = userService.getLoginUser(request);
        UserInterfaceInfo.setUserId(loginUser.getId());
        //4.保存接口信息，如果失败抛出异常
        boolean result = UserInterfaceInfoService.save(UserInterfaceInfo);
        if(!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"保存接口信息失败");
        }
        long newUserInterfaceInfoId = UserInterfaceInfo.getId();
        return ResultUtils.success(newUserInterfaceInfoId);
    }

    /**
     * 删除接口
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1.获取当前用户
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        //2.判断接口是否存在
        UserInterfaceInfo oldUserInterfaceInfo = UserInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldUserInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        //3.仅本人或管理员可删除
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) || !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = UserInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param UserInterfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest UserInterfaceInfoUpdateRequest) {
        if (UserInterfaceInfoUpdateRequest == null || UserInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1.将旧接口信息拷贝到新的接口类中
        UserInterfaceInfo UserInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(UserInterfaceInfoUpdateRequest, UserInterfaceInfo);
        //2.对接口的参数校验
        UserInterfaceInfoService.validUserInterfaceInfo(UserInterfaceInfo, false);
        //3.判断原来的接口是否存在
        long id = UserInterfaceInfoUpdateRequest.getId();
        UserInterfaceInfo oldUserInterfaceInfo = UserInterfaceInfoService.getById(id);
        //4.如果不存在抛出异常
        ThrowUtils.throwIf(oldUserInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        //5.更新接口信息
        boolean result = UserInterfaceInfoService.updateById(UserInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取接口信息
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo UserInterfaceInfo = UserInterfaceInfoService.getById(id);
        return ResultUtils.success(UserInterfaceInfo);
    }

    /**
     * 分页获取接口列表
     * @param UserInterfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest UserInterfaceInfoQueryRequest, HttpServletRequest request) {
        if (UserInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1.将接口参数拷贝到接口查询类
        UserInterfaceInfo UserInterfaceInfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(UserInterfaceInfoQueryRequest, UserInterfaceInfoQuery);
        long current = UserInterfaceInfoQueryRequest.getCurrent();
        long size = UserInterfaceInfoQueryRequest.getPageSize();
        String sortField = UserInterfaceInfoQueryRequest.getSortField();
        String sortOrder = UserInterfaceInfoQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(UserInterfaceInfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> UserInterfaceInfoPage = UserInterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(UserInterfaceInfoPage);
    }




}
