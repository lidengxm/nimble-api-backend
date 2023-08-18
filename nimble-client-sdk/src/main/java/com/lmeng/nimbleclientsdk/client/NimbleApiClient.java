package com.lmeng.nimbleclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import com.lmeng.nimbleclientsdk.util.SignUtils;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/7/29
 * @Des 调用第三方接口的客户端
 */

public class NimbleApiClient {

//    protected static final String GATEWAY_HOST = "http://localhost:8103";
    //上线配置
    protected static final String GATEWAY_HOST = "http://124.220.222.98:8103";

    protected String accessKey;

    protected String secretKey;

    public NimbleApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 负责将数字签名的相关参数填入请求头中
     * @param body
     * @param accessKey
     * @param secretKey
     * @return
     */
    protected static Map<String,String> getHeadMap(String body, String accessKey, String secretKey){
        Map<String,String> headMap = new HashMap<>();
        headMap.put("accessKey",accessKey);
        headMap.put("body",body);
        headMap.put("sign", SignUtils.generateSign(body,secretKey));
        headMap.put("nonce", RandomUtil.randomNumbers(4));
        //当下时间/1000，时间戳大概10位
        headMap.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        return headMap;
    }
}
