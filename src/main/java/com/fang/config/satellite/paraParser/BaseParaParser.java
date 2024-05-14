package com.fang.config.satellite.paraParser;

import com.fang.config.exception.ExceptionManager;
import com.fang.config.satellite.configStruct.FrameCatalogConfigClass;
import com.fang.config.satellite.configStruct.FrameConfigClass;
import com.fang.config.satellite.configStruct.ParaConfigLineConfigClass;
import com.fang.config.satellite.configStruct.SatelliteConfigClass;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.service.setExcpetionJuge.ThresholdInfo;
import com.fang.utils.ConfigUtils;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

import java.util.*;

@NoArgsConstructor
public class BaseParaParser implements ParaParser {
    private String satelliteTime;
    private String satelliteTime_延时;
    private String restartTime;
    private String restartTime_延时;
    private String satelliteName;
    private SatelliteConfigClass satelliteConfigClass;
    private ExceptionManager exceptionManager;
    private boolean isBd;

    @Override
    public String getDisplayValue(String paraCode, Double paraValue) {
        return paraValue.toString();
    }

    @Override
    public void init(String satelliteName, SatelliteDb satelliteDb, List<ThresholdInfo> thresholdInfoList) {
        this.satelliteName = satelliteName;
        this.isBd = false;

        if(this.satelliteName.contains("北斗")){
            isBd=true;
        }
        this.satelliteConfigClass = new SatelliteConfigClass(satelliteDb);
        this.exceptionManager = ConfigUtils.createExceptionManager(satelliteName);
        setExceptionManager(this.exceptionManager,thresholdInfoList);

    }

    private void setExceptionManager(ExceptionManager exceptionManager, List<ThresholdInfo> thresholdInfoList) {
        Map<String, ParaConfigLineConfigClass> configLineMap = new HashMap<>();
     Map<String,FrameCatalogConfigClass>  catalogMap= this.satelliteConfigClass.getCatalogNameConfigClassMap();
        if(catalogMap==null||catalogMap.size()==0){
            return;
        }

        for (FrameCatalogConfigClass catalog : catalogMap.values()) {
            Map<String,FrameConfigClass> frameMap = catalog.getFrameNameMap();
            if(frameMap==null||frameMap.size()==0){
                continue;
            }
            for (FrameConfigClass frame : frameMap.values()) {
                List<ParaConfigLineConfigClass> configLineList = frame.getConfigLineList();
                if(configLineList==null||configLineList.size()==0){
                    continue;
                }
                for (ParaConfigLineConfigClass configLine :configLineList) {
                    configLineMap.put(configLine.getParaCode(), configLine);
                }
            }
        }

        for (ThresholdInfo thresholdInfo : thresholdInfoList) {

            if(configLineMap.containsKey(thresholdInfo.getParaCode())){
                configLineMap.get(thresholdInfo.getParaCode()).initExceptionManager(thresholdInfo,exceptionManager);
            }

        }
    }


    @Override
    public void setSatelliteConfigClass(SatelliteConfigClass satelliteConfigClass) {

        this.satelliteConfigClass = satelliteConfigClass;
    }

    @Override
    public SatelliteConfigClass getSatelliteConfigClass() {
        return this.satelliteConfigClass;
    }

    @Override
    public FrameConfigClass getFrameConfigClass(Integer catalogCode, Integer frameCode, Integer reuseChannel) {
        return this.satelliteConfigClass.getFrameConfigClassByFrameCode(catalogCode, frameCode, reuseChannel);
    }


    @Override
    public void threadInit() {

    }

    @Override
    public void threadFinsh() {

    }
}
