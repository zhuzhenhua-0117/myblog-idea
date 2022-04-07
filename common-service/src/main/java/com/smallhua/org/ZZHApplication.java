package com.smallhua.org;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.smallhua.org")
@EnableDiscoveryClient
public class ZZHApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZZHApplication.class, args);
    }
}