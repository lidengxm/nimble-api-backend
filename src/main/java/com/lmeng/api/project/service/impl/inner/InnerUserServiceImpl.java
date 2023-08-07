package com.lmeng.api.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lmeng.api.project.exception.BusinessException;
import com.lmeng.api.project.mapper.UserMapper;
import com.lmeng.apicommon.common.ErrorCode;
import com.lmeng.apicommon.model.entity.User;
import com.lmeng.apicommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/8/3
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 从数据库中查询是否要给用户分配ak和sk
     * @param accessKey
     * @return
     */
    @Override
    public User getInvokeUser(String accessKey) {
        //1.校验参数
        if(StringUtils.isBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.构造查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey",accessKey);
        return userMapper.selectOne(queryWrapper);
    }
}
