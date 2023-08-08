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

public class NameController {

    /**
     * 返回你的名字
     * @param user
     * @return
     */
    @PostMapping("/api/name/user")
    public String getUserNameByPost(@RequestBody User user) {
        return "你的名字是：" + user.getUserName();
    }
}
