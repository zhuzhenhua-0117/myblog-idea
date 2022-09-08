package com.smallhua.org.bussiness.uniqueSn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈节点信息〉
 *
 * @author ZZH
 * @create 2022/8/30
 * @since 1.0.0
 */
@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class DefaultNodeInfo {

    private Long dataId;

    private Long serviceId;

    private String clientIp;

    @Autowired
    public DefaultNodeInfo(DistributeMachineStrategy<DefaultNodeInfo> redisDistributeMachineStrategy){
        redisDistributeMachineStrategy.doProcess(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultNodeInfo)) return false;
        DefaultNodeInfo that = (DefaultNodeInfo) o;
        return Objects.equals(dataId, that.dataId) &&
                Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(clientIp, that.clientIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, serviceId, clientIp);
    }
}