package com.lmeng.apicommon.constant;

/**
 * 用户常量
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String LOGIN_USER_STATE = "user:login";

    /**
     * 登录态过期时间
     */
    Long LOGIN_USER_TTL = 30L;

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";

}
