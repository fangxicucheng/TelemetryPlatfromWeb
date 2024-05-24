package com.fang.config.exception.judgeStruct;

import com.fang.config.exception.ExceptionManager;
import com.fang.config.exception.JudgeExpression;

import java.util.Map;

public class ProgramJudge implements JudgeExpression {
    ExceptionManager exceptionManager;
    @Override
    public void initFormula(ExceptionManager exceptionManager) {
       this.exceptionManager=exceptionManager;
    }

    @Override
    public boolean judgeMatch(Double paraValue, Map<String,Double>realMap,String paraCode) {

        return exceptionManager.judgeMatch(realMap,paraCode,paraValue);
    }
}
