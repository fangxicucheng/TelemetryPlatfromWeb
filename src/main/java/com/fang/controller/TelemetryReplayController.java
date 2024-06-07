package com.fang.controller;

import com.fang.service.replayService.ReplayService;
import com.fang.telemetry.TelemetryFrameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/replay")
public class TelemetryReplayController {


    @Autowired
    private ReplayService replayService;

    @GetMapping("/getTelemetryFrame/{recordId}/{serialNum}")
    public TelemetryFrameModel getTelemetryFrame(@PathVariable int recordId,@PathVariable int serialNum) throws IOException {
        return replayService.getTelemetryFrameModel(serialNum,recordId);
    }
}
