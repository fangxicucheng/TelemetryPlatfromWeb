package com.fang.service.kafkaService;

import com.fang.telemetry.TelemetryFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {
  @Autowired
    KafkaTemplate<String, TelemetryFrame> kafkaTemplate;
    private int indexTopic1 = 0;
    private int indexTopic2 = 0;
    private int indexTopic3 = 0;
    @Scheduled(fixedRate = 1000*60*60*24)
    public void productMessage() {
//        new Thread(()->{
//
//
//            while (true){
//                try {
//                    kafkaTemplate.send("telemetry",Thread.currentThread().getName()+"线程发送的"+indexTopic1+"条消息");
//                    indexTopic1++;
//                    Thread.sleep(25);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//        },"thread1").start();
//        new Thread(()->{
//
//
//            while (true){
//                try {
//                    kafkaTemplate.send("telemetry",Thread.currentThread().getName()+"线程发送的"+indexTopic2+"条消息");
//                    indexTopic2++;
//                    Thread.sleep(25);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//        },"thread2").start();
//        new Thread(()->{
//
//
//            while (true){
//                try {
//                    kafkaTemplate.send("telemetry",Thread.currentThread().getName()+"线程发送的"+indexTopic3+"条消息");
//                    indexTopic3++;
//                    Thread.sleep(25);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//        },"thread3").start();

    }
}
