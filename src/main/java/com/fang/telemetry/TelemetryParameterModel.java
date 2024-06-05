package com.fang.telemetry;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fang.database.postgresql.entity.ParaConfigLineDb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryParameterModel {
    @JSONField(name = "ParaCode")
    private String paraCode;
    @JSONField(name = "ParaName")
    private String paraName;
    @JSONField(name="ParaValue")
    private String paraValue;
    @JSONField(name="ParaValueDouble")
    private double paraDouble;
    @JSONField(name="ParaHex")
    private String paraHex;
    @JSONField(name="RecieveCount")
    private Integer receiveCount;
    @JSONField(name="Expression")
    private String expression;
//    @JSONField(name="name")
//    private String paraName;
    @JSONField(name="ExpFlag")
    private boolean expFlag;

    public TelemetryParameterModel(ParaConfigLineDb configLineDb){
        this.paraCode=configLineDb.getParaCode();
        this.paraName=configLineDb.getParaName();
        this.expression=configLineDb.getExp();

    }
//    @JSONField(serialize = false)
//    private String subsystemName;
//    @JSONField(serialize = false)
//    private String subsystemChargerContract;
}
