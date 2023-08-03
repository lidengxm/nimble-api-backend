package com.lmeng.api.project;

import javax.annotation.Resource;

import com.lmeng.api.project.service.UserInterfaceInfoService;
import com.lmeng.apicommon.service.InnerUserInterfaceInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 测试主类
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private UserInterfaceInfoService interfaceInfoService;


    @Test
    void contextLoads() {
        boolean b = interfaceInfoService.invokeCount(1L, 1);
        Assertions.assertTrue(b);
    }

}
