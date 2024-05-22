package com.fang.service.kafkaService;

import com.alibaba.fastjson2.JSON;
import com.fang.telemetry.TelemetryFrame;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaRestTemplateService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplateAutoWried;
    private static KafkaTemplate<String, String> kafkaTemplate;


    @PostConstruct
    public void init(){
        kafkaTemplate=this.kafkaTemplateAutoWried;
    }

    public static void sendKafkaMsg(String stationId,TelemetryFrame frame){
        String frameStr = JSON.toJSONString(frame);

        kafkaTemplate.send(stationId,frameStr);
    }

}
