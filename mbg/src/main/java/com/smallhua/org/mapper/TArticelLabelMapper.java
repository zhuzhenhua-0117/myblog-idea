package com.smallhua.org.mapper;

import com.smallhua.org.model.TArticelLabel;
import com.smallhua.org.model.TArticelLabelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TArticelLabelMapper {
    long countByExample(TArticelLabelExample example);

    int deleteByExample(TArticelLabelExample example);

    int insert(TArticelLabel record);

    int insertSelective(TArticelLabel record);

    List<TArticelLabel> selectByExample(TArticelLabelExample example);

    int updateByExampleSelective(@Param("record") TArticelLabel record, @Param("example") TArticelLabelExample example);

    int updateByExample(@Param("record") TArticelLabel record, @Param("example") TArticelLabelExample example);
}