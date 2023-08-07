package com.lmeng.apicommon.constant;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/8/7
 */
public interface RedissonConstant {
    /**
     * 分布式锁名字
     */
    String INVOKE_NAME = "invokeCount:";

    /**
     * 获取分布式锁等待时间
     */
    Integer INVOKE_NAME_TIME = 3;
}
