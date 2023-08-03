package com.lmeng.api.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lmeng.api.project.common.ErrorCode;
import com.lmeng.api.project.exception.BusinessException;
import com.lmeng.api.project.mapper.InterfaceInfoMapper;
import com.lmeng.apicommon.model.entity.InterfaceInfo;
import com.lmeng.apicommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/8/3
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 查询接口是否存在
     * @param path
     * @param method
     * @return
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        if(StringUtils.isAnyBlank(path,method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.构造查询条件
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("path",path);
        queryWrapper.eq("method",method);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
