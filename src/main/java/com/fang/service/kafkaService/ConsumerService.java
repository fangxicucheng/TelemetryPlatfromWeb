package com.fang.service.kafkaService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class ConsumerService {
    @KafkaListener(topics="sendTopic1",groupId = "fxc")
    public void consumerTopic1(String msg){

        System.out.println( msg);
    }
    @KafkaListener(topics="sendTopic2",groupId = "fxc")
    public void consumerTopic2(String msg){

        System.out.println( msg);
    }
    @KafkaListener(topics="sendTopic3",groupId = "fxc")
    public void consumerTopic3(String msg){

        System.out.println( msg);
    }
}
