package com.fang.config.satellite.configStruct;

import com.fang.database.postgresql.entity.ParaConfigLineDb;
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

    public ParaConfigLineConfigClass(ParaConfigLineDb paraConfigLineDb) {
        this.bitStart=paraConfigLineDb.getBitStart();
        this.bitNum=paraConfigLineDb.getBitNum();
        this.paraCode=paraConfigLineDb.getParaCode();
        this.paraName=paraConfigLineDb.getParaName();
        this.dimension= ParseUtils.getDimension(paraConfigLineDb.getDimension());
        ParseUtils.initParaConfigClass(paraConfigLineDb,this);

    }
}
