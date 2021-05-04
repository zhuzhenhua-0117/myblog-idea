package com.smallhua.org.front.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br>
 * 〈mapper文件扫描配置〉
 *
 * @author ZZH
 * @create 2021/4/26
 * @since 1.0.0
 */
@Configuration
@MapperScan({"com.smallhua.org.mapper","com.smallhua.org.dao"})
public class MapperConfig {

}