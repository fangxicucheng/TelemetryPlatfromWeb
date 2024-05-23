package com.fang.service.gpsService.gpsManager;

import com.fang.database.postgresql.entity.GpsParaConfig;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class GpsConfigInfo {

    private String timeParaCode;

    private String xLocationParaCode;

    private String yLocationParaCode;

    private String zLocationParaCode;

    private String xSpeedParaCode;

    private String ySpeedParaCode;

    private String zSpeedParaCode;

    private String frameName;

    public GpsConfigInfo(GpsParaConfig gpsParaConfig) {
        this.frameName = gpsParaConfig.getFrameName();
        this.timeParaCode = gpsParaConfig.getTimeParaCode();
        this.xLocationParaCode = gpsParaConfig.getXLocationParaCode();
        this.yLocationParaCode = gpsParaConfig.getYLocationParaCode();
        this.zLocationParaCode = gpsParaConfig.getZLocationParaCode();
        this.xSpeedParaCode = gpsParaConfig.getXSpeedParaCode();
        this.ySpeedParaCode = gpsParaConfig.getYSpeedParaCode();
        this.zSpeedParaCode = gpsParaConfig.getZSpeedParaCode();
    }


    public boolean checkGpsFrame(String frameName) {
        return frameName.equals(this.frameName);
    }

}
