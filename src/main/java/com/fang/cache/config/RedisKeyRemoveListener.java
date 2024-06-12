package com.fang.cache.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

//@Component
public class RedisKeyRemoveListener implements MessageListener {
//    @Autowired
//    private RedisCaffeineCacheManager cacheManager;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String removedKey=message.toString();
        //cacheManager.clearLocal(removedKey);
        System.out.println("Key removed" + removedKey);
    }
}
