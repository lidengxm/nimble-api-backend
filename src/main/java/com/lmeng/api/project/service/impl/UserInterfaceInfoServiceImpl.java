package com.lmeng.api.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmeng.api.project.exception.BusinessException;
import com.lmeng.api.project.mapper.UserInterfaceInfoMapper;
import com.lmeng.api.project.model.dto.userInterfaceInfo.UserInterfaceInfoInvokeRequest;
import com.lmeng.api.project.service.UserInterfaceInfoService;
import com.lmeng.apicommon.common.ErrorCode;
import com.lmeng.apicommon.common.ResultUtils;
import com.lmeng.apicommon.constant.CommonConstant;
import com.lmeng.apicommon.constant.RedissonConstant;
import com.lmeng.apicommon.entity.UserInterfaceInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
* 用户调用接口信息
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 校验接口参数信息
     * @param userInterfaceInfo
     * @param add
     */
    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建时，接口和用户必须存在
        if (add) {
            if(userInterfaceInfo.getInterfaceInfoId() <= 0 || userInterfaceInfo.getUserId() < 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口或用户不存在");
            }
        }

        if(userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"剩余次数不能小于0");
        }
    }

    /**
     * 用户调用接口计数
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        //1.判断接口和用户是否存在
        if(interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.用户接口信息表 剩余接口次数-1，总调用次数+1，使用分布式锁改进
        //2.1获取Redisson可重入锁
        RLock lock = redissonClient.getLock(RedissonConstant.INVOKE_NAME + interfaceInfoId);
        UpdateWrapper<UserInterfaceInfo> wrapper = new UpdateWrapper<>();
        boolean result = false;
        try {
            //2.获取锁成功才查询接口次数，修改剩余调用次数
            if(lock.tryLock(RedissonConstant.INVOKE_NAME_TIME, TimeUnit.MINUTES)) {
                wrapper.eq("interfaceInfoId",interfaceInfoId);
                wrapper.eq("userId",userId);
                //剩余调用次数大于0
                wrapper.gt("leftNum",0);
                wrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
                result = this.update(wrapper);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return result;
    }

    /**
     * 用户获取接口调用次数
     * @param invokeRequest
     * @return
     */
    @Override
    public boolean updateUserInterfaceInfo(UserInterfaceInfoInvokeRequest invokeRequest) {
        //1.校验请求参数
        Long userId = invokeRequest.getUserId();
        Long interfaceInfoId = invokeRequest.getInterfaceInfoId();
        if(interfaceInfoId == null || userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数不完整!");
        }

        //2.查询当前用户与接口的关系表
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<UserInterfaceInfo>()
                .eq("userId", userId)
                .eq("interfaceInfoId", interfaceInfoId);
        UserInterfaceInfo oldUserInterfaceInfo = this.getOne(queryWrapper);

        //3.如果当前用户是新用户
        if(oldUserInterfaceInfo == null) {
            //说明是新用户
            UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setInterfaceInfoId(interfaceInfoId);
            userInterfaceInfo.setUserId(userId);
            //给用户每次分配10次调用次数
            userInterfaceInfo.setLeftNum(CommonConstant.GET_INVOKE_COUNT);
            return this.save(userInterfaceInfo);
        } else {
            //4.如果不是更新新用户就更新用户调用接口次数
            Integer leftNum = oldUserInterfaceInfo.getLeftNum();
            //校验用户接口的剩余调用次数决定是否要分配
            if(leftNum >= 100) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"客官，您的接口调用次数已经够多啦!");
            }
            //给用户每次分配10次调用次数，更新用户接口信息
            return this.update(new UpdateWrapper<UserInterfaceInfo>()
                    .eq("userId",userId)
                    .eq("interfaceInfoId",interfaceInfoId)
                    .setSql("leftNum = leftNum +" + CommonConstant.GET_INVOKE_COUNT));
        }
    }
}




