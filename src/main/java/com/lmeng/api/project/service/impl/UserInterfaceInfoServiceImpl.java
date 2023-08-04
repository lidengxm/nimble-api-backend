package com.lmeng.api.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmeng.api.project.common.ErrorCode;
import com.lmeng.api.project.exception.BusinessException;
import com.lmeng.api.project.mapper.UserInterfaceInfoMapper;
import com.lmeng.api.project.service.UserInterfaceInfoService;
import com.lmeng.apicommon.model.entity.UserInterfaceInfo;
import org.springframework.stereotype.Service;

/**
* 用户调用接口信息
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    /**
     * 校验接口参数信息
     * @param userInterfaceInfo
     * @param add
     */
    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建时，接口和用户必须存在
        if (add) {
            if(userInterfaceInfo.getInterfaceInfoId() <= 0 || userInterfaceInfo.getUserId() < 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口或用户不存在");
            }
        }

        if(userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"剩余次数不能小于0");
        }
    }

    /**
     * 用户调用接口计数
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        //1.判断接口和用户是否存在
        if(interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //TODO 分布式锁(原子性的)
        //2.用户接口信息表 剩余接口次数-1，总调用次数+1
        UpdateWrapper<UserInterfaceInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("interfaceInfoId",interfaceInfoId);
        wrapper.eq("userId",userId);
        //剩余调用次数大于0
        wrapper.gt("leftNum",0);
        wrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return this.update(wrapper);
    }

}




