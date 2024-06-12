package com.fang.service.caffeineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class BufferService {

    @CachePut(value="caffeineCacheManager",key="#recordId+'_'+#serialNum")
    public String testPut(int recordId, int serialNum) {
        System.out.println("put");
        return recordId+"_"+serialNum;
    }


}
