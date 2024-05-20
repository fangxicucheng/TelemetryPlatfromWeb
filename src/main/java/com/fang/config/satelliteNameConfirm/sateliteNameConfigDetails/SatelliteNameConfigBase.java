package com.fang.config.satelliteNameConfirm.sateliteNameConfigDetails;

import com.fang.config.satelliteNameConfirm.SatelliteNameConfirmInterface;
import com.fang.database.postgresql.entity.SatelliteNameConfig;

public class SatelliteNameConfigBase implements SatelliteNameConfirmInterface {


    @Override
    public void setInitSatelliteName(String satelliteName) {

    }

    @Override
    public void init(SatelliteNameConfig satelliteNameConfig) {

    }

    @Override
    public void checkFrame(String frameName) {

    }

    @Override
    public boolean setSatelliteNameFromParam(String paraCode, String paraValue) {
        return false;
    }
}
