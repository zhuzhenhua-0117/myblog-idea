package com.smallhua.org.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 〈一句话功能简述〉<br>
 * 〈发送消息〉
 *
 * @author ZZH
 * @create 2022/4/26
 * @since 1.0.0
 */
@Service
@Slf4j
public class SendMessageServiceImpl implements SendMessageService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    @Override
    public void sendMessageSync(String topic, Integer partition, String key, Object message) {
        kafkaTemplate.send(topic, partition, key, message);
    }

    @Override
    public void sendMessageSync(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }

    @Override
    public void sendMessageSync(String topic, String key, Object message) {
        kafkaTemplate.send(topic, key, message);
    }

    @Override
    public void sendMessageASync(String topic, Integer partition, String key, Object message) {
        kafkaTemplate.send(topic, partition, key, message);
    }

    @Override
    public void sendMessageASync(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }

    @Override
    public void sendMessageASync(String topic, String key, Object message) {
        kafkaTemplate.send(topic, key, message);
    }
}