package com.smallhua.org.mapper;

import com.smallhua.org.model.TArticleLabel;
import com.smallhua.org.model.TArticleLabelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TArticleLabelMapper {
    long countByExample(TArticleLabelExample example);

    int deleteByExample(TArticleLabelExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TArticleLabel record);

    int insertSelective(TArticleLabel record);

    List<TArticleLabel> selectByExample(TArticleLabelExample example);

    TArticleLabel selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TArticleLabel record, @Param("example") TArticleLabelExample example);

    int updateByExample(@Param("record") TArticleLabel record, @Param("example") TArticleLabelExample example);

    int updateByPrimaryKeySelective(TArticleLabel record);

    int updateByPrimaryKey(TArticleLabel record);
}