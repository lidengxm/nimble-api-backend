package com.lmeng.api.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmeng.api.project.exception.BusinessException;
import com.lmeng.api.project.exception.ThrowUtils;
import com.lmeng.api.project.mapper.InterfaceInfoMapper;
import com.lmeng.api.project.service.InterfaceInfoService;
import com.lmeng.apicommon.common.ErrorCode;
import com.lmeng.apicommon.model.entity.InterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* 接口服务实现层
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {


    @Override
    public void validInterfaceInfo(InterfaceInfo InterfaceInfo, boolean add) {
        if (InterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1.获取添加接口时的参数
         String name = InterfaceInfo.getName();
         String description = InterfaceInfo.getDescription();
         String url = InterfaceInfo.getUrl();
         String method = InterfaceInfo.getMethod();

        // 创建时，所有参数都不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name,description,url,method),ErrorCode.PARAMS_ERROR);
        }

        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称不能大于50");
        }
    }
}




