package com.smallhua.org.service;

import cn.hutool.core.date.DateUtil;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.common.util.ConditionUtil;
import com.smallhua.org.common.util.ConstUtil;
import com.smallhua.org.common.util.IdUtil;
import com.smallhua.org.mapper.CommentMapper;
import com.smallhua.org.mapper.TCommentMapper;
import com.smallhua.org.model.TComment;
import com.smallhua.org.model.TCommentExample;
import com.smallhua.org.util.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private TCommentMapper tCommentMapper;
    @Autowired
    private CommentMapper commentMapper;


    public CommonResult<Map> selAllComments(BaseParam baseParam) {
        TCommentExample example = new TCommentExample();
        TCommentExample.Criteria criteria = example.createCriteria();
        criteria.andIsDelEqualTo(ConstUtil.ZERO);
        long l = tCommentMapper.countByExample(example);
        criteria.andPidEqualTo(0l);

        ConditionUtil.createCondition(baseParam, criteria, TComment.class);
//        CommonPage<CommentVo> pagination = PageUtil.pagination(baseParam, () -> commentMapper.selectByExample(example));
//        List<CommentVo> tree = TreeUtil.createTree();
//        pagination.setList(tree);

        Map data = new HashMap();
        data.put("list", commentMapper.selectByExample(example));
        data.put("total", l);

        return CommonResult.success(data);
    }

    public CommonResult saveComment(TComment comment) {
        long id = IdUtil.generateIdBySnowFlake();

        if (comment.getPid() == 0l){
            comment.setFullPath("/" + id);
        }else {
            StringBuffer sb = new StringBuffer();
            TCommentExample commentExample = new TCommentExample();
            commentExample.createCriteria()
                    .andIdEqualTo(comment.getPid())
            .andIsDelEqualTo(ConstUtil.ZERO);
            List<TComment> tComments = tCommentMapper.selectByExample(commentExample);
            sb.append(tComments.get(0).getFullPath()).append("/").append(id);

            comment.setFullPath(sb.toString());
        }
        comment.setId(id);
        comment.setCreId(SessionHelper.currentUserId());
        comment.setCreTime(DateUtil.date());
        comment.setUpdId(SessionHelper.currentUserId());
        comment.setUpdTime(DateUtil.date());
        comment.setSourceId(SessionHelper.currentUserId());

        int i = tCommentMapper.insertSelective(comment);

        if (i < 1){
            return CommonResult.failed("发布失败");
        }

        return CommonResult.success(i);
    }

    public CommonResult delComment(Long id) {

        TComment tComment = tCommentMapper.selectByPrimaryKey(id);

        tComment.setIsDel(ConstUtil.ONE);
        tComment.setUpdId(SessionHelper.currentUserId());
        tComment.setUpdTime(DateUtil.date());
        int i = tCommentMapper.updateByPrimaryKeySelective(tComment);

        if (i < 1){
            return CommonResult.failed("删除失败");
        }

        return CommonResult.success(i, "删除成功");
    }
}