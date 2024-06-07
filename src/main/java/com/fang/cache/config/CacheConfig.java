package com.fang.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    //    @Bean
//    public RedisCaffeineCacheManager redisCaffeineCacheManager(RedisTemplate<String, Object> redisTemplate){
//        return new RedisCaffeineCacheManager(redisTemplate);
//    }
    @Bean("caffeineCacheManager")
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterWrite(30, TimeUnit.MINUTES)
                // 初始的缓存空间大小
//                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000));
        return cacheManager;
    }

//————————————————
//
//    版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
//
//    原文链接：https://blog.csdn.net/weixin_42666623/article/details/131310750
}
