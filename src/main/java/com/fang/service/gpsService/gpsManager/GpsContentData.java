package com.fang.service.gpsService.gpsManager;

import lombok.Data;

@Data
public class GpsContentData {
    private double time;
    private double xLocation;
    private double yLocation;
    private double zLocation;
    private double xSpeed;
    private double ySpeed;
    private double zSpeed;

    public boolean canRecord() {
        boolean result = true;
        if (time == 0 ||
                xLocation == 0 ||
                yLocation == 0 ||
                zLocation == 0 ||
                xSpeed == 0 ||
                ySpeed == 0 ||
                zSpeed == 0
        ) {
            result = false;
        }
        return result;
    }
    public String getWriterStr(){
        StringBuilder sb=new StringBuilder();
        sb.append(Math.round(time));
        sb.append(",");
        sb.append(Math.round(xLocation));
        sb.append(",");
        sb.append(Math.round(yLocation));
        sb.append(",");
        sb.append(Math.round(zLocation));
        sb.append(",");
        sb.append(Math.round(xSpeed));
        sb.append(",");
        sb.append(Math.round(ySpeed));
        sb.append(",");
        sb.append(Math.round(zSpeed));
        return sb.toString();
    }
}
