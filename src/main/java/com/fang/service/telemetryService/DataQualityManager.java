package com.fang.service.telemetryService;

import com.fang.config.satellite.paraParser.FrameInfo;
import com.fang.telemetry.TelemetryFrame;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataQualityManager {
    private int serialNum;
    private int errorCount;
    private double errorCodeRate;

    public void setFrameInfo(FrameInfo frameInfo) {
        this.serialNum++;
        if (!frameInfo.isValid()) {
            this.errorCount++;
            this.errorCodeRate = Math.round(this.errorCount / this.serialNum);
        }
    }

    public void serFrame(TelemetryFrame frame){
        frame.setErrorCodeNum(this.errorCount);
        frame.setSerialNum(this.serialNum);
       // frame.set
    }
    public void refresh(){
        this.serialNum=0;
        this.errorCount=0;
        this.errorCodeRate=0.0;
    }

}
