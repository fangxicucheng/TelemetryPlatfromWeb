package com.fang.cache.config;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class RedisCaffeineCache extends AbstractValueAdaptingCache {
    private String name;
    private RedisTemplate<String, Object> redisTemplate;
    private Cache<Object, Object> caffeineCache;
    private String topic = "cache";
    private Map<String, ReentrantLock> keyLockMap = new ConcurrentHashMap<>();

    protected RedisCaffeineCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    public RedisCaffeineCache(boolean allowNullValues,RedisTemplate<String, Object> redisTemplate,Cache<Object, Object> caffeineCache,String name){
        super(allowNullValues);
        this.redisTemplate=redisTemplate;
        this.caffeineCache=caffeineCache;
        this.name=name;
    }

    public void init(RedisTemplate<String, Object> redisTemplate,Cache<Object, Object> caffeineCache,String name){
        this.redisTemplate=redisTemplate;
        this.caffeineCache=caffeineCache;
        this.name=name;
    }



    @Override
    protected Object lookup(Object key) {

        Object value = this.caffeineCache.getIfPresent(key);
        if (value != null) {
           // System.out.println("找到本地数据"+value);
            return value;
        }
        value = this.redisTemplate.opsForValue().get(key);
        if (value != null) {
        //    System.out.println("找到远程数据"+value);
            this.caffeineCache.put(key, value);
        }

        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = lookup(key);

        if (value != null) {

            return (T) value;
        }
        ReentrantLock lock = keyLockMap.get(key.toString());
        if (lock == null) {
            keyLockMap.putIfAbsent(key.toString(), new ReentrantLock());
            lock = keyLockMap.get(key.toString());
        }
        try {
            lock.lock();
            value = lookup(key);

            if(value==null){
                value = valueLoader.call();
                Object storeValue = toStoreValue(value);
                put(key,storeValue);
            }
        }
        catch (Exception e)
        {

        } finally {
            {
                lock.unlock();

                keyLockMap.remove(key);
            }
        }

        return value==null?null :(T) value;



    }

    @Override
    public void put(Object key, Object value) {
        if (!super.isAllowNullValues() && value == null) {
            this.evict(key);
        }
        this.redisTemplate.opsForValue().set(key.toString(), toStoreValue(value), 30, TimeUnit.MINUTES);

        this.caffeineCache.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Boolean setSuccess = false;
        setSuccess = this.redisTemplate.opsForValue().setIfAbsent(key.toString(), toStoreValue(value), Duration.ofMinutes(30));

        Object hasValue = null;
        if (setSuccess) {
            hasValue = value;
        } else {

            hasValue = redisTemplate.opsForValue().get(key);
        }
        return toValueWrapper(hasValue);
    }

    @Override
    public void evict(Object key) {
        this.redisTemplate.delete(key.toString());
        caffeineCache.invalidate(key);

    }

    @Override
    public void clear() {
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            this.redisTemplate.delete(key);
        }
        caffeineCache.invalidateAll();

    }
    public void updateLocal(Object key){
        Object value = this.redisTemplate.opsForValue().get(key.toString());
        this.caffeineCache.put(key,value);
    }

    public void clearLocal(Object key) {
        if (key == null) {
            this.caffeineCache.invalidateAll();
        } else {
            this.caffeineCache.invalidate(key);
        }
    }
}
