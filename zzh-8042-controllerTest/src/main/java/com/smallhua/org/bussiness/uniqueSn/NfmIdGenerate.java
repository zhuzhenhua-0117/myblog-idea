package com.smallhua.org.bussiness.uniqueSn;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

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
public class NfmIdGenerate {

    private static final long initTime = 946656000l;

    private long lastTime;

    private NfmNodeInfo nodeConfig;

    private String businessType = "";

    private long businessTypeBit = 4l;

    private long serviceIdBit = 3l;

    private long dataIdBit = 3l;

    private long timeBit = 41;

    private long sequenceBit = 13l;

    private final long sequenceMask = -1L ^ (-1L << sequenceBit);// 4095

    private final long timeShift = sequenceBit;
    private final long serviceIdShift = timeShift + timeBit;
    private final long dataIdShift = serviceIdShift + serviceIdBit;

    public NfmIdGenerate(NfmNodeInfo nodeConfig, String businessType){
        this.nodeConfig = nodeConfig;
        this.businessType = businessType;
        this.lastTime = Instant.now().toEpochMilli();
    }

    public synchronized String nextId(){
        long timestamp = SystemClock.now();
        StringBuilder sb = new StringBuilder();
        sb.append(businessType);
        Long dataId = nodeConfig.getDataId();
        Long serviceId = nodeConfig.getServiceId();

        long sequence = 0l;
        long lastTime = getLastTime();
        if(lastTime == timestamp - initTime){
            sequence = ++sequence & sequenceMask;
            if(sequence == 0l){
                timestamp = untilNextTime();
            }
        } else {
            sequence = 0l;
        }

        long id = (dataId << dataIdShift) | (serviceId | serviceIdBit) | (lastTime << timeShift) | sequence;
        sb.append(id);
        this.lastTime = timestamp;
        return sb.toString();
    }

    private long untilNextTime() {
        long timestamp = SystemClock.now();
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

    public long getLastTime(){
        return lastTime - initTime;
    }

}