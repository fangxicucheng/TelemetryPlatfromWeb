package com.fang.controller;

import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.service.saveService.ReceiveRecordRequestInfo;
import com.fang.service.saveService.ReceiveRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/records")
public class ReceiveRecordController {



    @Autowired
    private ReceiveRecordService receiveRecordService;
    @PostMapping("/getReceivePage")
    public Page<ReceiveRecord> findReceivePage(@RequestBody ReceiveRecordRequestInfo requestInfo){
        return this.receiveRecordService.getTelemetryReplayList(requestInfo);

    }


}
