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

    public static void saveReceiveRecord(Date startTime,Date endTime,String satelliteName,String filePath){
        ReceiveRecord receiveRecord=new ReceiveRecord();
        receiveRecord.setStartTime(startTime);
        receiveRecord.setEndTime(endTime);
        receiveRecord.setSatelliteName(satelliteName);
        receiveRecord.setFilePath(filePath);

        receiveRecordService.save(receiveRecord);

    }

    public static void updateParaCountMap(String satelliteName,Map<String,Double>paraCountMap){

        if(paraCountMap==null||paraCountMap.size()==0){
            return;
        }
        List<CountPara>countParaList=new ArrayList<>();
        for (String paraCode : paraCountMap.keySet()) {

            CountPara countPara=new CountPara();
            countPara.setParaValue(paraCountMap.get(paraCode));
            countPara.setParaName();
        }
        countParaService.saveParaCount();
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
