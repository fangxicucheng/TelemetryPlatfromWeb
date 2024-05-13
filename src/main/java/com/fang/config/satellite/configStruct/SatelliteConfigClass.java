package com.fang.config.satellite.configStruct;

import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.database.postgresql.entity.SatelliteDb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SatelliteConfigClass {
    private String satelliteName;
    private String satelliteId;

    private Map<Integer, FrameCatalogConfigClass> catalogCodeConfigClassMap;
    private Map<String, FrameCatalogConfigClass> catalogNameConfigClassMap;

    public SatelliteConfigClass(SatelliteDb satelliteDb) {
        this.satelliteName = satelliteDb.getSatelliteName();
        this.satelliteId = satelliteDb.getSatelliteId();
        for (FrameCatalogDb frameCatalogDb : satelliteDb.getFrameCatalogDbList()) {
            FrameCatalogConfigClass frameCatalogConfigClass=new FrameCatalogConfigClass(frameCatalogDb);
            if(this.catalogCodeConfigClassMap==null){
                this.catalogCodeConfigClassMap=new HashMap<>();
            }
            if(this.catalogNameConfigClassMap==null){
                this.catalogNameConfigClassMap=new HashMap<>();
            }
            this.catalogNameConfigClassMap.put(frameCatalogDb.getCatalogName(),frameCatalogConfigClass);
            this.catalogCodeConfigClassMap.put(frameCatalogDb.getCatalogCode(),frameCatalogConfigClass);
        }
    }











    public FrameConfigClass getFrameConfigClassByFrameCode(Integer catalogCode,Integer frameCode,Integer refuseChannel ){
        FrameConfigClass frameConfigClass=null;
        if(this.catalogCodeConfigClassMap.containsKey(catalogCode)){
            frameConfigClass=this.catalogCodeConfigClassMap.get(catalogCode).getFrameConfigClassByFrameCode(frameCode,refuseChannel);
        }
        return frameConfigClass;

    }
    public FrameConfigClass getFrameConfigClassByFrameName(String frameName){
        FrameConfigClass frameConfigClass=null;

        for (FrameCatalogConfigClass catalog : this.catalogNameConfigClassMap.values()) {

            FrameConfigClass frameConfigByFrameName = catalog.getFrameConfigByFrameName(frameName);
            if (frameConfigByFrameName!=null) {
                frameConfigClass=frameConfigByFrameName;
                break;
            }
        }
        return frameConfigClass;
    }
}
