package com.smallhua.org.config;

import com.smallhua.org.common.config.BaseSwaggerConfig;
import com.smallhua.org.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 * Created by macro on 2019/4/8.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.smallhua.org.controller")
                .title("zzh前端博客系统")
                .description("SpringBoot版本中的一些示例")
                .contactName("smallhua")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }

}
