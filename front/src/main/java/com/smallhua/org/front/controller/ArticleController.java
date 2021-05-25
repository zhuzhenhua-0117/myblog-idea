package com.smallhua.org.front.controller;

import com.smallhua.org.common.api.CommonPage;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.front.service.ArticleService;
import com.smallhua.org.front.vo.ArticleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈文章管理〉
 *
 * @author ZZH
 * @create 2021/5/23
 * @since 1.0.0
 */
@RestController
@RequestMapping("/articleController")
@Api("文章管理")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation("查询所有文章")
    @PostMapping(value = "articles")
    public CommonPage<ArticleVo> selAllArticles(@RequestBody BaseParam baseParam){
        return articleService.selAllArticles(baseParam);
    }

    @ApiOperation("查询所有文章")
    @GetMapping(value = "article/{articleId}")
    public CommonResult<ArticleVo> selArticleById(@PathVariable("articleId") Long articleId){
        return articleService.selArticleById(articleId);
    }

    @ApiOperation("保存更新文章")
    @PostMapping(value = "saveOrUpdArticle")
    public CommonResult saveOrUpdArticle(@RequestBody ArticleVo articleVo){
        return articleService.saveOrUpdArticle(articleVo);
    }

    @ApiOperation("删除文章文章")
    @DeleteMapping(value = "article/{articleId}")
    public CommonResult delArticle(@PathVariable("articleId") Long articleId){
        return articleService.delArticle(articleId);
    }

}