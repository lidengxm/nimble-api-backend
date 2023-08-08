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
     * @param request
     * @return
     */
    @PostMapping("/yan/api.php")
    public String poisonChicken(HttpServletRequest request) {
        String url = "https://api.btstu.cn/yan/api.php";
        String body = URLUtil.decode(request.getHeader("body"), CharsetUtil.CHARSET_UTF_8);
        HttpResponse httpResponse = HttpRequest.get(url + "?" + body)
                .execute();
        return httpResponse.body();
    }

    /**
     * 返回需要翻译的内容
     * @param request
     * @return
     */
    @PostMapping("/tst/api.php")
    public String getTranslate(HttpServletRequest request) {
        String url = "https://api.btstu.cn/tst/api.php";
        String body = URLUtil.decode(request.getHeader("body"), CharsetUtil.CHARSET_UTF_8);
        HttpResponse httpResponse = HttpRequest.get(url + "?" + body)
                .execute();
        return httpResponse.body();
    }

    /**
     * 获取随机壁纸
     * @param request
     * @return
     */
    @PostMapping("/sjbz/api.php")
    public String getWallpaper(HttpServletRequest request) {
        String url = "https://api.btstu.cn/sjbz/api.php";
        String body = URLUtil.decode(request.getHeader("body"), CharsetUtil.CHARSET_UTF_8);
        HttpResponse httpResponse = HttpRequest.get(url + "?" + body)
                .execute();
        return httpResponse.body();
    }
}
