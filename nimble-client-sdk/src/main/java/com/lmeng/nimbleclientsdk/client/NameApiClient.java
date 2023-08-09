package com.lmeng.nimbleclientsdk.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.lmeng.nimbleclientsdk.model.User;

/**
 * NameController-NameApiClient
 */
public class NameApiClient extends NimbleApiClient{

    public NameApiClient(String accessKey, String secretKey) {
        super(accessKey,secretKey);
    }

    /**
     * 返回你的名字
     * @param user
     * @return
     */
    public String getName(User user){
        String json = JSONUtil.toJsonStr(user);
        return HttpRequest.post(GATEWAY_HOST + "/api/interface/name/user")
                .addHeaders(getHeadMap(json,accessKey,secretKey))
                .body(json)
                .execute().body();
    }
}
