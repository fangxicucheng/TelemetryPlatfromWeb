package com.fang.service.replayService;

import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.service.saveService.ReceiveRecordService;
import com.fang.service.telemetryService.ParseReplayTelemetry;
import com.fang.telemetry.TelemetryFrameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReplayService
{
    @Autowired
    private ReceiveRecordService receiveRecordService;
    @Cacheable(value="caffeineCacheManager",key="#id+'_'+#serialNum",sync = true)
    public TelemetryFrameModel getTelemetryFrameModel( int serialNum,int id) throws IOException {

        ReceiveRecord receiveRecord = receiveRecordService.getReceiveRecordById(id);
        List<TelemetryFrameModel> telemetryFrameModelList = ParseReplayTelemetry.parseTelemetry(receiveRecord.getFilePath());

        for (int i = 0; i < telemetryFrameModelList.size(); i++) {

            if(i!=serialNum-1){
                putTelemetryFrameModel(i+1,id,telemetryFrameModelList.get(i));
            }
        }
        System.out.println("再次緩存");
        return telemetryFrameModelList.get(serialNum-1);
    }
    @CachePut(value="caffeineCacheManager",key="#id+'_'+#serialNum")
    public TelemetryFrameModel putTelemetryFrameModel(int serialNum,int id,TelemetryFrameModel frameModel){

        return frameModel;
    }




}
