package com.fang.service.replayService;

import com.alibaba.fastjson2.JSON;
import com.fang.telemetry.TelemetryFrameModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReplayBufferService {
/*    @CachePut(value="replay",key="#id+'_'+#serialNum",cacheManager = "caffeineCacheManager")
    public TelemetryFrameModel putTelemetryFrameModel(int serialNum, int id, TelemetryFrameModel frameModel){

        System.out.println("put存储");
        return frameModel;
    }*/
    @CachePut(value="replay",key="#id+'_'+#serialNum",cacheManager = "caffeineCacheManager")
    public String  putTelemetryFrameModel(int serialNum, int id, TelemetryFrameModel frameModel){
        log.info("put存储");
        return JSON.toJSONString(frameModel);
    }
}
