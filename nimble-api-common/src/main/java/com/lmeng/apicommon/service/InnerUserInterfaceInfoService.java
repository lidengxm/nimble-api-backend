package com.lmeng.apicommon.service;


public interface InnerUserInterfaceInfoService  {

    /**
     * 用户调用接口计数
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);



//    /**
//     * 校验接口参数是否合法
//     * @param userInterfaceInfo
//     * @param add
//     */
//    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

}
