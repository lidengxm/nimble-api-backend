package com.lmeng.apiinterface.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/8/8
 * @des  获取鸡汤
 */

@RestController
public class SentenceController {

    /**
     * 返回一句毒鸡汤
     * @return
     */
    @GetMapping("/yan/api.php")
    public String getPoisonChicken() {
        HttpResponse response = HttpRequest.get("https://api.btstu.cn/yan/api.php")
                .execute();
        return response.body();
    }

}
