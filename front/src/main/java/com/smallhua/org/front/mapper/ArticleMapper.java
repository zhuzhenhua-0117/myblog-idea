package com.smallhua.org.front.mapper;


import com.smallhua.org.front.vo.ArticleVo;
import com.smallhua.org.model.TArticleExample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {
    List<ArticleVo> selectByExampleWithBLOBs(TArticleExample example);

    List<ArticleVo> selectByExample(TArticleExample example);
}
