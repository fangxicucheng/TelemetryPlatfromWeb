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
    private Map<Double,String> stateParseMap;
    private ParaJudge paraJudge;
    private ThreadLocal<Integer>count;

    public ParaConfigLineConfigClass(ParaConfigLineDb paraConfigLineDb) {
        this.bitStart=paraConfigLineDb.getBitStart();
        this.bitNum=paraConfigLineDb.getBitNum();
        this.paraCode=paraConfigLineDb.getParaCode();
        this.paraName=paraConfigLineDb.getParaName();
        this.dimension= ParseUtils.getDimension(paraConfigLineDb.getDimension());
        ParseUtils.initParaConfigClass(paraConfigLineDb,this);
        this.needJudgeException=false;
    }

    public void initExceptionManager(ThresholdInfo thresholdInfo, ExceptionManager exceptionManager){
        this.needJudgeException=true;
        this.paraJudge=new ParaJudge(thresholdInfo,exceptionManager);

    }
    public void removeThreadLocal(){
        if(this.count!=null){
            this.count.remove();
        }
    }
    public Integer getParaCodeCount(){
        this.count.set(this.count.get()+1);
        return this.count.get();
    }

    public void refresh(){
        this.count.set(0);
    }
}
