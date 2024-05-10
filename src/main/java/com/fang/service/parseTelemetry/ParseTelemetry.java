package com.fang.service.parseTelemetry;

import com.fang.service.parseTelemetry.blockIngQueue.TelemetryBlockingQueue;
import com.fang.utils.ConfigUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

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

    private void initQueue(){



    }

    public void enQueue(byte[] bytes){

        queue.push(bytes);
    }

}
