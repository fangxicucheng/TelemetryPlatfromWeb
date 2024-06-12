package com.fang.cache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig
{
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        // 配置 Redis 连接工厂（使用 Lettuce）
//        return new LettuceConnectionFactory();
//    }
   // @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 使用 String 序列化器序列化和反序列化 redis 的 key 值
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // 使用 Jackson 序列化器序列化和反序列化 redis 的 value 值
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

   // @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,

                                                   MessageListenerAdapter removedListenerAdapter, MessageListenerAdapter setListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //container.addMessageListener(expiredListenerAdapter, new PatternTopic("__keyevent@*__:expired"));
        container.addMessageListener(removedListenerAdapter, new PatternTopic("__keyevent@*__:del"));
        container.addMessageListener(setListenerAdapter, new PatternTopic("__keyevent@*__:set"));
        return container;
    }
   // @Bean
    public MessageListenerAdapter removedListenerAdapter(RedisKeyRemoveListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }
   // @Bean
    public MessageListenerAdapter setListenerAdapter(RedisUpdateAndAddListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }
}
