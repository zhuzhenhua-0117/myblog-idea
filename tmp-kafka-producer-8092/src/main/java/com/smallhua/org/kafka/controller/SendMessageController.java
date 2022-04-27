package com.smallhua.org.kafka.controller;

import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.kafka.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈生产消息〉
 *
 * @author ZZH
 * @create 2022/4/26
 * @since 1.0.0
 */
@RestController
@RequestMapping("producer")
public class SendMessageController {

    @Autowired
    private SendMessageService sendMessageService;

    private final String topic = "zzh_test";


    @PostMapping("public/api/send")
    public CommonResult sendMessage(@RequestBody String body){
        try {
            sendMessageService.sendMessageSync(topic, body);
        }catch (Exception e) {
            return CommonResult.failed("发送失败");
        }
        return CommonResult.success(null, "发送成功");
    }

}