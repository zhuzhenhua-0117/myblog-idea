package com.smallhua.org.kafka.component;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author ZZH
 * @create 2022/4/26
 * @since 1.0.0
 */
@Component
public class MqListener {

    @KafkaListener(topics = {"zzh_test"},groupId = "zzh-gp1")
    public void onMessage(ConsumerRecord<?, ?> record, Acknowledgment ack,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        System.out.println("消费消息："+record.topic()+"----"+record.partition()+"----"+record.value());
        ack.acknowledge();
    }
}