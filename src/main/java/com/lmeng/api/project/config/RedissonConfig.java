package com.lmeng.api.project.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/8/7
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient createRedissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://124.220.222.98:6379")
                .setPassword("123321");

        return Redisson.create(config);
    }
}
