package com.fang.service.telemetryService;

import com.fang.config.satellite.paraParser.FrameInfo;
import com.fang.telemetry.TelemetryFrameModel;
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

    public void serFrame(TelemetryFrameModel frame){
        frame.setErrorCodeNum(this.errorCount);
        frame.setSerialNum(this.serialNum);
        frame.setErrorRate(this.errorCodeRate);
       // frame.set
    }
    public void refresh(){
        this.serialNum=0;
        this.errorCount=0;
        this.errorCodeRate=0.0;
    }

}
