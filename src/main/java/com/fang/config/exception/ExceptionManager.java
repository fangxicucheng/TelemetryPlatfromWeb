package com.fang.config.exception;

import java.util.Map;

public interface ExceptionManager {
     boolean judgeException(Map<String, Double> real, String paraCode, Double paraValue);
}
