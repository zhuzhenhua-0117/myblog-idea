package com.smallhua.org.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.common.util.ConditionUtil;
import com.smallhua.org.common.util.ConstUtil;
import com.smallhua.org.common.util.IdGenerator;
import com.smallhua.org.mapper.*;
import com.smallhua.org.model.*;
import com.smallhua.org.util.SessionHelper;
import com.smallhua.org.websocket.WebSocket;
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
    @Autowired
    private TMessageMapper tMessageMapper;
    @Autowired
    private WebSocket webSocket;
    @Autowired
    private TArticleMapper tArticleMapper;
    @Autowired
    private TUserMirMapper tUserMirMapper;


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
        long id = IdGenerator.generateIdBySnowFlake();

        if (comment.getPid() == 0l) {
            comment.setFullPath("/" + id);
        } else {
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

        if (i < 1) {
            return CommonResult.failed("发布失败");
        }

        //websocket 推送并保存消息
        pushMessage(comment);

        return CommonResult.success(i);
    }


    public CommonResult delComment(Long id) {

        TComment tComment = tCommentMapper.selectByPrimaryKey(id);

        tComment.setIsDel(ConstUtil.ONE);
        tComment.setUpdId(SessionHelper.currentUserId());
        tComment.setUpdTime(DateUtil.date());
        int i = tCommentMapper.updateByPrimaryKeySelective(tComment);

        if (i < 1) {
            return CommonResult.failed("删除失败");
        }

        return CommonResult.success(i, "删除成功");
    }

    private void pushMessage(TComment comment) {
        TMessage message = new TMessage();
        StringBuffer sb = new StringBuffer();
        if (comment.getPid() != 0l) {
            TComment tComment = tCommentMapper.selectByPrimaryKey(comment.getPid());
            message.setId(IdGenerator.generateIdBySnowFlake());
            message.setSourceId(comment.getSourceId());
            message.setTargetId(comment.getTargetId());
            message.setIfRead((byte) 0);
            sb.append("<span>");
            sb.append(SessionHelper.currentUser().getUserName());
            sb.append("回复了你的评论<a class=\"msg-comment\" @click=\"tiaozhuan(" + comment.getPid() + ",'0')\">" + tComment.getContent() + "</a>");
            sb.append("</span>");
            message.setTitle(sb.toString());
            message.setContent(comment.getContent());
            message.setType((byte) 0);
            message.setCreId(SessionHelper.currentUserId());
            message.setCreTime(comment.getCreTime());
            message.setUpdId(comment.getUpdId());
            message.setUpdTime(comment.getUpdTime());

            tMessageMapper.insertSelective(message);
        }

        if (!Long.valueOf(1388434861433425920l).equals(comment.getTargetId())) {
            message.setId(IdGenerator.generateIdBySnowFlake());
            message.setSourceId(comment.getSourceId());
            message.setIfRead((byte) 0);
            Long articleId = comment.getArticleId();
            TArticle tArticle = tArticleMapper.selectByPrimaryKey(articleId);
            if (sb.length()>0) sb.delete(0, sb.length() - 1);
            sb.append("<span>");
            sb.append(SessionHelper.currentUser().getUserName());
            sb.append("对你的文章<a class=\"msg-comment\" @click=\"tiaozhuan(" + articleId + ")\">" + tArticle.getTitle() + "</a>进行了评论");
            sb.append("</span>");
            message.setTitle(sb.toString());
            message.setTargetId(1388434861433425920l);
            message.setContent(comment.getContent());
            message.setType((byte) 0);
            message.setCreId(SessionHelper.currentUserId());
            message.setCreTime(comment.getCreTime());
            message.setUpdId(comment.getUpdId());
            message.setUpdTime(comment.getUpdTime());
            tMessageMapper.insertSelective(message);
        }


       /* TMessageExample example = new TMessageExample();
        example.setOrderByClause("CRE_TIME DESC");
        example.createCriteria()
                .andTargetIdEqualTo(SessionHelper.currentUserId())
                .andIfReadEqualTo((byte) 0);
        List<TMessage> tMessages = tMessageMapper.selectByExample(example);

        if (CollectionUtil.isNotEmpty(tMessages)) {
            TUserMir user = tUserMirMapper.selectByPrimaryKey(comment.getTargetId());
            webSocket.sendOneMessage(user.getAccount(), JSONUtil.toJsonStr(tMessages));
        }
        */
        TUserMir user = tUserMirMapper.selectByPrimaryKey(comment.getTargetId());
        webSocket.sendOneMessage(user.getAccount(), JSONUtil.toJsonStr(message));
    }
}