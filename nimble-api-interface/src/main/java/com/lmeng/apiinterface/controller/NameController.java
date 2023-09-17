package com.lmeng.apiinterface.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.lmeng.nimbleclientsdk.model.Text;
import com.lmeng.nimbleclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/7/29
 */

@RestController

public class NameController {

    /**
     * 返回你的名字
     * @param user
     * @return
     */
    @PostMapping("/name/user")
    public String getName(@RequestBody User user) {
        return "你的名字是：" + user.getName();
    }

    /**
     * 翻译文本
     * @param text
     * @return
     */
    @PostMapping("/text/translation")
    public String getTranslationText(@RequestBody Text text){
        String url = "https://api.btstu.cn/tst/api.php?text=" + text.getText();
        HttpResponse response = HttpRequest.post(url).execute();
        return response.body();
    }
}
