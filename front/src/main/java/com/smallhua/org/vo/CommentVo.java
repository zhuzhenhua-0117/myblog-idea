package com.smallhua.org.vo;

import com.smallhua.org.model.TComment;
import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈评论vo〉
 *
 * @author ZZH
 * @create 2021/6/3
 * @since 1.0.0
 */
@Data
public class CommentVo extends TComment {

    private List<TComment> children;

    private String sourceName;

    private String targetName;

}