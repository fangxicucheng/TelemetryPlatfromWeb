package com.fang.telemetry;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryParameterModel {
    @JSONField(name = "ParaCode")
    private String paraCode;
    @JSONField(name="ParaValue")
    private String paraValue;
    @JSONField(name="ParaValueDouble")
    private double paraDouble;
    @JSONField(name="ParaHex")
    private String paraHex;
    @JSONField(name="ReceiveCount")
    private Integer receiveCount;
//    @JSONField(name="name")
//    private String paraName;
    @JSONField(name="ExpFlag")
    private boolean isException;
//    @JSONField(serialize = false)
//    private String subsystemName;
//    @JSONField(serialize = false)
//    private String subsystemChargerContract;
}
