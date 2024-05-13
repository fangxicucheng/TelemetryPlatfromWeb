package com.fang.config.satellite.paraParser;

import com.fang.config.exception.ExceptionManager;
import com.fang.config.satellite.configStruct.FrameConfigClass;
import com.fang.config.satellite.configStruct.SatelliteConfigClass;
import com.fang.database.postgresql.entity.SatelliteDb;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

import java.util.Calendar;
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
    public void init(String satelliteName, SatelliteDb satelliteDb) {
        this.satelliteName=satelliteName;
        this.isBd=false;
        this.satelliteConfigClass=new SatelliteConfigClass(satelliteDb);
    }




    @Override
    public void setSatelliteConfigClass(SatelliteConfigClass satelliteConfigClass) {

        this.satelliteConfigClass=satelliteConfigClass;
    }

    @Override
    public SatelliteConfigClass getSatelliteConfigClass() {
        return this.satelliteConfigClass;
    }

    @Override
    public FrameConfigClass getFrameConfigClass(Integer catalogCode, Integer frameCode, Integer reuseChannel) {
        return this.satelliteConfigClass.getFrameConfigClassByFrameCode(catalogCode,frameCode,reuseChannel);
    }



    @Override
    public void threadInit() {

    }

    @Override
    public void threadFinsh() {

    }
}
