package com.lmeng.api.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lmeng.api.project.annotation.AuthCheck;
import com.lmeng.api.project.common.BaseResponse;
import com.lmeng.api.project.common.ErrorCode;
import com.lmeng.api.project.common.ResultUtils;
import com.lmeng.api.project.constant.UserConstant;
import com.lmeng.api.project.exception.BusinessException;
import com.lmeng.api.project.mapper.UserInterfaceInfoMapper;
import com.lmeng.api.project.model.vo.InterfaceInfoVO;
import com.lmeng.api.project.service.InterfaceInfoService;
import com.lmeng.api.project.service.UserInterfaceInfoService;
import com.lmeng.apicommon.model.entity.InterfaceInfo;
import com.lmeng.apicommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * 接口统计分析控制类
 */

@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 统计接口调用次数前3
     * @return
     */
    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterface() {
        //1.查询出接口调用次数前三的接口id
        List<UserInterfaceInfo> userInterfaceInfos = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        //2.将用户接口对象按照接口ID排序
        Map<Long, List<UserInterfaceInfo>> interfaceInfoObjMap = userInterfaceInfos.stream().
                collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        //3.根据接口idList查询接口信息
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        //以用户接口对象集合的键集合（接口ID）查询
        queryWrapper.in("id",interfaceInfoObjMap.keySet());
        //查询出接口ID集合
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        if(CollectionUtils.isEmpty(interfaceInfoList)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        //4.通过接口id集合查询出InterfaceInfoVO对象返回
        List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            //从用户接口集合中查询出接口对应的调用次数
            Integer totalNum = interfaceInfoObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            interfaceInfoVO.setName(interfaceInfo.getName());
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        //5.返回
        return ResultUtils.success(interfaceInfoVOList);
    }

}
