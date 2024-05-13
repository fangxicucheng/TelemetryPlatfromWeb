package com.fang.config.exception;

import com.fang.service.setExcpetionJuge.ThresholdInfo;
import com.fang.utils.StringConvertUtils;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParaJudge {

    List<SingleExceptionManager> exceptionManagerList;

    public ParaJudge(ThresholdInfo thresholdInfo, ExceptionManager exceptionManager) {
        this.exceptionManagerList = new ArrayList<>();
        String condition = thresholdInfo.getCondition();
        String minThresholdStr = thresholdInfo.getThresholdMin();
        String maxThresholdStr = thresholdInfo.getThresholdMax();

        String[] minStrArray = StringConvertUtils.testStringEmpty(minThresholdStr) ? null : minThresholdStr.split(";");
        String[] maxStrArray = StringConvertUtils.testStringEmpty(maxThresholdStr) ? null : maxThresholdStr.split(";");
        String[] conditionArray = StringConvertUtils.testStringEmpty(condition) ? null : condition.split(";");
        int number = 0;
        number = minStrArray == null ? number : Math.max(number, minStrArray.length);
        number = maxStrArray == null ? number : Math.max(number, maxStrArray.length);
        number = conditionArray == null ? number : Math.max(number, conditionArray.length);
        if (number == 0) {
            SingleExceptionManager singleExceptionManager = new SingleExceptionManager();
            singleExceptionManager.init(minThresholdStr, maxThresholdStr, condition, exceptionManager);
        } else {
            for (int i = 0; i < number; i++) {

                String min = minStrArray != null && minStrArray.length > i ? minStrArray[i] : null;
                String max = maxStrArray != null && maxStrArray.length > i ? maxStrArray[i] : null;
                String con = conditionArray != null && conditionArray.length > i ? conditionArray[i] : null;
                SingleExceptionManager singleExceptionManager = new SingleExceptionManager();
                singleExceptionManager.init(min,max,con,exceptionManager);
                this.exceptionManagerList.add(singleExceptionManager);
            }

        }
    }

    public boolean judgeException(Double paraValue, Map<String,Double>realMap,String paraCode){

        for (SingleExceptionManager singleExceptionManager : this.exceptionManagerList) {

            if (singleExceptionManager.matchCondition(realMap)) {

                singleExceptionManager.judgeParaValue(paraValue,realMap,paraCode);
            }
        }
        return true;

    }
    public void refresh(Double paraValue){

        for (SingleExceptionManager singleExceptionManager : this.exceptionManagerList) {
            singleExceptionManager.refresh(paraValue);
        }
    }

}
