package com.yxb.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * AI服务启动类
 */
@SpringBootApplication(scanBasePackages = "com.yxb")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.yxb.api")
@MapperScan("com.yxb.ai.mapper")
public class YxbAIApplication {

    public static void main(String[] args) {
        SpringApplication.run(YxbAIApplication.class, args);
    }
}
