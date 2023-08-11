package com.lmeng.apicommon.service;


import com.lmeng.apicommon.entity.InterfaceInfo;

/**
* 接口服务层
*/
public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询模拟接口是否存在（请求路径、请求方法、请求参数、布尔）
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path, String method);

}
