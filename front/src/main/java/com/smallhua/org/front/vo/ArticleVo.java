package com.smallhua.org.front.vo;

import com.smallhua.org.model.TArticle;
import com.smallhua.org.model.TArticleLabel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈文章数据封装〉
 *
 * @author ZZH
 * @create 2021/5/23
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class ArticleVo extends TArticle {

    private List<String> types;

    private List<String> labels;

    private List<TArticleLabel> articleLabels;

}