package com.fang.config.exception.judgeStruct;

import com.fang.config.exception.ExceptionManager;
import com.fang.config.exception.JudeExpression;

import java.util.Map;

public class ProgramJudge implements JudeExpression {
    ExceptionManager exceptionManager;
    @Override
    public void initFormula(ExceptionManager exceptionManager) {
       this.exceptionManager=exceptionManager;
    }

    @Override
    public boolean judgeMatch(Double paraValue, Map<String,Double>realMap,String paraCode) {

        return exceptionManager.judgeException(realMap,paraCode,paraValue);
    }
}
