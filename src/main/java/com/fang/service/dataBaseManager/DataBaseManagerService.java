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


    public static CountParaService getCountParaService(){
        return countParaService;
    }

}
