package com.lmeng.api.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lmeng.api.project.model.dto.userInterfaceInfo.UserInterfaceInfoInvokeRequest;
import com.lmeng.apicommon.entity.UserInterfaceInfo;

public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 校验用户接口信息
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 用户调用接口计数
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 更新用户接口信息
     * @param invokeRequest
     * @return
     */
    boolean updateUserInterfaceInfo(UserInterfaceInfoInvokeRequest invokeRequest);
}
