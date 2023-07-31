package com.lmeng.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmeng.api.common.ErrorCode;
import com.lmeng.api.exception.BusinessException;
import com.lmeng.api.exception.ThrowUtils;
import com.lmeng.api.model.entity.InterfaceInfo;
import com.lmeng.api.model.entity.UserInterfaceInfo;
import com.lmeng.api.service.UserInterfaceInfoService;
import com.lmeng.api.mapper.UserInterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author 26816
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-07-31 15:03:51
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建时，所有参数都不能为空
        if (add) {
            if(userInterfaceInfo.getInterfaceInfoId() <= 0 || userInterfaceInfo.getUserId() < 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口或用户不存在");
            }
        }

        if(userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"剩余次数不能小于0");
        }
    }

    @Override
    public boolean invokeCount(long interfaceId, int userId) {
        //1.判断接口和用户是否存在
        if(interfaceId < 0 || userId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //TODO 分布式锁
        //2.用户接口信息表 剩余接口次数-1，总调用次数+1
        UpdateWrapper<UserInterfaceInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("interfaceId",interfaceId);
        wrapper.eq("userId",userId);
        //剩余调用次数大于0
        wrapper.gt("leftNum",0);
        wrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return this.update(wrapper);
    }


}




