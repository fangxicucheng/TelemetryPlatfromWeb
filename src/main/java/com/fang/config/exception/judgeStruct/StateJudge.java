package com.fang.config.exception.judgeStruct;

import com.fang.config.exception.JudgeExpression;
import com.fang.utils.StringConvertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StateJudge implements JudgeExpression {
    private List<Double> stateBufferList;

    @Override
    public void init(String minStr , String maxStr){
        this.stateBufferList=new ArrayList<>();
        if(minStr!=null&&!minStr.isEmpty())
        {
            for (String str : minStr.split(",")) {

                Double state = StringConvertUtils.strConvertToDouble(str);
                this.stateBufferList.add(state);
            }
        }
    }

    @Override
    public boolean judgeMatch(Double paraValue, Map<String, Double> real, String paraCode) {


        return this.stateBufferList.contains(paraValue);

    }
}
