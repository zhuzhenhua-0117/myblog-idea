package com.smallhua.org.mapper;

import com.smallhua.org.model.TType;
import com.smallhua.org.model.TTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TTypeMapper {
    long countByExample(TTypeExample example);

    int deleteByExample(TTypeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TType record);

    int insertSelective(TType record);

    List<TType> selectByExample(TTypeExample example);

    TType selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TType record, @Param("example") TTypeExample example);

    int updateByExample(@Param("record") TType record, @Param("example") TTypeExample example);

    int updateByPrimaryKeySelective(TType record);

    int updateByPrimaryKey(TType record);
}