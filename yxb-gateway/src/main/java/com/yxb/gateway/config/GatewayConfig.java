package com.yxb.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "gateway.ignore")
public class GatewayConfig {
    
    /**
     * 白名单路径
     */
    private List<String> urls;
}
