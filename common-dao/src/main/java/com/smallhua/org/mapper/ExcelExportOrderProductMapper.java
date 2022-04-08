package com.smallhua.org.mapper;

import com.smallhua.org.model.ExcelExportOrderProduct;
import com.smallhua.org.model.ExcelExportOrderProductExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ExcelExportOrderProductMapper {
    long countByExample(ExcelExportOrderProductExample example);

    int deleteByExample(ExcelExportOrderProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ExcelExportOrderProduct record);

    int insertSelective(ExcelExportOrderProduct record);

    List<ExcelExportOrderProduct> selectByExample(ExcelExportOrderProductExample example);

    ExcelExportOrderProduct selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ExcelExportOrderProduct record, @Param("example") ExcelExportOrderProductExample example);

    int updateByExample(@Param("record") ExcelExportOrderProduct record, @Param("example") ExcelExportOrderProductExample example);

    int updateByPrimaryKeySelective(ExcelExportOrderProduct record);

    int updateByPrimaryKey(ExcelExportOrderProduct record);
}