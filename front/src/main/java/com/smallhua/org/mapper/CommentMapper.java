package com.smallhua.org.mapper;


import com.smallhua.org.model.TCommentExample;
import com.smallhua.org.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    //查询类别与标签级联信息
    List<CommentVo> selectByExample(TCommentExample example);
}
