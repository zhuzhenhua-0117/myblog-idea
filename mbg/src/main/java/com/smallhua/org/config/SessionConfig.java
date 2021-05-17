package com.smallhua.org.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 〈一句话功能简述〉<br>
 * 〈Session配置文件〉
 *  maxInactiveIntervalInSeconds设置 Session 失效时间，
 *  使用 Redis Session 之后，原 Spring Boot 中的 server.session.timeout 属性不再生效
 *
 * @author ZZH
 * @create 2021/5/3
 * @since 1.0.0
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400)
public class SessionConfig {

}