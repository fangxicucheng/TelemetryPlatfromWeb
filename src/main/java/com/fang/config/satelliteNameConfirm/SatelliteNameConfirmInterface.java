package com.fang.config.satelliteNameConfirm;

import com.fang.database.postgresql.entity.SatelliteNameConfig;
import com.fang.telemetry.TelemetryFrameModel;

public interface SatelliteNameConfirmInterface {

    void setInitSatelliteName(String satelliteName);

    /// <summary>
    /// </summary>
    /// <param name="satelliteName">初始化卫星名</param>
    /// <param name="paraCode">卫星名所处的参数代号</param>
    void init(SatelliteNameConfig satelliteNameConfig);

    void checkFrame(String frameName);

    default void setSatelliteName(TelemetryFrameModel frame) {
//        if (needCheckSatellite() && checkFrameName(frame)) {
//            for (TelemetryParameterModel telemetryParameterModel : frame.getParameterList()) {
//                setSatelliteNameInfoFromPara(telemetryParameterModel.getParaCode(),telemetryParameterModel.getParaValue());
//            }
//        }
    }

    default boolean checkFrameName(TelemetryFrameModel frame) {
        return false;
    }

    /// <summary>
    /// </summary>
    /// <param name="paraCode">解析后参数代号</param>
    /// <param name="paraValue">解析后的参数值</param>
    boolean setSatelliteNameFromParam(String paraCode, String paraValue);

    default boolean isCorrectSatellite() {
        return true;
    }

    default boolean needCheckSatellite() {
        return false;
    }

    default boolean allCorrectSatellite() {
        return true;
    }

   default void setSatelliteNameInfoFromPara(String paraCode, String paraValue){

    }
}
