package com.smallhua.org.export.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.smallhua.org.domain.dto.ExportOrderForExcel;
import com.smallhua.org.domain.strategy.CustomMergeStrategy;
import com.smallhua.org.domain.mapper.ExcelExportOrderExtMapper;
import com.smallhua.org.export.service.ExcelService;
import com.smallhua.org.export.util.EasyExcelUtils;
import com.smallhua.org.model.ExcelExportOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 〈一句话功能简述〉<br>
 * 〈导出excel业务〉
 *
 */
@Service
@Slf4j
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private ExcelExportOrderExtMapper excelExportOrderExtMapper;

    @Override
    public void exportExcelProduct() throws IOException {
        String filePath = "E:" + File.separator + "file" + DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String fileName = filePath + File.separator + "orderTest.xlsx";

        System.out.println(fileName);

        if(Files.notExists(Paths.get(filePath))) Files.createDirectories(Paths.get(filePath));
        if(Files.notExists(Paths.get(fileName)))  Files.createFile(Paths.get(fileName));
        ExcelWriter excelWriter = EasyExcel
                .write(fileName, ExportOrderForExcel.class)
                .registerWriteHandler(new CustomMergeStrategy(ExportOrderForExcel.class))
                .excelType(ExcelTypeEnum.XLSX).build();

        Long total = excelExportOrderExtMapper.queryTotalRecordsForExportProduct();

        EasyExcelUtils.pageWrite(excelWriter, "数据清单", 20000l,
                (currentPage, pageSize) -> excelExportOrderExtMapper.queryOrderProductForExport(currentPage, pageSize));
    }

    @Override
    public void exportExcel() throws IOException {
        String filePath = "E:" + File.separator + "file" + DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String fileName = filePath + File.separator + "orderTest.xlsx";

        System.out.println(fileName);

        if(Files.notExists(Paths.get(filePath))) Files.createDirectories(Paths.get(filePath));
        if(Files.notExists(Paths.get(fileName)))  Files.createFile(Paths.get(fileName));

        ExcelWriter excelWriter = EasyExcel
                .write(fileName, ExcelExportOrder.class)
                .excelType(ExcelTypeEnum.XLSX).build();

        Long total = excelExportOrderExtMapper.queryTotalRecordsForExport();

        EasyExcelUtils.pageWrite(excelWriter, "数据清单", total,
                (currentPage, pageSize) -> excelExportOrderExtMapper.queryOrderForExport(currentPage, pageSize));
    }

}