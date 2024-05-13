package com.fang.config.exception;

import java.util.Map;

public interface JudeExpression {
    default void init(String minStr ,String maxStr){}
    default  void refresh(Double paraValue){}
    default void initFormula(ExceptionManager exceptionManager){}
    boolean judgeMatch(Double paraValue, Map<String, Double> real, String paraCode);
}
