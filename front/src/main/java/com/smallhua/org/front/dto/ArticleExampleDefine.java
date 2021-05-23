package com.smallhua.org.front.dto;

import com.smallhua.org.model.TArticleExample;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈多表联查时 增加从表属性条件〉
 *
 * @author ZZH
 * @create 2021/5/23
 * @since 1.0.0
 */
public class ArticleExampleDefine extends TArticleExample {

    @Override
    protected Criteria createCriteriaInternal() {
        return new ArticleCriteriaDefine();
    }


    public static class ArticleCriteriaDefine extends TArticleExample.Criteria {
        public TArticleExample.Criteria andTypeIdEqualTo(Long value) {
            addCriterion("type.ID =", value, "typeId");
            return (TArticleExample.Criteria) this;
        }

        public TArticleExample.Criteria andTypeIdIn(List<Long> values) {
            addCriterion("type.ID in", values, "typeId");
            return (TArticleExample.Criteria) this;
        }

        public TArticleExample.Criteria andLabelIdEqualTo(Long value) {
            addCriterion("label.ID =", value, "labelId");
            return (TArticleExample.Criteria) this;
        }

        public TArticleExample.Criteria andLabelIdIn(List<Long> values) {
            addCriterion("label.ID in", values, "labelId");
            return (TArticleExample.Criteria) this;
        }

    }
}