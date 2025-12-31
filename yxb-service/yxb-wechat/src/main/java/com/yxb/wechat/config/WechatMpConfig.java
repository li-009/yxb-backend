package com.yxb.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wechat.mp")
public class WechatMpConfig {
    private String appid;
    private String secret;
    private String token;
    private String aesKey;
}
