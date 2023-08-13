package com.lmeng.apicommon.constant;

/**
 * 通用常量类
 */
public interface CommonConstant {

    /**
     * 升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = " descend";

    /**
     * 用户初始分配接口调用次数
     */
    Integer GET_INVOKE_COUNT = 10;

    /**
     * 用户名前缀
     */
    String USER_PREINDEX = "API";

    /**
     * 防止请求重放key前缀
     */
    String NONCE_KEY_PREINDEX = "nonce:";

}
