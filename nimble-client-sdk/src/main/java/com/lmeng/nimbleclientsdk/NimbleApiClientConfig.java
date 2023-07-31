package com.lmeng.nimbleclientsdk;

import com.lmeng.nimbleclientsdk.client.NimbleApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/7/29
 */
@ConfigurationProperties("nimble-api.client")
@Configuration
@Data
@ComponentScan
public class NimbleApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public NimbleApiClient NimbleApiClient() {
        return new NimbleApiClient(accessKey,secretKey);
    }
}
