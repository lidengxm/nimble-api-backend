package com.lmeng.apicommon.service;


import com.lmeng.apicommon.model.entity.UserInterfaceInfo;

public interface InnerUserInterfaceInfoService  {

    /**
     * 用户调用接口计数
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 是否还有调用次数
     *
     * @param interfaceInfoId 接口id
     * @param userId      用户id
     * @return UserInterfaceInfo 用户接口信息
     */
    boolean hasLeftNum(Long interfaceInfoId, Long userId);


}
