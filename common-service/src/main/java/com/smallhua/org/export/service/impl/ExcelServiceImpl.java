package com.smallhua.org.export.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.smallhua.org.export.pojo.PageWriteData;
import com.smallhua.org.export.service.ExcelService;
import com.smallhua.org.export.util.EasyExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈导出excel业务〉
 *
 */
@Service
@Slf4j
public class ExcelServiceImpl implements ExcelService {

    @Override
    public void exportExcel() throws IOException {
        File file = new File("E:/easyexcel", "testPageWrite.xlsx");
        if (!file.exists()) file.createNewFile();
        ExcelWriter excelWriter = EasyExcel.write(file, PageWriteData.class).excelType(ExcelTypeEnum.XLSX).build();
        Random r = new Random();
        // page write
        EasyExcelUtils.pageWrite(excelWriter, "数据清单", 2_500_000L,
                (currentPage, pageSize) -> {
                    List<PageWriteData> ret = new ArrayList<>();
                    for (int i = currentPage*pageSize; i < (currentPage+1)*pageSize; i++) {
                        PageWriteData tmp = new PageWriteData();
                        tmp.setNumber(i);
                        tmp.setName("zzh"+i);
                        tmp.setSex(r.nextInt(1));

                        ret.add(tmp);
                    }
                    return ret;
                });
    }
}