package com.smallhua.org.service;

import cn.hutool.core.date.DateUtil;
import com.smallhua.org.common.api.CommonPage;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.common.util.ConditionUtil;
import com.smallhua.org.common.util.PageUtil;
import com.smallhua.org.mapper.TMessageMapper;
import com.smallhua.org.model.TMessage;
import com.smallhua.org.model.TMessageExample;
import com.smallhua.org.util.SessionHelper;
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

       /* //更新未读变成已读
        pagination.getList().stream()
                .filter(item -> item.getIfRead() == (byte) 0)
                .forEach(item -> {
                    TMessage message = new TMessage();
                    BeanUtil.copyProperties(item, message);
                    message.setIfRead((byte) 1);
                    tMessageMapper.updateByPrimaryKeySelective(message);
                });*/
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
        }else {
            return CommonResult.failed();
        }
    }
}