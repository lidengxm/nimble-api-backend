package com.lmeng.api.project.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
@PropertySource("classpath:des.properties")
@Data
public class AliyunOSSConfig {
    /**
     * 地域节点
     */
    private String endPoint;

    /**
     * 密钥id
     */
    private String accessKeyId;

    /**
     * 密钥密码
     */
    private String accessKeySecret;

    /**
     * OSS的Bucket名称
     */
    private String bucketName;

    /**
     * Bucket 域名
     */
    private String urlPrefix;

    /**
     * 目标文件夹
     */
    private String fileHost;

    /**
     * 将OSS 客户端交给Spring容器托管
     * @return OSS
     */
    @Bean
    public OSS ossClient() {
        // 创建默认访问的凭证提供器
        DefaultCredentialProvider credentialsProvider = CredentialsProviderFactory.newDefaultCredentialProvider(accessKeyId, accessKeySecret);
        // 创建OSSClient实例
        return new OSSClientBuilder().build(endPoint, credentialsProvider);
    }
}
