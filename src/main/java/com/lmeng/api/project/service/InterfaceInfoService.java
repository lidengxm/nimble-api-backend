package com.lmeng.api.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lmeng.apicommon.model.entity.InterfaceInfo;

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
}
