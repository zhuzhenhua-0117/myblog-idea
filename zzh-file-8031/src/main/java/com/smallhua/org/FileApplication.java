package com.smallhua.org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 〈一句话功能简述〉<br>
 * 〈博客项目admin微服务主启动类〉
 *
 * @author ZZH
 * @create 2021/4/24
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.smallhua.org")
@EnableDiscoveryClient
public class FileApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }

}