package com.smallhua.org;

import com.smallhua.org.security.config.IgnoreUrlsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 〈一句话功能简述〉<br>
 * 〈博客项目admin微服务主启动类〉
 *
 * @author ZZH
 * @create 2021/4/24
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.smallhua.org")
@EnableConfigurationProperties({IgnoreUrlsConfig.class})
@EnableDiscoveryClient
@EnableZuulProxy
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }

}