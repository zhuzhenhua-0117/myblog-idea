package com.smallhua.org.mapper;

import com.smallhua.org.model.TArticleType;
import com.smallhua.org.model.TArticleTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TArticleTypeMapper {
    long countByExample(TArticleTypeExample example);

    int deleteByExample(TArticleTypeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TArticleType record);

    int insertSelective(TArticleType record);

    List<TArticleType> selectByExample(TArticleTypeExample example);

    TArticleType selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TArticleType record, @Param("example") TArticleTypeExample example);

    int updateByExample(@Param("record") TArticleType record, @Param("example") TArticleTypeExample example);

    int updateByPrimaryKeySelective(TArticleType record);

    int updateByPrimaryKey(TArticleType record);
}