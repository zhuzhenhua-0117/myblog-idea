package com.smallhua.org;

import com.smallhua.org.security.config.IgnoreUrlsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 〈一句话功能简述〉<br>
 * 〈博客项目主启动类〉
 *
 * @author ZZH
 * @create 2021/4/24
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.smallhua.org")
@EnableConfigurationProperties({IgnoreUrlsConfig.class})
public class FrontApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrontApplication.class, args);
    }

}