package com.lmeng.api.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lmeng.apicommon.model.entity.InterfaceInfo;
import com.lmeng.nimbleclientsdk.client.NimbleApiClient;

import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/8/3
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验接口参数是否合法
     * @param InterfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo InterfaceInfo, boolean add);

    /**
     * 获取api客户端
     * @return
     */
    NimbleApiClient getNimbleApiClient(HttpServletRequest request);
}
