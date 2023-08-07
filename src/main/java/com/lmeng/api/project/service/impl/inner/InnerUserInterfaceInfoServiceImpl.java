package com.lmeng.api.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lmeng.api.project.exception.BusinessException;
import com.lmeng.api.project.service.UserInterfaceInfoService;
import com.lmeng.apicommon.common.ErrorCode;
import com.lmeng.apicommon.common.ResultUtils;
import com.lmeng.apicommon.model.entity.UserInterfaceInfo;
import com.lmeng.apicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/8/3
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 调用接口后调用次数+1
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
        return false;
    }

    @Override
    public boolean hasLeftNum(Long interfaceInfoId, Long userId) {
        if(interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数错误");
        }
        //1.根据接口Id和用户Id查询接口信息
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId",interfaceInfoId);
        queryWrapper.eq("userId",userId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(queryWrapper);
        if(userInterfaceInfo == null ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口不存在!");
        }
        //2.查询该接口的剩余调用次数
        Integer leftNum = userInterfaceInfo.getLeftNum();
        if(leftNum <= 0) {
            return false;
        }
        return true;
    }
}
