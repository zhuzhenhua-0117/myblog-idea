package com.smallhua.org.service;

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
}