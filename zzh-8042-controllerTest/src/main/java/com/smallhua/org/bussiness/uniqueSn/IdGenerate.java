package com.smallhua.org.bussiness.uniqueSn;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * 〈一句话功能简述〉<br>
 * 〈财务侧id生成器〉
 *
 * @author ZZH
 * @create 2022/8/30
 * @since 1.0.0
 */
@Data
@Component
@Scope("singleton")
public class IdGenerate {

    private static final long initTime = 946656000l;

    private static long sequence = 0l;

    private long lastTime;

    private DefaultNodeInfo nodeConfig;

    private long businessTypeBit = 4l;

    private long serviceIdBit = 3l;

    private long dataIdBit = 3l;

    private long timeBit = 41;

    private long sequenceBit = 13l;

    private final long sequenceMask = -1L ^ (-1L << sequenceBit);// 4095

    private final long timeShift = sequenceBit;
    private final long serviceIdShift = timeShift + timeBit;
    private final long dataIdShift = serviceIdShift + serviceIdBit;

    public IdGenerate(DefaultNodeInfo nodeConfig){
        this.nodeConfig = nodeConfig;
        this.lastTime = Instant.now().toEpochMilli() - initTime;
    }

    public synchronized String nextId(){
        long nowTimeInterval = SystemClock.now() - initTime;
        StringBuilder sb = new StringBuilder();
        Long dataId = nodeConfig.getDataId();
        Long serviceId = nodeConfig.getServiceId();

        long lastTimeInterval = lastTime;
        if(lastTimeInterval == nowTimeInterval){
            sequence = ++sequence & sequenceMask;
            if(sequence == 0l){
                nowTimeInterval = untilNextTime();
            }
        } else {
            sequence = 0l;
        }

        long id = (dataId << dataIdShift) | (serviceId << serviceIdShift) | (nowTimeInterval << timeShift) | sequence;
        sb.append(id);
        this.lastTime = nowTimeInterval;
        return sb.toString();
    }

    private long untilNextTime() {
        long timestamp = SystemClock.now() - initTime;
        // 循环直到操作系统时间戳变化
        while (timestamp == lastTime) {
            timestamp =  SystemClock.now();
        }
        if (timestamp < lastTime) {
            // 如果发现新的时间戳比上次记录的时间戳数值小，说明操作系统时间发生了倒退，报错
            throw new IllegalStateException(
                    StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms", lastTime - timestamp));
        }
        return timestamp;
    }

}