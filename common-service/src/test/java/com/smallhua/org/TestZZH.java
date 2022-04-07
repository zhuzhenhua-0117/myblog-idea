package com.smallhua.org;


import com.smallhua.org.export.service.ExcelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZZHApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestZZH {


    @Autowired
    private ExcelService excelService;

    @Test
    public void test() throws IOException {
        excelService.exportExcel();
    }
}