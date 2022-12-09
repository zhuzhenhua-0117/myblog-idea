package com.smallhua.org.bussiness.seckill.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br>
 * 〈配置〉
 *
 * @author ZZH
 * @create 2022/12/9
 * @since 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "secKill")
@Data
public class SecKillConfig {

    @ApiModelProperty("库存缓存阈值")
    private long skuCacheThreshold = 50;

    @ApiModelProperty("文件路径")
    private String filePath;


}