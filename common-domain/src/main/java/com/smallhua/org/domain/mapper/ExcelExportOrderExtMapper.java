package com.smallhua.org.domain.mapper;

import com.small.org.modal.dto.ExportOrderForExcel;
import com.smallhua.org.model.ExcelExportOrder;
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
