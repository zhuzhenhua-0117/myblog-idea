package com.smallhua.org.bussiness.seckill.service;


public interface SecKillService {

    boolean putSku(long productId, long quality);

    boolean executeSecKill(long productId);
}
