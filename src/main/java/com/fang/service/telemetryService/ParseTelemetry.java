package com.fang.service.telemetryService;


import com.fang.config.satellite.paraParser.ParaParser;
import com.fang.telemetry.TelemetryFrame;
import com.fang.utils.ConfigUtils;
import lombok.Data;

import java.util.concurrent.LinkedBlockingDeque;

@Data

public class ParseTelemetry {
    private String satelliteId;
    private String satelliteName;
    private String stationName;
    private String stationId;
    private Thread parseThread;
    private LinkedBlockingDeque<byte[]> queue;
    private ParaParser paraParser;

    public ParseTelemetry(String satelliteName, String stationName, String stationId) {
        this.satelliteName = satelliteName;
        this.stationName = stationName;
        this.stationId = stationId;
        this.satelliteId = ConfigUtils.getSatelliteIdBySatelliteName(satelliteName);
        this.queue = new LinkedBlockingDeque<>();
        this.paraParser = ConfigUtils.getParaParser(satelliteName);
    }

    public void enQueue(byte[] bytes) {

        queue.push(bytes);
    }

    //处理业务
    public void parseService() {

        while (true) {
            byte[] receiveBytes = queue.poll();












        }


    }


}
