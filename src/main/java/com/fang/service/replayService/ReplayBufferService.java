package com.fang.service.replayService;

import com.alibaba.fastjson2.JSON;
import com.fang.telemetry.TelemetryFrameModel;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class ReplayBufferService {
/*    @CachePut(value="replay",key="#id+'_'+#serialNum",cacheManager = "caffeineCacheManager")
    public TelemetryFrameModel putTelemetryFrameModel(int serialNum, int id, TelemetryFrameModel frameModel){

        System.out.println("put存储");
        return frameModel;
    }*/
    @CachePut(value="replay",key="#id+'_'+#serialNum",cacheManager = "caffeineCacheManager")
    public String  putTelemetryFrameModel(int serialNum, int id, TelemetryFrameModel frameModel){
        System.out.println("put存储");
        return JSON.toJSONString(frameModel);
    }
}
