package com.lmeng.apiinterface;

import com.lmeng.nimbleclientsdk.NimbleApiClientConfig;
import com.lmeng.nimbleclientsdk.client.NimbleApiClient;
import com.lmeng.nimbleclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class NimbleApiInterfaceApplicationTests {

    @Resource
    private NimbleApiClient nimbleApiClient;

    @Test
    void contextLoads() {
        String res1 = nimbleApiClient.getNameByGet("yupiapi");
        String res2 = nimbleApiClient.getNameByPost("yupi");
        String res3 = nimbleApiClient.getUserNameByPost(new User("lihu"));
        System.out.println(res1);
        System.out.println(res2);
        System.out.println(res3);
    }

}
