package com.fang.cache.config;

import com.fang.service.caffeineService.CaffeineService;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;

//@Service
public class RecordCaffeineService {
//    @Autowired
//    @Qualifier("caffeineCacheManager")
//    private CacheManager cacheManager;
//
//
//    @Scheduled(fixedRate = 1000)
//    public void printStatus(){
//
//        Collection<String> cacheNames = cacheManager.getCacheNames();
//
//
//        for (String cacheName : cacheNames) {
//
//            Cache cache = cacheManager.getCache(cacheName);
//            if(cache!=null&&cache.getNativeCache() instanceof com.github.benmanes.caffeine.cache.Cache<?,?>){
//                com.github.benmanes.caffeine.cache.Cache<?, ?> nativeCache = (com.github.benmanes.caffeine.cache.Cache<?, ?>) cache.getNativeCache();
//                CacheStats stats = nativeCache.stats();
//                System.out.println("Cache Stats for " + cacheName + ":");
//                System.out.println("Hit Count: " + stats.hitCount());
//                System.out.println("Miss Count: " + stats.missCount());
//                System.out.println("Load Success Count: " + stats.loadSuccessCount());
//                System.out.println("Load Failure Count: " + stats.loadFailureCount());
//                System.out.println("Total Load Time: " + stats.totalLoadTime());
//                System.out.println("Eviction Count: " + stats.evictionCount());
//                System.out.println("Eviction Weight: " + stats.evictionWeight());
//                System.out.println("Load Count: " +stats.loadCount());
//
//            }
//        }
//
//
//    }

}
