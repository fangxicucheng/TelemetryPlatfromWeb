package com.fang;

import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.service.saveService.ReceiveRecordRequestInfo;
import com.fang.service.saveService.ReceiveRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
@SpringBootTest
public class TestClass {
    @Autowired

    private ReceiveRecordService receiveRecordService;

    @Test
    void testReceiveRecoredSearch() {
        ReceiveRecordRequestInfo requestInfo = new ReceiveRecordRequestInfo();
        requestInfo.setPageNum(1);
        requestInfo.setPageSize(50);
//        List<String> stationInfoList=new ArrayList<>();
//        //stationInfoList.add("北斗站");
//        stationInfoList.add("长春站02");
//        stationInfoList.add("遥感所");
//        requestInfo.setStationNameList(stationInfoList);
        Page<ReceiveRecord> telemetryReplayList = receiveRecordService.getTelemetryReplayList(requestInfo);
        System.out.println(telemetryReplayList);


    }
    @Test
    public void reSaveTest(){
        this.receiveRecordService.reSave();
    }
}
