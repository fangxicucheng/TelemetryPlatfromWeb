package com.fang.service.kafkaService;

import com.fang.telemetry.TelemetryFrame;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaRestTemplateService {
    @Autowired
    private KafkaTemplate<String, TelemetryFrame> kafkaTemplateAutoWried;
    private static KafkaTemplate<String, TelemetryFrame> kafkaTemplate;


    @PostConstruct
    public void init(){
        kafkaTemplate=this.kafkaTemplateAutoWried;
    }

    public static void sendKafkaMsg(String stationId,TelemetryFrame frame){

        kafkaTemplate.send(stationId,frame);
    }

}
