package com.smallhua.org.bussiness.shardingJDBC.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 〈一句话功能简述〉<br>
 * 〈分库〉
 *
 * @author ZZH
 * @create 2022/8/15
 * @since 1.0.0
 */
@Component
@Slf4j
public class MyTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        // 真实节点
        availableTargetNames.forEach(a -> { log.info("actual node db:{}", a);});

        log.info("logic table name: {}, route column: {}", shardingValue.getLogicTableName(), shardingValue.getColumnName());

        // 精确分片
        log.info("column  name:{}", shardingValue.getValue());

        for (String availableTargetName : availableTargetNames) {
            if (("t_order"+shardingValue.getValue()).equals(availableTargetName)) {
                return availableTargetName;
            }
        }

        return null;
    }
}