package com.fang.controller;

import com.fang.service.replayService.ReplayService;
import com.fang.telemetry.TelemetryFrameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/replay")
public class TelemetryReplayController {


    @Autowired
    private ReplayService replayService;
    @GetMapping("/getTelemetryFrame/{recordId}/{serialNum}")
    public String getTelemetryFrame(@PathVariable int recordId,@PathVariable int serialNum) throws IOException {
        long startTime = System.currentTimeMillis();
        // LocalDateTime startTime=LocalDateTime.now();
        String telemetryFrameModel = replayService.getTelemetryFrameModel(serialNum, recordId);
        //Duration between = Duration.between(startTime, LocalDateTime.now());
       //*//* System.out.println(between.toMillis());*//*
        System.out.println(System.currentTimeMillis() - startTime);
        return telemetryFrameModel;
    }

/*    @GetMapping("/getTelemetryFrame/{recordId}/{serialNum}")
    public TelemetryFrameModel getTelemetryFrame(@PathVariable int recordId,@PathVariable int serialNum) throws IOException {
        long startTime = System.currentTimeMillis();
       // LocalDateTime startTime=LocalDateTime.now();
        TelemetryFrameModel telemetryFrameModel = replayService.getTelemetryFrameModel(serialNum, recordId);
        //Duration between = Duration.between(startTime, LocalDateTime.now());
       *//* System.out.println(between.toMillis());*//*
        System.out.println(System.currentTimeMillis() - startTime);
        return telemetryFrameModel;
    }*/
}
