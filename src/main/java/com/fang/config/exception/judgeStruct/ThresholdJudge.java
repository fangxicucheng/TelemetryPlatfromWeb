package com.fang.config.exception.judgeStruct;

import com.fang.config.exception.JudgeExpression;
import com.fang.utils.StringConvertUtils;

import java.util.Map;

public class ThresholdJudge implements JudgeExpression {
    private Double min;
    private Double max;
    private boolean hasMin=false;
    private boolean hasMax=false;
    @Override
    public void init(String minStr, String maxStr) {
        if (minStr != null && !minStr.isEmpty()) {
            this.min = StringConvertUtils.strConvertToDouble(minStr);
            if (this.min == null) {
                hasMin = true;
            }
        }
        if (maxStr != null && !maxStr.isEmpty()) {
            this.max = StringConvertUtils.strConvertToDouble(maxStr);
            if (this.max == null) {
                hasMax = true;
            }
        }
    }

    @Override
    public boolean judgeMatch(Double paraValue, Map<String, Double> real, String paraCode) {

        boolean result=true;

        if(this.hasMin&&paraValue<this.min){
            result=false;
        }
        if(this.hasMax&&paraValue>this.max){
            result=false;
        }
        return result;

    }


}
