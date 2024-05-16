package com.fang.service.telemetryService;

import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.service.commandCountService.CommandCountService;
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
    private static CommandCountService commandCountService;
    private static ReceiveRecordService receiveRecordService;

    private static  RestartTimeService restartTimeService;
    @PostConstruct
    public void init() {
        commandCountService = this.commandCountServiceAutoWired;
        receiveRecordService = this.receiveRecordServiceAutoWired;
        restartTimeService=this.restartTimeServiceAutoWired;
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


}
