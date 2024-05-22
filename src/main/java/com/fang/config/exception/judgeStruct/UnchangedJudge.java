package com.fang.config.exception.judgeStruct;

import com.fang.config.exception.JudgeExpression;
import com.fang.database.postgresql.entity.CountPara;

import java.util.Map;

public class UnchangedJudge implements JudgeExpression {
    private ThreadLocal<Double> oldValue;
    private ThreadLocal<Integer> changedTimes;

    @Override
    public void init(String minStr, String maxStr) {
        this.oldValue = new ThreadLocal<>();
        this.changedTimes = new ThreadLocal<>();
    }

    @Override
    public void refreshUnchanged(Double oldValue) {

        this.oldValue.set(oldValue);
        this.changedTimes.set(0);
    }

    @Override
    public Double getParaValue() {
        return this.oldValue.get();
    }


    @Override
    public void destroyThread() {
        this.oldValue.remove();
        this.changedTimes.remove();
    }


    @Override
    public boolean judgeMatch(Double paraValue, Map<String,Double>realMap,String paraCode) {
        boolean result = false;
        Double buffer = oldValue.get();
        Integer times = this.changedTimes.get();
        if (paraValue != buffer) {
            times++;
        }
        if (times > 3) {
            result = true;
            this.oldValue.set(paraValue);
            this.changedTimes.set(0);
        }
        return result;
    }
}
