package com.fang.service.telemetryService;

import com.fang.utils.UTCUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
@Data
@AllArgsConstructor
public class SatelliteTimeManager {
    private String satelliteName;
    private LocalDateTime satelliteTime;
    private LocalDateTime restartTime;
    private LocalDateTime satelliteTimeDelay;
    private LocalDateTime restartTimeDelay;
    private int restartTimeChangeTimes;
    private String satelliteTimeStr;
    private String satelliteTimeDelayStr;
    private String restartTimeStr;
    private String restartTimeDelayStr;

    public SatelliteTimeManager() {
        this.restartTimeChangeTimes=0;
    }

    public void setSatelliteTime(double paraValue){
       this.satelliteTime=UTCUtils.getUTCTime(paraValue);
       this.restartTimeStr=UTCUtils.convertLocalTimeToStr(this.satelliteTime);
    }

    public void setSatelliteTimeDelay(double paraValue){

        this.satelliteTimeDelay=UTCUtils.getUTCTime(paraValue);
        this.restartTimeDelayStr=UTCUtils.convertLocalTimeToStr(this.satelliteTimeDelay);


    }


    public void setRestartTime(double paraValue){
        if(this.satelliteTime!=null){
            LocalDateTime restartTimeNow = this.satelliteTime.plusSeconds(-(long)paraValue);
            if(this.restartTime==null){
                this.restartTime=restartTimeNow;
                this.restartTimeStr=this.restartTime.format(UTCUtils.dateTimeFormatter);
            }
            else if(!this.restartTime.equals(restartTimeNow)){
                this.restartTimeChangeTimes++;
            }else{
                this.restartTimeChangeTimes=0;
            }
            if(this.restartTimeChangeTimes>3){
                this.restartTime=restartTimeNow;
                this.restartTimeStr=this.restartTime.format(UTCUtils.dateTimeFormatter);
            }
        }
    }
    public void setResRestartDelayTime(double paraValue){

        if(this.satelliteTimeDelay!=null){
            this.restartTimeDelay = this.satelliteTimeDelay.plusSeconds(-(long) paraValue);
            this.restartTimeDelayStr=UTCUtils.convertLocalTimeToStr(this.restartTimeDelay);

        }
    }

    public void setRestartTime(Date restartTime){

        if(restartTime==null){
            return;
        }
        this.restartTime=UTCUtils.convertDateToLocalDateTime(restartTime);

        this.restartTimeStr=UTCUtils.convertLocalTimeToStr(this.restartTime);
    }

    public boolean judgeRestart(){
        if(this.restartTimeChangeTimes>3){
            this.restartTimeChangeTimes=0;
            return true;
        }else{
            return false;
        }
    }
}
