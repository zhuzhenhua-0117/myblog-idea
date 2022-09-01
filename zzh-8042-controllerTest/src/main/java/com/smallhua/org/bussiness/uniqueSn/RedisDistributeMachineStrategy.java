package com.smallhua.org.bussiness.uniqueSn;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.Instant;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author ZZH
 * @create 2022/8/30
 * @since 1.0.0
 */
@Component
public class RedisDistributeMachineStrategy implements DistributeMachineStrategy<NfmNodeInfo> {

    private static final long max_machine_num = 1<<3;

    private static final String PREFIX ="nfms:id:";
    private static final String KEY_ID_NODE = PREFIX.concat("node");
    /*private static final String KEY_DATA_CENTER = PREFIX.concat("dataCenter");
    private static final String KEY_SERVER_ID = PREFIX.concat("serviceId");*/
    private static final String SERVER_NFM = "nfmsservice";

    private RedisTemplate redisTemplate;

    public RedisDistributeMachineStrategy(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;

    }

    @Override
    public void doProcess(NfmNodeInfo nfmNodeInfo) {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        String localIP = getLocalIP();
        String idNode = KEY_ID_NODE;
        nfmNodeInfo.setClientIp(localIP);
        long time = Instant.now().toEpochMilli();
        Set<NfmNodeInfo> nfmNodeInfos = zSetOperations.range(idNode, 0, -1);
        Map<String, NfmNodeInfo> map = nfmNodeInfos.stream().collect(Collectors.toMap(item -> item.getClientIp(), item -> item));
        if(map.containsKey(localIP)){
            NfmNodeInfo nfmNodeInfo1 = map.get(localIP);
            nfmNodeInfo.setDataId(nfmNodeInfo1.getDataId());
            nfmNodeInfo.setServiceId(nfmNodeInfo1.getServiceId());
            return;
        }
        Long size = zSetOperations.zCard(idNode);
        if (size >= max_machine_num){
            zSetOperations.removeRange(idNode,0, 0);
        }
        List<Long> dataIds = nfmNodeInfos.stream().map(NfmNodeInfo::getDataId).collect(Collectors.toList());
        List<Long> serviceIds = nfmNodeInfos.stream().map(NfmNodeInfo::getServiceId).collect(Collectors.toList());

        for (long i = 0; i < max_machine_num; i++) {
            if (!dataIds.contains(i)){
                nfmNodeInfo.setDataId(i);
                break;
            }
        }
        if (nfmNodeInfo.getDataId() == null) throw new RuntimeException("超过支持的最大机器数！");

        for (long i = 0; i < max_machine_num; i++) {
            if (!serviceIds.contains(i)){
                nfmNodeInfo.setServiceId(i);
                break;
            }
        }
        if (nfmNodeInfo.getServiceId() == null) throw new RuntimeException("超过支持的最大机器数！");

        zSetOperations.add(idNode, nfmNodeInfo, time);

    }


    public static String getLocalIP() {
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