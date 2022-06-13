package com.smallhua.org.es.config;

import com.smallhua.org.common.config.BaseSwaggerConfig;
import com.smallhua.org.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 * Created by zzh on 2019/4/8.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
//                .apiBasePackage("com.smallhua.org.*.controller")
                .title("zzh实践")
                .description("elk等实践，瞎搞")
                .contactName("smallhua")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }

}
