package com.smallhua.org.mapper;

import com.smallhua.org.model.TDict;
import com.smallhua.org.model.TDictExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TDictMapper {
    long countByExample(TDictExample example);

    int deleteByExample(TDictExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TDict record);

    int insertSelective(TDict record);

    List<TDict> selectByExample(TDictExample example);

    TDict selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TDict record, @Param("example") TDictExample example);

    int updateByExample(@Param("record") TDict record, @Param("example") TDictExample example);

    int updateByPrimaryKeySelective(TDict record);

    int updateByPrimaryKey(TDict record);
}