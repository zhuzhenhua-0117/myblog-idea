package com.smallhua.org.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 〈生成唯一id〉
 */
@Component
@Slf4j
public class SnowFlow {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private long workerId;//为终端ID
    private long datacenterId = 1;//数据中心ID
    private Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);

    @PostConstruct
    public void init() {
        workerId = NetUtil.ipv4ToLong(getLocalIP());
        log.info("当前机器的workId:{}", workerId);
    }

    public synchronized String nextIdStr() {
        return snowflake.nextIdStr();
    }

    public synchronized String nextIdStr(long workerId, long datacenterId) {
        Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);
        return snowflake.nextIdStr();
    }


    /**
     * 获得本地ip
     * @return
     */
    private static String getLocalIP() {
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface nextElement = netInterfaces.nextElement();
                if (!nextElement.isUp() || nextElement.isLoopback()) {
                    continue;
                }
                // 遍历所有ip
                Enumeration<InetAddress> ips = nextElement.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress inetAddress = ips.nextElement();
                    if (null == inetAddress || "".equals(inetAddress)) {
                        continue;
                    }
                    String sIP = inetAddress.getHostAddress();
                    if (sIP == null || sIP.indexOf(":") > -1) {
                        continue;
                    }
                    if ("127.0.0.1".equals(sIP)) {
                        continue;
                    }else{
                        return sIP;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}