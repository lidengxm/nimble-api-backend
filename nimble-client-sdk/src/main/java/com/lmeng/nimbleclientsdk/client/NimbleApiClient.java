package com.lmeng.nimbleclientsdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lmeng.nimbleclientsdk.util.SignUtil;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/7/29
 * @Des 调用第三方接口的客户端
 */

public class NimbleApiClient {

    private static String GATEWAY_HOST = "http://localhost:8103";

    private String accessKey;

    private String secretKey;

    public void setGatewayHost(String gatewayHost) {
        GATEWAY_HOST = gatewayHost;
    }

    public NimbleApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private Map<String,String> getHeaderMap(String body,String method) {
        Map<String,String> map = new HashMap<>();
        map.put("accessKey",accessKey);
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("timestamp",String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", SignUtil.getSign(body,secretKey));
        body = URLUtil.encode(body, CharsetUtil.CHARSET_UTF_8);
        map.put("body",body);
        map.put("method",method);
        return map;
    }

    public String invokeInterface(String params, String url, String method) {
        HttpResponse response = HttpRequest.post(GATEWAY_HOST + url)
//        HttpResponse response = HttpRequest.post(url)
                .header("Accept-Charset", CharsetUtil.UTF_8)
                .addHeaders(getHeaderMap(params,method))
                .body(params)
                .execute();
        return JSONUtil.formatJsonStr(response.body());
    }

}
