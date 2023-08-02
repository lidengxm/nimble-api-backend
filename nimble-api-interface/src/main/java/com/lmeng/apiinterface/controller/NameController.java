package com.lmeng.apiinterface.controller;

import com.lmeng.nimbleclientsdk.model.User;
import com.lmeng.nimbleclientsdk.util.SignUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/7/29
 */

@RestController
@RequestMapping("/name")
public class NameController {


    @GetMapping("/get")
    public String getNameByGet(String name,HttpServletRequest request) {
        System.out.println(request.getHeader("yupi"));
        return "GET 你的名字是：" + name;
    }

    @PostMapping("/get")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是：" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");
        //根据实际情况应该是去数据库中查询是否分配该用户
        if(!accessKey.equals(accessKey)) {
            throw new RuntimeException("没有权限");
        }
        if(Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }
        //todo 时间戳和当前时间不能超过5分钟

        //实际情况中是从数据库中查询secretKey
        String serverSign = SignUtil.getSign(body, "0729");
        if(!sign.equals(serverSign)) {
            throw new RuntimeException("没有权限");
        }

        return "POST 用户的名字是：" + user.getUserName();
    }
}
