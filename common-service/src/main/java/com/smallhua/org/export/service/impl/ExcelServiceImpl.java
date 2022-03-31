package com.smallhua.org.export.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.smallhua.org.export.service.ExcelService;
import com.smallhua.org.export.util.EasyExcelUtils;

/**
 * 〈一句话功能简述〉<br>
 * 〈导出excel业务〉
 *
 */
public class ExcelServiceImpl implements ExcelService {

    @Override
    public void exportExcel() {
        ExcelWriter excelWriter = EasyExcel.write("E:/easyexcel/testPageWrite.xlsx", PageWriteData.class).excelType(ExcelTypeEnum.XLSX).build();
        // page write
        EasyExcelUtils.pageWrite(excelWriter, "数据清单", 2_500_000L,
                (currentPage, pageSize) -> {
                    Page<User> page = new Page<>(currentPage, pageSize);
                    return userService.selectByPage(page).getRecords();
                });
    }
}