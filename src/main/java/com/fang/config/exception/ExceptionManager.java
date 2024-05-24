package com.fang.config.exception;

import java.util.Map;

public interface ExceptionManager {
    boolean judgeMatch(Map<String, Double> real, String paraCode, Double paraValue);

    default void initThread() {
    }

    default void detroyThread() {
    }
}
