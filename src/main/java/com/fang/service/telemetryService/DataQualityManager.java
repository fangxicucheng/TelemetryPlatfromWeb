package com.fang.service.telemetryService;

import com.fang.config.satellite.paraParser.FrameInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataQualityManager {
    private int frameNumber;
    private int errorCount;
    private double errorCodeRate;

    public void setFrameInfo(FrameInfo frameInfo) {
        this.frameNumber++;
        if (!frameInfo.isValid()) {
            this.errorCount++;
            this.errorCodeRate = Math.round(this.errorCount / this.frameNumber);
        }
    }
    public void refresh(){
        this.frameNumber=0;
        this.errorCount=0;
        this.errorCodeRate=0.0;
    }

}
