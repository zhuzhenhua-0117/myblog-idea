package com.smallhua.org.front.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.smallhua.org.common.api.CommonPage;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.common.util.ConditionUtil;
import com.smallhua.org.common.util.IdUtil;
import com.smallhua.org.common.util.PageUtil;
import com.smallhua.org.front.dto.ArticleDefine;
import com.smallhua.org.front.dto.ArticleExampleDefine;
import com.smallhua.org.front.mapper.ArticleMapper;
import com.smallhua.org.front.util.SessionHelper;
import com.smallhua.org.front.vo.ArticleVo;
import com.smallhua.org.mapper.TArticleLabelMapper;
import com.smallhua.org.mapper.TArticleMapper;
import com.smallhua.org.model.TArticle;
import com.smallhua.org.model.TArticleExample;
import com.smallhua.org.model.TArticleLabel;
import com.smallhua.org.model.TArticleLabelExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Autowired
    private TArticleMapper tArticleMapper;
    @Autowired
    private TArticleLabelMapper tArticleLabelMapper;

    public CommonPage<ArticleVo> selAllArticles(BaseParam baseParam) {
        if (StrUtil.isEmpty(baseParam.getOrderBy())){
            baseParam.setOrderBy("IF_TOP DESC, CRE_TIME DESC");
        }
        TArticleExample example = new ArticleExampleDefine();
        TArticleExample.Criteria criteriaDefine = example.createCriteria();

        ConditionUtil.createCondition(baseParam, criteriaDefine, ArticleDefine.class);
        return PageUtil.pagination(baseParam, () -> articleMapper.selectByExample(example));
    }

    public CommonResult<ArticleVo> selArticleById(Long articleId) {
        TArticleExample example = new ArticleExampleDefine();
        TArticleExample.Criteria criteriaDefine = example.createCriteria();

        criteriaDefine.andIdEqualTo(articleId);
        return CommonResult.success(articleMapper.selectByExample(example).get(0));
    }

    public CommonResult saveOrUpdArticle(ArticleVo articleVo) {
        TArticle tArticle = null;
        List<TArticleLabel> articleLabels = articleVo.getArticleLabels();

        if (articleVo.getId() == null) {
            tArticle = new TArticle();
            articleVo.setId(IdUtil.generateIdBySnowFlake());
            BeanUtil.copyProperties(articleVo, tArticle);
            tArticle.setCreTime(DateUtil.date());
            tArticle.setCreId(SessionHelper.currentUserId());
            tArticle.setUpdTime(DateUtil.date());
            tArticle.setUpdId(SessionHelper.currentUserId());

            tArticleMapper.insertSelective(tArticle);
        }else {
            tArticle = tArticleMapper.selectByPrimaryKey(articleVo.getId());
            BeanUtil.copyProperties(articleVo, tArticle);
            tArticle.setUpdTime(DateUtil.date());
            tArticle.setUpdId(SessionHelper.currentUserId());

            tArticleMapper.updateByPrimaryKeySelective(tArticle);
        }

        //清空关联表 t_article_label
        if (articleVo.getId() != null) {
            TArticleLabelExample example = new TArticleLabelExample();
            example.createCriteria().andArticleIdEqualTo(articleVo.getId());

            tArticleLabelMapper.deleteByExample(example);
        }

        if (CollUtil.isNotEmpty(articleLabels)){
            articleLabels.stream().forEach(item -> {
                if (item.getId() == null) {
                    item.setId(IdUtil.generateIdBySnowFlake());
                }
                item.setArticleId(articleVo.getId());
                tArticleLabelMapper.insertSelective(item);
            });
        }

        return CommonResult.success(null);
    }

    public CommonResult delArticle(Long articleId) {
        tArticleMapper.deleteByPrimaryKey(articleId);

        //删除关联表
        TArticleLabelExample example = new TArticleLabelExample();
        example.createCriteria().andArticleIdEqualTo(articleId);

        tArticleLabelMapper.deleteByExample(example);
        return CommonResult.success(null);
    }
}