package com.fang.config.satellite.configStruct;

import com.fang.database.postgresql.entity.FrameDb;
import com.fang.database.postgresql.entity.ParaConfigLineDb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrameConfigClass {
    private List<ParaConfigLineConfigClass> configLineList;
    private Map<String,ParaConfigLineConfigClass>configlineMap;
    private String frameName;
    private Integer frameCode;
    private Integer reuseChannel;
    public FrameConfigClass(FrameDb frameDb)
    {
        this.frameName=frameDb.getFrameName();
        this.frameCode=frameDb.getFrameCode();
        this.reuseChannel=frameDb.getReuseChannel();
        for (ParaConfigLineDb paraConfigLineDb : frameDb.getParaConfigLineDbList()) {
            if(configLineList==null){
                configLineList=new ArrayList<>();
            }
            if(configlineMap==null){
                configlineMap=new HashMap<>();
            }
            ParaConfigLineConfigClass paraConfigLineConfigClass=new ParaConfigLineConfigClass(paraConfigLineDb);
            this.configLineList.add(paraConfigLineConfigClass);
            this.configlineMap.put(paraConfigLineDb.getParaCode(),paraConfigLineConfigClass);
        }


    }
}
