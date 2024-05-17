package com.fang.config.exception;

import com.fang.database.postgresql.entity.CountPara;

import java.util.Map;

public interface JudgeExpression {
    default void init(String minStr ,String maxStr){}
    default  void refreshUnchanged(Double paraValue){}
    default Double getParaValue(){return null;}

    default void destroyThread(){}
    default void initFormula(ExceptionManager exceptionManager){}
    boolean judgeMatch(Double paraValue, Map<String, Double> real, String paraCode);
}
