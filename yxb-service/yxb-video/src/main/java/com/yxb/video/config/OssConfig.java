package com.yxb.video.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "oss")
public class OssConfig {

    private String endpoint = "";
    private String accessKey = "";
    private String secretKey = "";
    private String bucket = "yxb-video";

    @Bean
    public OSS ossClient() {
        if (accessKey == null || accessKey.isEmpty()) {
            log.warn("OSS未配置access-key，OSS功能将不可用");
            return null;
        }
        log.info("初始化OSS客户端, endpoint={}, bucket={}", endpoint, bucket);
        return new OSSClientBuilder().build(endpoint, accessKey, secretKey);
    }
}
