package com.smallhua.org;
import java.math.BigDecimal;


import cn.hutool.core.date.DateUtil;
import cn.hutool.system.SystemUtil;
import com.smallhua.org.common.util.SnowFlow;
import com.smallhua.org.export.service.ExcelService;
import com.smallhua.org.mapper.ExcelExportOrderMapper;
import com.smallhua.org.mapper.ExcelExportOrderProductMapper;
import com.smallhua.org.model.ExcelExportOrder;
import com.smallhua.org.model.ExcelExportOrderProduct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZZHApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestZZH {


    @Autowired
    private ExcelService excelService;

    @Autowired
    private ExcelExportOrderMapper excelExportOrderMapper;

    @Autowired
    private ExcelExportOrderProductMapper excelExportOrderProductMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private SnowFlow snowFlow;

    @Test
    public void test() throws IOException {
        long start = System.currentTimeMillis();
        excelService.exportExcelProduct();
        long end = System.currentTimeMillis();
        System.out.println((end - start)/1000);
    }

    @Test
    public void testExportForOrder() throws IOException {
        excelService.exportExcel();
    }

    @Test
    public void initExportTable() throws InterruptedException {
        String[] storeNos = new String[] {"京东", "美团","小A","老干妈","特斯拉"};
        String[] address = new String[] {"北京", "新加坡","缅甸","火星","阴曹地府"};
        String[] products = new String[] {"苹果", "香蕉","草莓","西瓜","榴莲"};
        Random random = new Random();
        AtomicInteger index = new AtomicInteger(0);
        Semaphore semaphore = new Semaphore(6);

        for (int i = 0; i < 1000000; i++) {
            threadPoolTaskExecutor.submit(() -> {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long time = DateUtil.date().getTime() / 1000;
                ExcelExportOrder order = new ExcelExportOrder();
                order.setOrderSn(snowFlow.nextIdStr());
                order.setUserName("zzh-" + index.getAndIncrement());
                order.setStoreName(storeNos[random.nextInt(storeNos.length)]);
                order.setOrderAmount(BigDecimal.valueOf(random.nextDouble()));
                order.setAddress(address[random.nextInt(address.length)]);
                order.setAddTime(time);
                order.setStatus(random.nextInt(2));
                excelExportOrderMapper.insertSelective(order);

                String productNumber = snowFlow.nextIdStr();
                int i1 = random.nextInt(5);
                int jIndex = i1 == 0 ? 1 : i1;
                for (int j = 0; j < jIndex; j++) {
                    ExcelExportOrderProduct product = new ExcelExportOrderProduct();
                    product.setOrderId(order.getId());
                    product.setProductSn(productNumber + "-" + j);
                    product.setProductName(products[random.nextInt(products.length)]);
                    product.setProductPrice(new BigDecimal("0"));
                    product.setProductQuatity(random.nextInt(100));
                    product.setAddTime(time);

                    excelExportOrderProductMapper.insertSelective(product);
                }
                semaphore.release();
            });
        }
    }
}