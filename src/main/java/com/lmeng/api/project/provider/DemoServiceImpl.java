package com.lmeng.api.project.provider;

import com.lmeng.api.project.service.DemoService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/8/3
 */
@DubboService
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}
