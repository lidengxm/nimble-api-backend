package com.lmeng.nimbleclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lmeng.nimbleclientsdk.model.User;
import com.lmeng.nimbleclientsdk.util.SignUtil;
import org.springframework.web.bind.annotation.RequestBody;
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
    String accessKey;
    String secretKey;

    public NimbleApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private Map<String,String> getHeaderMap(String body) {
        Map<String,String> map = new HashMap<>();
        map.put("accessKey",accessKey);
        //一定不能再服务器中传输
//        map.put("secretKey",secretKey);
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("body",body);
        map.put("timestamp",String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", SignUtil.getSign(body,secretKey));
        return map;
    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get("http://localhost:8102/api/name/", paramMap);
        return result;
    }

    public String getNameByPost(@RequestParam String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.post("http://localhost:8102/api/name/", paramMap);
        return result;
    }

    public String getUserNameByPost(@RequestBody User user) {
        String jsonStr = JSONUtil.toJsonStr(user);
        HttpResponse response = HttpRequest.post("http://localhost:8102/api/name/user")
                .addHeaders(getHeaderMap(jsonStr))
                .body(jsonStr)
                .execute();
        System.out.println(response.getStatus());
        return response.body();
    }
}
