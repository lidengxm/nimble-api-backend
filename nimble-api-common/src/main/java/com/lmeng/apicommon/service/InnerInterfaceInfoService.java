package com.lmeng.apicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lmeng.apicommon.model.entity.InterfaceInfo;

/**
* 接口服务层
*/
public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询模拟接口是否存在（请求路径、请求方法、请求参数、布尔）
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String url, String method);

//    /**
//     * 校验接口参数是否合法
//     * @param interfaceInfo
//     * @param add
//     */
//    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
