package com.fang.service.setExcpetionJuge;
import com.alibaba.fastjson2.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThresholdInfo {

    @JSONField(name="Num")
    private Integer num;
    private boolean isModified;
    @JSONField(name="ParaCode")
    private String paraCode;
    @JSONField(serialize = false)
    private String paraName;
    @JSONField(name="ThresholdMax")
    private String thresholdMax;
    @JSONField(name="ThresholdMin")
    private String thresholdMin;
    @JSONField(name="Condition")
    private String condition;
}
