package com.smallhua.org.front.dto;

import com.smallhua.org.model.TArticle;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈文章自定义类〉
 *
 * @author ZZH
 * @create 2021/5/23
 * @since 1.0.0
 */

@Data
public class ArticleDefine extends TArticle {

    private Long typeId;

    private Long labelId;

    private String typeName;

    private String labelName;

}
