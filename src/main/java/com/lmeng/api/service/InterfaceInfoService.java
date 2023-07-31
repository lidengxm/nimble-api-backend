package com.lmeng.api.service;

import com.lmeng.api.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* 接口服务层
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
