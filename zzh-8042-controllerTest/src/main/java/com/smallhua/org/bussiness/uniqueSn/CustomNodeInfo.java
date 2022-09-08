package com.smallhua.org.bussiness.uniqueSn;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author ZZH
 * @create 2022/9/8
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = "machine", name = "enabled", matchIfMissing = false)
@Configuration
@ConfigurationProperties(prefix = "machine")
@Data
public class CustomNodeInfo {

    private Long dataId;

    private Long serviceId;

}