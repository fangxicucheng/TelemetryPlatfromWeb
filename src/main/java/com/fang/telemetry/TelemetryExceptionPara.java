package com.fang.telemetry;

import com.fang.database.postgresql.entity.ExceptionParameterInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryExceptionPara {
    private String paraCode;
    private String paraName;
    private String frameName;
    private String paraValue;
    private String satelliteTime;
    private String threshold;
    private String subsystemName;
    private String subssystemChargerContact;
    public TelemetryExceptionPara(ExceptionParameterInfo exceptionParameterInfo){
        this.paraCode=exceptionParameterInfo.getParaCode();
        this.paraName=exceptionParameterInfo.getParaName();
        this.frameName=exceptionParameterInfo.getFrameName();
        this.paraValue=exceptionParameterInfo.getParaValue();
        this.threshold=exceptionParameterInfo.getThreshold();
        this.satelliteTime=exceptionParameterInfo.getSatelliteTime();
        this.subsystemName=exceptionParameterInfo.getSubsystemName();
        this.subssystemChargerContact= exceptionParameterInfo.getSubssystemChargerContact();
    }
}
