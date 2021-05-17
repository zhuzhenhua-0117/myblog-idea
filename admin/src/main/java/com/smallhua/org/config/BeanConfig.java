package com.smallhua.org.config;

import com.smallhua.org.security.util.JwtTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 〈一句话功能简述〉<br>
 * 〈创建一些bean〉
 *
 * @author ZZH
 * @create 2021/5/16
 * @since 1.0.0
 */
@Configuration
public class BeanConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil(){
        return new JwtTokenUtil();
    }
}