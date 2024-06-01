package com.fang.cache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class CacheConfig {
    @Bean
    public RedisCaffeineCacheManager redisCaffeineCacheManager(RedisTemplate<String, Object> redisTemplate){
        return new RedisCaffeineCacheManager(redisTemplate);
    }
}
