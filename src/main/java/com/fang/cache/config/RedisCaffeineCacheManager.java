package com.fang.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
@Slf4j
public class RedisCaffeineCacheManager implements CacheManager {
    //@Autowired
 //   private CaffeineCacheManager caffeineCacheManager;

   // @Autowired
   // private RedisCacheManager redisCacheManager;
    private RedisTemplate<String, Object> redisTemplate;
    private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    private RedisCaffeineCache redisCaffeineCache;
    HashSet<String> cacheNames = new HashSet<>();

    public RedisCaffeineCacheManager(RedisTemplate<String, Object> redisTemplate) {
        super();
        this.redisTemplate = redisTemplate;
        this.redisCaffeineCache=new RedisCaffeineCache(false);
        this.redisCaffeineCache = new RedisCaffeineCache(false, this.redisTemplate, caffeineCache(), "caffeineRedis");
        cacheNames.add("caffeineRedis");
        log.info("cacheManager初始化");
    }

    @Override
    public Cache getCache(String name) {

        log.info("cacheManager获取");

        return this.redisCaffeineCache;
    }

    @Override
    public Collection<String> getCacheNames() {

        log.info("获取cacheNames");

        return cacheNames;
    }

    public void clearLocal(Object key) {
        this.redisCaffeineCache.clearLocal(key);
    }

    public void updateLocal(Object key) {
        this.redisCaffeineCache.updateLocal(key);
    }

    private com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache() {
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        cacheBuilder.expireAfterAccess(30, TimeUnit.MINUTES);
        cacheBuilder.expireAfterWrite(30, TimeUnit.MINUTES);
        cacheBuilder.recordStats();
        return cacheBuilder.build();
    }
}
