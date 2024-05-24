package com.fang.config.exception.satelliteExceptionManager;

import com.fang.config.exception.ExceptionManager;

import java.util.Map;

public class BaseExceptionManager implements ExceptionManager {

    @Override
    public boolean judgeMatch(Map<String, Double> real, String paraCode, Double paraValue) {
        return true;
    }
}
