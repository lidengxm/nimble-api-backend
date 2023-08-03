package com.lmeng.api.project.service.impl.inner;

import com.lmeng.api.project.service.UserInterfaceInfoService;
import com.lmeng.apicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/8/3
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 调用接口后调用次数+1
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
        return false;
    }
}
