package com.lmeng.api.project.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/8/7
 */
@Configuration
//@ConfigurationProperties(prefix = "redis")
public class RedissonConfig {
    private String host;
    private int port;
    private String password;

    @Bean
    public RedissonClient createRedissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379");
//                .setPassword("123321");

        return Redisson.create(config);
    }
}
