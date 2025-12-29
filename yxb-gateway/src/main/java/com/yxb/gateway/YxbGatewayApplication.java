package com.yxb.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class YxbGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(YxbGatewayApplication.class, args);
    }
}
