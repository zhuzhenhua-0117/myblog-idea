package com.smallhua.org;
import java.math.BigDecimal;
import java.util.Date;


import cn.hutool.core.date.DateUtil;
import com.smallhua.org.common.config.ThreadPoolConfig;
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

    private volatile int index = 1;

    @Test
    public void test() throws IOException {
        excelService.exportExcel();
    }

    @Test
    public void initExportTable() {
        String[] storeNos = new String[] {"京东", "美团","小A","老干妈","特斯拉"};
        String[] address = new String[] {"北京", "新加坡","缅甸","火星","阴曹地府"};
        String[] products = new String[] {"apple", "banana","caomei","waterGua","榴莲"};
        Random random = new Random();

        for (int i = 0; i < 1000000; i++) {
            threadPoolTaskExecutor.submit(() -> {
                long time = DateUtil.date().getTime();
                ExcelExportOrder order = new ExcelExportOrder();
                order.setOrderSn(snowFlow.nextIdStr());
                order.setUserName("zzh-"+index);
                order.setStoreName(storeNos[random.nextInt(storeNos.length)]);
                order.setOrderAmount(BigDecimal.valueOf(random.nextDouble()));
                order.setAddress(address[random.nextInt(address.length)]);
                order.setAddTime(time);
                excelExportOrderMapper.insertSelective(order);
                index++;

                String productNumber = snowFlow.nextIdStr();
                for (int j = 0; j < random.nextInt(5); j++) {
                    ExcelExportOrderProduct product = new ExcelExportOrderProduct();
                    product.setProductSn(productNumber + "-" + j);
                    product.setProductName(products[random.nextInt(products.length)]);
                    product.setProductPrice(new BigDecimal("0"));
                    product.setProductQuatity(random.nextInt(100));
                    product.setAddTime(time);

                    excelExportOrderProductMapper.insertSelective(product);
                }
            });
        }
    }
}