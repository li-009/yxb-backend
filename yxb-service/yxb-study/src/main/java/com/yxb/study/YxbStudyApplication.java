package com.yxb.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.yxb")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.yxb.api")
@MapperScan("com.yxb.study.mapper")
public class YxbStudyApplication {
    public static void main(String[] args) {
        SpringApplication.run(YxbStudyApplication.class, args);
    }
}
