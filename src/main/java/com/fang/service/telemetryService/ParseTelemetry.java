package com.fang.service.telemetryService;


import com.fang.utils.ConfigUtils;
import lombok.Data;

import java.util.concurrent.LinkedBlockingDeque;
@Data

public class ParseTelemetry {
    private String satelliteId;
    private String satelliteName;
    private String stationName;
    private String stationId;

    private LinkedBlockingDeque<byte[]> queue;


    public ParseTelemetry(String satelliteName, String stationName, String stationId) {
        this.satelliteName = satelliteName;
        this.stationName = stationName;
        this.stationId = stationId;
       this.satelliteId= ConfigUtils.getSatelliteIdBySatelliteName(satelliteName);
       this.queue=new LinkedBlockingDeque<>();
    }



    public void enQueue(byte[] bytes){

        queue.push(bytes);
    }
    //处理业务
    public void parseService(){




    }

}
