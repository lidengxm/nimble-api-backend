package com.lemng.apigateway.config;

import com.alibaba.nacos.shaded.com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @learner lmeng
 * @date 2023/9/17
 * @des
 */
@Configuration
public class GuavaRateLimiterConfig {

    @SuppressWarnings("UnstableApiUsage")
    @Bean
    public RateLimiter rateLimiter() {
        //每秒限制最多请求20次
        return RateLimiter.create(20);
    }

}
