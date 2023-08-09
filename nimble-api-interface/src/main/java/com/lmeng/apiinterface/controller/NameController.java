package com.lmeng.apiinterface.controller;

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
}
