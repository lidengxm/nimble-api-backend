package com.lmeng.nimbleclientsdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lmeng.nimbleclientsdk.model.Text;
import com.lmeng.nimbleclientsdk.model.User;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

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
        String body = HttpRequest.post(GATEWAY_HOST + "/api/interface/name/user")
                .addHeaders(getHeadMap(json, accessKey, secretKey))
                .body(json)
                .execute().body();
        return body;
    }

    /**
     * 翻译文本
     * @param text
     * @return
     */
//    public String getTextTranslation(Text text) {
//        String url = GATEWAY_HOST + "/api/interface/text/translation?text=" + text.getText();
//        String body = HttpRequest.post(url)
//                .addHeaders(getHeadMap(JSONUtil.toJsonStr(text), accessKey, secretKey))
//                .execute().body();
//        return body;
//    }

}
