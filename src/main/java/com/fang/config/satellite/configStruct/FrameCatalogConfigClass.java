package com.fang.config.satellite.configStruct;

import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.database.postgresql.entity.FrameDb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrameCatalogConfigClass {
    private Map<Integer,Map<Integer,FrameConfigClass>> frameCodeMap;
    private Map<String,FrameConfigClass> frameNameMap;
    private String catalogName;
    private Integer catalogCode;

    public FrameCatalogConfigClass(FrameCatalogDb catalogDb) {
    this.catalogName=catalogDb.getCatalogName();
    this.catalogCode=catalogDb.getCatalogCode();
        for (FrameDb frameDb : catalogDb.getFrameDbList()) {
            FrameConfigClass frameConfigClass = new FrameConfigClass(frameDb);
            if(frameNameMap==null){
                frameNameMap=new HashMap<>();
            }
            frameNameMap.put(frameConfigClass.getFrameName(),frameConfigClass);
            if(frameCodeMap==null){
                frameCodeMap=new HashMap<>();
            }
            if(!frameCodeMap.containsKey(frameConfigClass.getFrameCode())){
                frameCodeMap.put(frameConfigClass.getFrameCode(),new HashMap<>());
            }
            frameCodeMap.get(frameConfigClass.getFrameCode()).put(frameConfigClass.getReuseChannel(),frameConfigClass);
        }

    }
    public void initTread(){
        Collection<FrameConfigClass> frameList = frameNameMap.values();
        if(frameList!=null){
            for (FrameConfigClass frame : frameList) {

                frame.initThread();
            }
        }

    }
    public void destroyThread(){
        Collection<FrameConfigClass> frameList = frameNameMap.values();
        if(frameList!=null){
            for (FrameConfigClass frame : frameList) {

                frame.destroyThread();
            }
        }

    }


    public FrameConfigClass getFrameConfigClassByFrameCode(Integer frameCode,Integer reuseChannel){
        FrameConfigClass frameConfigClass =null;
        if(this.frameCodeMap!=null){
            if(this.frameCodeMap.containsKey(frameCode)){
                if(this.frameCodeMap.get(frameCode).containsKey(reuseChannel)){
                    frameConfigClass=this.frameCodeMap.get(frameCode).get(reuseChannel);
                }
            }
        }
        return frameConfigClass;
    }


    public FrameConfigClass getFrameConfigByFrameName(String frameName){
        FrameConfigClass frameConfigClass=null;
        if(this.frameNameMap!=null){
            if(this.frameNameMap.containsKey(frameName)){
                frameConfigClass=this.frameNameMap.get(frameName);
            }
        }
        return frameConfigClass;
    }
}
