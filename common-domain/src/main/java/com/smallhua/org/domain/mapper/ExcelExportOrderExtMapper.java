package com.smallhua.org.domain.mapper;

import com.smallhua.org.domain.dto.ExportExcelOfOrder;
import com.smallhua.org.domain.dto.ExportExcelOfOrderProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExcelExportOrderExtMapper {

    Long queryTotalRecordsForExportProduct();

    List<ExportExcelOfOrderProduct> queryOrderProductForExport(@Param("currentPage") int currentPage, @Param("pageSize") int pageSize);

    Long queryTotalRecordsForExport();

    List<ExportExcelOfOrder> queryOrderForExport(@Param("currentPage") int currentPage, @Param("pageSize") int pageSize);

}
