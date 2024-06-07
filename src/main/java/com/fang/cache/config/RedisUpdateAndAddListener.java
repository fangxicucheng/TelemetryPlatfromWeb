package com.fang.cache.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisUpdateAndAddListener implements MessageListener {
//    @Autowired
//    private RedisCaffeineCacheManager redisCaffeineCacheManager;
    @Override
    public void onMessage(Message message, byte[] pattern){
        //String topic = new String(pattern);
        String key = new String(message.toString());
       // this.redisCaffeineCacheManager.updateLocal(key);


        //System.out.println("收到key更新或修改，消息主题是："+ topic+",消息内容是："+msg);
    }
}
