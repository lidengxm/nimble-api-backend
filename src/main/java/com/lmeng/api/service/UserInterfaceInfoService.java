package com.lmeng.api.service;

import com.lmeng.api.model.entity.InterfaceInfo;
import com.lmeng.api.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 26816
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-07-31 15:03:51
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 校验用户接口信息
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 用户调用接口
     * @param interfaceId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceId, int userId);

}
