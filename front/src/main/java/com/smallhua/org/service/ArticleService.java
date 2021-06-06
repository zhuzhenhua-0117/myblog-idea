package com.smallhua.org.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.smallhua.org.common.api.CommonPage;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.common.util.ConditionUtil;
import com.smallhua.org.common.util.IdGenerator;
import com.smallhua.org.common.util.PageUtil;
import com.smallhua.org.dto.ArticleDefine;
import com.smallhua.org.dto.ArticleExampleDefine;
import com.smallhua.org.mapper.ArticleMapper;
import com.smallhua.org.mapper.TArticleLabelMapper;
import com.smallhua.org.mapper.TArticleMapper;
import com.smallhua.org.model.TArticle;
import com.smallhua.org.model.TArticleExample;
import com.smallhua.org.model.TArticleLabel;
import com.smallhua.org.model.TArticleLabelExample;
import com.smallhua.org.util.SessionHelper;
import com.smallhua.org.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

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
        CommonPage<ArticleVo> pagination = PageUtil.pagination(baseParam, () -> articleMapper.selectByExampleWithBLOBs(example));
        if (pagination.getTotalPage() != 0 && pagination.getTotalPage() < pagination.getPageNum()){
            baseParam.setPage(pagination.getTotalPage());
            pagination = PageUtil.pagination(baseParam, () -> articleMapper.selectByExampleWithBLOBs(example));
        }
        pagination.getList().stream().map(item -> {renderYMD(item); return item;}).collect(Collectors.toList());
        return pagination;
    }

    public CommonResult<ArticleVo> selArticleById(Long articleId) {
        TArticleExample example = new ArticleExampleDefine();
        TArticleExample.Criteria criteriaDefine = example.createCriteria();

        criteriaDefine.andIdEqualTo(articleId);
        return CommonResult.success(articleMapper.selectByExampleWithBLOBs(example).get(0));
    }

    public CommonResult saveOrUpdArticle(ArticleVo articleVo) {
        TArticle tArticle = null;
        List<TArticleLabel> articleLabels = articleVo.getArticleLabels();

        if (articleVo.getId() == null) {
            tArticle = new TArticle();
            articleVo.setId(IdGenerator.generateIdBySnowFlake());
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
                    item.setId(IdGenerator.generateIdBySnowFlake());
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

    private void renderYMD(ArticleVo articleVo){
        Calendar cal = Calendar.getInstance();
        cal.setTime(articleVo.getCreTime());
        articleVo.setYear(cal.get(Calendar.YEAR));
        articleVo.setMonth(cal.get(Calendar.MONTH)+ 1);
        articleVo.setDate(cal.get(Calendar.DATE));
    }
}