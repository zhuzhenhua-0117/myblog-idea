package com.smallhua.org.mapper;

import com.smallhua.org.model.TLabel;
import com.smallhua.org.model.TLabelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TLabelMapper {
    long countByExample(TLabelExample example);

    int deleteByExample(TLabelExample example);

    int insert(TLabel record);

    int insertSelective(TLabel record);

    List<TLabel> selectByExample(TLabelExample example);

    int updateByExampleSelective(@Param("record") TLabel record, @Param("example") TLabelExample example);

    int updateByExample(@Param("record") TLabel record, @Param("example") TLabelExample example);
}