package com.fang.telemetry;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryFrame {
    private String telemetryPlanId;
    private String frameName;
    private int serialNum;
    private int frameNum;
    private int delayFlag;
    private String satelliteTime;
    private String restartTime;
    private String realTimeContent;
    private int needInsertCommand;
    private int hasInsertCommand;
    private int errorCodeNum;
    private Integer frameFlag;
    private int order;
    private boolean isCorrSate;
    private double errorRate;
    @JSONField(name="parameterList")
    private List<TelemetryParameterModel>parameterList;

    public void addParameter(TelemetryParameterModel parameterModel){
        if(this.parameterList==null){
            this.parameterList=new ArrayList<>();
        }
        this.parameterList.add(parameterModel);
    }
}
