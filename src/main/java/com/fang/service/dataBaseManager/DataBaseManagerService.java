package com.fang.service.dataBaseManager;

import com.fang.database.postgresql.entity.CountPara;
import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.service.commandCountService.CommandCountService;
import com.fang.service.countParaService.CountParaService;
import com.fang.service.restartTimeService.RestartTimeService;
import com.fang.service.saveService.ReceiveRecordService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataBaseManagerService {

    @Autowired
    private CommandCountService commandCountServiceAutoWired;

    @Autowired
    private ReceiveRecordService receiveRecordServiceAutoWired;
    @Autowired
    private RestartTimeService restartTimeServiceAutoWired;
    @Autowired
    private CountParaService countParaServiceAutoWired;
    private static CommandCountService commandCountService;
    private static ReceiveRecordService receiveRecordService;

    private static  RestartTimeService restartTimeService;
    private static CountParaService countParaService;
    @PostConstruct
    public void init() {
        commandCountService = this.commandCountServiceAutoWired;
        receiveRecordService = this.receiveRecordServiceAutoWired;
        restartTimeService=this.restartTimeServiceAutoWired;
        countParaService=this.countParaServiceAutoWired;
    }

    public static ReceiveRecordService getReceiveRecordService() {
        return receiveRecordService;
    }

    public static CommandCountService getCommandCountService() {
        return commandCountService;
    }
    public static RestartTimeService getRestartTimeService(){
        return restartTimeService;
    }

    public static void saveOrUpdateRestartTime(String satelliteName, Date restartTime){

        if(restartTime==null){
            return;
        }
        restartTimeService.saveOrUpdateRestartTime(satelliteName,restartTime);
    }
    public static Date getRestartTime(String satelliteName){
        return restartTimeService.getRestartTimeBySatelliteName(satelliteName);
    }

    public static void saveReceiveRecord(ReceiveRecord receiveRecord){
//        ReceiveRecord receiveRecor=new ReceiveRecord();
////        receiveRecord.setStartTime(startTime);
////        receiveRecord.setEndTime(endTime);
////        receiveRecord.setSatelliteName(satelliteName);
////        receiveRecord.setFilePath(filePath);d

        receiveRecordService.save(receiveRecord);

    }

        public static void updateSatelliteParaCountList(String satelliteName,List<CountPara>countParaList){

        if(countParaList==null||countParaList.size()==0){
            return;
        }

        countParaService.saveParaCount(satelliteName,countParaList);
    }

    public static Map<String,Double> getParaCountMap(String satelliteName){

        Map<String,Double> paraCountMap=new HashMap<>();


        List<CountPara> paraCountList = countParaService.getParaCountBySatelliteName(satelliteName);
        if(paraCountList!=null&&paraCountList.size()>0){
            for (CountPara countPara : paraCountList) {

                paraCountMap.put(countPara.getParaCode(),countPara.getParaValue());
            }
        }


        return paraCountMap;
    }


    public static CountParaService getCountParaService(){
        return countParaService;
    }

}
