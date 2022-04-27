package com.smallhua.org.kafka.service;

public interface SendMessageService {

    void sendMessageSync(String topic, Integer partition, String key, Object message);
    void sendMessageSync(String topic, Object message);
    void sendMessageSync(String topic, String key, Object message);

    void sendMessageASync(String topic, Integer partition, String key, Object message);
    void sendMessageASync(String topic, Object message);
    void sendMessageASync(String topic, String key, Object message);
}
