package com.fang.config.exception;

import com.fang.config.exception.judgeStruct.ProgramJudge;
import com.fang.config.exception.judgeStruct.StateJudge;
import com.fang.config.exception.judgeStruct.ThresholdJudge;
import com.fang.config.exception.judgeStruct.UnchangedJudge;
import com.fang.database.postgresql.entity.CountPara;
import com.fang.utils.StringConvertUtils;
import lombok.Data;

import java.util.Map;
@Data
public class SingleExceptionManager {
    private boolean hasCondition;
    private String conditionParaCode;
    JudgeExpression conditionJudge;
    JudgeExpression judgeExpression;
    private boolean unChanged;
    public void init(String minStr, String maxStr, String condition, ExceptionManager exceptionManager) {
        initCondition(condition);
        initJudgeExpression(minStr,maxStr,exceptionManager);
        this.unChanged=false;
    }
    public void initJudgeExpression(String minStr, String maxStr, ExceptionManager exceptionManager) {

        if (!StringConvertUtils.testStringEmpty(minStr)) {
            if (minStr.equals(maxStr)) {
                if (minStr.contains("不变")) {
                    this.judgeExpression = new UnchangedJudge();
                    this.unChanged=true;

                } else {
                    this.judgeExpression = new StateJudge();
                    this.judgeExpression.init(minStr, maxStr);
                }

            } else {
                this.judgeExpression = new ThresholdJudge();
                this.judgeExpression.init(minStr, maxStr);
            }

        } else if (!StringConvertUtils.testStringEmpty(maxStr)) {
            this.judgeExpression = new ThresholdJudge();
            this.judgeExpression.init(minStr, maxStr);
        } else {
            this.judgeExpression = new ProgramJudge();
            this.judgeExpression.initFormula(exceptionManager);
        }
    }
    public Double getParaValue(){
        return this.judgeExpression.getParaValue();
    }



    public void initCondition(String condition) {
        this.hasCondition=false;
        if (condition != null && condition.contains("=")) {
            String[] conditionInfoArray = condition.split("=");
            this.conditionParaCode = conditionInfoArray[0];
            this.conditionJudge = new StateJudge();
            this.hasCondition=true;
            this.conditionJudge.init(conditionInfoArray[1], conditionInfoArray[1]);
        }
    }
    public void refreshUnchanged(Double paraValue) {
        if (this.judgeExpression != null) {
            this.judgeExpression.refreshUnchanged(paraValue);
        }
    }
    public boolean matchCondition(Map<String, Double> realMap) {
        if (hasCondition && realMap.containsKey(this.conditionParaCode)) {
            this.conditionJudge.judgeMatch(realMap.get(this.conditionParaCode), realMap, this.conditionParaCode);
        }
        return true;
    }
    public boolean judgeParaValue(Double paraValue, Map<String, Double> realMap, String paraCode) {
        return this.judgeExpression.judgeMatch(paraValue, realMap, paraCode);
    }


}
