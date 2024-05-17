package com.fang.config.satellite.configStruct;

import com.fang.config.exception.ExceptionManager;
import com.fang.config.exception.ParaJudge;
import com.fang.database.postgresql.entity.ParaConfigLineDb;
import com.fang.service.setExcpetionJuge.ThresholdInfo;
import com.fang.utils.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParaConfigLineConfigClass {
    private int bitStart;
    private int bitNum;
    private String paraName;
    private String paraCode;
    private Double dimension;
    private HandleType handleType;
    private SourceCodeSaveType sourceCodeSaveType;
    private boolean needJudgeException;
    private Map<Double, String> stateParseMap;
    private ParaJudge paraJudge;
    private ThreadLocal<Integer> count;

    public ParaConfigLineConfigClass(ParaConfigLineDb paraConfigLineDb) {
        this.bitStart = paraConfigLineDb.getBitStart();
        this.bitNum = paraConfigLineDb.getBitNum();
        this.paraCode = paraConfigLineDb.getParaCode();
        this.paraName = paraConfigLineDb.getParaName();
        this.dimension = ParseUtils.getDimension(paraConfigLineDb.getDimension());
        ParseUtils.initParaConfigClass(paraConfigLineDb, this);
        this.count = new ThreadLocal<>();
        this.needJudgeException = false;
    }

    public boolean checkUnchanged() {
        return this.paraJudge.checkUnchanged();
    }
    public Double getParaJudgeBufferValue(){
        return this.paraJudge.getParaValue();
    }

    public void initExceptionManager(ThresholdInfo thresholdInfo, ExceptionManager exceptionManager) {
        this.needJudgeException = true;
        this.paraJudge = new ParaJudge(thresholdInfo, exceptionManager);

    }

    public String getParseState(double paraValue) {
        if (this.stateParseMap == null || !this.stateParseMap.containsKey(paraValue)) {
            return Double.toString(paraValue);
        }
        return this.stateParseMap.get(paraValue);
    }

    public void initThread() {
        this.count.set(0);
    }

    public void destroyThread() {
        this.count.remove();
        if (this.paraJudge != null) {
            this.paraJudge.destroyThread();
        }
    }

    public boolean judgeException(double paraValue, Map<String, Double> localMap) {
        if (this.needJudgeException) {
            return this.paraJudge.judgeException(paraValue, localMap, this.paraCode);
        }
        return true;
    }

    public Integer getParaCodeCount() {
        this.count.set(this.count.get() + 1);
        return this.count.get();
    }

}
