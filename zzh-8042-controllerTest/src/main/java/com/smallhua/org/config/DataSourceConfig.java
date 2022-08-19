package com.smallhua.org.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author ZZH
 * @create 2022/8/19
 * @since 1.0.0
 */
@Configuration
public class DataSourceConfig {

    // bmw数据源配置
    @Bean
    @ConfigurationProperties(prefix = "nfms.properties.bmw")
    public DataSource bmwDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcTemplate bmwJdbcTemplate(@Qualifier("bmwDataSource") DataSource bmwDataSource) {
        return new JdbcTemplate(bmwDataSource);
    }

}