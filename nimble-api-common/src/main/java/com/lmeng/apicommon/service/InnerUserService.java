package com.lmeng.apicommon.service;

import com.lmeng.apicommon.entity.User;

/**
 * 网关层使用的用户服务层
 */
public interface InnerUserService {

    /**
     * 校验用户接口信息
     * @param accessKey
     */
    User getInvokeUser(String accessKey);

}
