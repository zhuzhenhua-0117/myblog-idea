package com.smallhua.org.front.service;

import cn.hutool.core.date.DateUtil;
import com.smallhua.org.common.api.CommonPage;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.common.util.ConditionUtil;
import com.smallhua.org.common.util.ConstUtil;
import com.smallhua.org.common.util.IdUtil;
import com.smallhua.org.common.util.PageUtil;
import com.smallhua.org.front.util.SessionHelper;
import com.smallhua.org.mapper.TCommentMapper;
import com.smallhua.org.model.TComment;
import com.smallhua.org.model.TCommentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 〈一句话功能简述〉<br>
 * 〈评论业务〉
 *
 * @author ZZH
 * @create 2021/5/25
 * @since 1.0.0
 */
@Service
@Transactional
public class CommentService {

    @Autowired
    private TCommentMapper commentMapper;


    public CommonPage<TComment> selAllComments(BaseParam baseParam) {
        TCommentExample example = new TCommentExample();
        TCommentExample.Criteria criteria = example.createCriteria();
        criteria.andIsDelEqualTo(ConstUtil.ZERO);

        ConditionUtil.createCondition(baseParam, criteria, TComment.class);

        return PageUtil.pagination(baseParam, () -> commentMapper.selectByExample(example));
    }

    public CommonResult saveComment(TComment comment) {
        comment.setId(IdUtil.generateIdBySnowFlake());
        comment.setCreId(SessionHelper.currentUserId());
        comment.setCreTime(DateUtil.date());
        comment.setUpdId(SessionHelper.currentUserId());
        comment.setUpdTime(DateUtil.date());

        int i = commentMapper.insertSelective(comment);

        if (i < 1){
            return CommonResult.failed("发布失败");
        }

        return CommonResult.success(i);
    }

    public CommonResult delComment(Long id) {

        TComment tComment = commentMapper.selectByPrimaryKey(id);

        tComment.setIsDel(ConstUtil.ONE);
        tComment.setUpdId(SessionHelper.currentUserId());
        tComment.setUpdTime(DateUtil.date());
        int i = commentMapper.updateByPrimaryKeySelective(tComment);

        if (i < 1){
            return CommonResult.failed("删除失败");
        }

        return CommonResult.success(i, "删除成功");
    }
}