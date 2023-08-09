package com.lmeng.nimbleclientsdk.client;

import cn.hutool.http.HttpRequest;

/**
 * SentenceApiClient-SentenceController
 */
public class SentenceApiClient extends NimbleApiClient{

    public SentenceApiClient(String accessKey, String secretKey) {
        super(accessKey, secretKey);
    }

    /**
     * 获取一句毒鸡汤
     * @return
     */
    public String getPoisonChicken(){
        return HttpRequest.get(GATEWAY_HOST+"/api/interface/yan/api.php")
                .addHeaders(getHeadMap("",accessKey,secretKey))
                .execute().body();
    }

}
