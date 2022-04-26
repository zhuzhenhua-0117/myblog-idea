package com.smallhua.org.domain.mapper;

import com.smallhua.org.domain.dto.ExcelExportOrder;
import com.smallhua.org.domain.dto.ExportOrderForExcel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExcelExportOrderExtMapper {

    Long queryTotalRecordsForExportProduct();

    List<ExportOrderForExcel> queryOrderProductForExport(@Param("currentPage") int currentPage, @Param("pageSize") int pageSize);

    Long queryTotalRecordsForExport();

    List<ExcelExportOrder> queryOrderForExport(@Param("currentPage") int currentPage, @Param("pageSize") int pageSize);

}
