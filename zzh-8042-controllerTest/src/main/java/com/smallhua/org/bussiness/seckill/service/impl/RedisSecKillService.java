package com.smallhua.org.bussiness.seckill.service.impl;

import com.smallhua.org.bussiness.RedisService;
import com.smallhua.org.bussiness.seckill.config.SecKillConfig;
import com.smallhua.org.bussiness.seckill.enums.ErrorEnum;
import com.smallhua.org.bussiness.seckill.exception.SecKillException;
import com.smallhua.org.bussiness.seckill.service.SecKillService;
import com.smallhua.org.bussiness.seckill.util.RedisKeyUtil;
import com.smallhua.org.mapper.TStockMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 〈一句话功能简述〉<br>
 * 〈redis缓存抢购〉
 *
 * @author ZZH
 * @create 2022/12/9
 * @since 1.0.0
 */
@Service
@Slf4j
public class RedisSecKillService implements SecKillService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private TStockMapper stockMapper;

    @Autowired
    private SecKillConfig secKillConfig;

    @Override
    public boolean putSku(long productId, long quality) {
        // 放入数据库

        return false;
    }

    @Override
    @Transactional
    public boolean executeSecKill(long productId) {
        // 判断缓存是否有该商品
        Long retainAtCache = (Long) redisService.get(RedisKeyUtil.getRedisKey(String.valueOf(productId)));

        if (retainAtCache == null || retainAtCache <= 0){
            // 拿出数据到redis内存
            Long skuQuality = stockMapper.querySkuByProductId(productId);
            if (skuQuality == null || skuQuality <= 0) throw new SecKillException(ErrorEnum.STOCK_NOT_ENOUGH);
        }

        // 记录日志 商品编码#数量#redis剩余数量#日期yyyyMMddHHmmss  1#1#99#20221209020000

        // 扣减缓存数量

        return false;
    }

    private OutputStream getFileOutputStream() throws IOException {
        String filePath = secKillConfig.getFilePath();
        log.info("文件路径：{}", filePath);
        return Files.newOutputStream(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }
}