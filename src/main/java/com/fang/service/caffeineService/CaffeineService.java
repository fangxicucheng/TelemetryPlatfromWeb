package com.fang.service.caffeineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CaffeineService {
    @Autowired
    private BufferService bufferService;
    @Cacheable(value="caffeineCacheManager",key="#recordId+'_'+#serialNum")
    public String testGet(int recordId, int serialNum) {
        System.out.println("获取");
        for (int i = 0; i < 200; i++) {

            if(i!=serialNum){
                bufferService.testPut(recordId,i);
            }
        }
        return recordId+"_"+serialNum;
    }

}
