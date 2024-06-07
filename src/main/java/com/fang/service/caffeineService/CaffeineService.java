package com.fang.service.caffeineService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CaffeineService {
    @Cacheable(value="caffeineCacheManager",key="#recordId+'_'+#serialNum")
    public String testGet(int recordId, int serialNum) {
        System.out.println("获取");
        return recordId+"_"+serialNum;
    }
}
