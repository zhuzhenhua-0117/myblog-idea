package com.smallhua.org.export.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 〈导出数据〉
 *
 */
@Data
public class PageWriteData {

    @ExcelProperty("编号")
    private int number;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("性别")
    private int sex;

}