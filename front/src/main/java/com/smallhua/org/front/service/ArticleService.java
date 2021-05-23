package com.smallhua.org.front.service;

import com.smallhua.org.common.api.CommonPage;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.common.util.ConditionUtil;
import com.smallhua.org.common.util.PageUtil;
import com.smallhua.org.front.dto.ArticleDefine;
import com.smallhua.org.front.dto.ArticleExampleDefine;
import com.smallhua.org.front.mapper.ArticleMapper;
import com.smallhua.org.front.vo.ArticleVo;
import com.smallhua.org.model.TArticleExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 〈一句话功能简述〉<br>
 * 〈文章业务〉
 *
 * @author ZZH
 * @create 2021/5/23
 * @since 1.0.0
 */
@Service
@Transactional
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    public CommonPage<ArticleVo> selAllArticles(BaseParam baseParam) {
        TArticleExample example = new ArticleExampleDefine();
        TArticleExample.Criteria criteriaDefine = example.createCriteria();

        ConditionUtil.createCondition(baseParam, criteriaDefine, ArticleDefine.class);
        return PageUtil.pagination(baseParam, () -> articleMapper.selectByExample(example));
    }
}