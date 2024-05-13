package com.fang.config.exception.satelliteExcetionManager;

import com.fang.config.exception.ExceptionManager;

import java.util.Map;

public class BaseExceptionManager implements ExceptionManager {

    @Override
    public boolean judgeException(Map<String, Double> real, String paraCode, Double paraValue) {
        return true;
    }
}
