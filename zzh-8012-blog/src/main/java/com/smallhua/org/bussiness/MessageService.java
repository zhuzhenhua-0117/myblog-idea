package com.smallhua.org.bussiness;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.smallhua.org.common.api.CommonPage;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.common.util.ConditionUtil;
import com.smallhua.org.common.util.IdGenerator;
import com.smallhua.org.common.util.PageUtil;
import com.smallhua.org.mapper.*;
import com.smallhua.org.model.*;
import com.smallhua.org.util.SessionHelper;
import com.smallhua.org.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈消息功能〉
 *
 * @author ZZH
 * @create 2021/6/6
 * @since 1.0.0
 */
@Service
@Transactional
public class MessageService {

    @Autowired
    private TMessageMapper tMessageMapper;
    @Autowired
    private WebSocket webSocket;
    @Autowired
    private TArticleMapper tArticleMapper;
    @Autowired
    private TUserMirMapper tUserMirMapper;
    @Autowired
    private TCommentMapper tCommentMapper;
    @Autowired
    private TLeavemsgMapper tLeaveMapper;

    public List<TMessage> queryUnReadMessageByUserId() {
        TMessageExample example = new TMessageExample();
        example.setOrderByClause("CRE_TIME DESC");
        example.createCriteria()
                .andTargetIdEqualTo(SessionHelper.currentUserId())
                .andIfReadEqualTo((byte) 0);
        List<TMessage> tMessages = tMessageMapper.selectByExample(example);
        return tMessages;
    }

    public CommonPage<TMessage> queryMessagesByType(Byte type, BaseParam baseParam) {
        TMessageExample example = new TMessageExample();
        example.setOrderByClause("CRE_TIME DESC");
        TMessageExample.Criteria criteria = example.createCriteria();
        criteria.andTargetIdEqualTo(SessionHelper.currentUserId())
                .andTypeEqualTo(type);
        ConditionUtil.createCondition(baseParam, criteria, TMessage.class);
        CommonPage<TMessage> pagination = PageUtil.pagination(baseParam, () -> tMessageMapper.selectByExample(example));

        return pagination;
    }

    public CommonResult updateReadById(Long id) {
        TMessage message = new TMessage();
        message.setId(id);
        message.setIfRead((byte) 1);
        message.setUpdTime(DateUtil.date());
        message.setUpdId(SessionHelper.currentUserId());
        int i = tMessageMapper.updateByPrimaryKeySelective(message);

        if (i > 0) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }


    /**
     * 服务器主动推送评论消息
     *
     * @param comment
     */
    public void pushCommentMessage(TComment comment) {
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
            if (sb.length() > 0) sb.delete(0, sb.length() - 1);
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


    /**
     * 服务器主动推送留言消息
     *
     * @param leavemsg
     */
    public void pushLeaveMessage(TLeavemsg leavemsg) {
        TMessage message = new TMessage();
        StringBuffer sb = new StringBuffer();

        if (!Long.valueOf(1388434861433425920l).equals(leavemsg.getSourceId())) {
            message.setId(IdGenerator.generateIdBySnowFlake());
            message.setSourceId(leavemsg.getSourceId());
            //推给zzh
            message.setTargetId(1388434861433425920l);
            message.setIfRead((byte) 0);
            sb.append("<span>");
            sb.append(SessionHelper.currentUser().getUserName());
            sb.append("对你进行了留言");
            sb.append("</span>");
            message.setTitle(sb.toString());
            message.setContent(leavemsg.getContent());
            message.setType((byte) 0);
            message.setCreId(SessionHelper.currentUserId());
            message.setCreTime(leavemsg.getCreTime());
            message.setUpdId(leavemsg.getUpdId());
            message.setUpdTime(leavemsg.getUpdTime());

            tMessageMapper.insertSelective(message);
            webSocket.sendOneMessage("zzh", JSONUtil.toJsonStr(message));
        }

        if (!Long.valueOf(1388434861433425920l).equals(leavemsg.getTargetId())
                && leavemsg.getPid() != 0l) {
            message.setId(IdGenerator.generateIdBySnowFlake());
            message.setSourceId(leavemsg.getSourceId());
            message.setIfRead((byte) 0);
            if (sb.length() > 0) sb.delete(0, sb.length());
            sb.append("<span class='leaveMsg' @click='openLeaveMsg(" + leavemsg.getPid() + ")'>");
            sb.append(SessionHelper.currentUser().getUserName());
            sb.append("回复了你的留言");
            sb.append("</span>");
            message.setTitle(sb.toString());
            message.setTargetId(leavemsg.getTargetId());
            message.setContent(leavemsg.getContent());
            message.setType((byte) 0);
            message.setCreId(SessionHelper.currentUserId());
            message.setCreTime(leavemsg.getCreTime());
            message.setUpdId(leavemsg.getUpdId());
            message.setUpdTime(leavemsg.getUpdTime());
            tMessageMapper.insertSelective(message);

            TUserMir user = tUserMirMapper.selectByPrimaryKey(leavemsg.getTargetId());
            webSocket.sendOneMessage(user.getAccount(), JSONUtil.toJsonStr(message));
        }
    }
}