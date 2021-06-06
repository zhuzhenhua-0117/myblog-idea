package com.smallhua.org.controller;

import com.smallhua.org.model.TMessage;
import com.smallhua.org.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈消息管理〉
 *
 * @author ZZH
 * @create 2021/6/6
 * @since 1.0.0
 */
@RestController
@RequestMapping("messageController")
@Api("消息管理")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ApiOperation("获得当前用户所有消息")
    @GetMapping(value = "messages")
    public List<TMessage> queryUnReadMessage() {
        return messageService.queryUnReadMessageByUserId();
    }
}