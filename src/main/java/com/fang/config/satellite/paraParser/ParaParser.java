package com.fang.config.satellite.paraParser;

import com.fang.config.exception.ExceptionManager;
import com.fang.config.satellite.configStruct.FrameConfigClass;
import com.fang.config.satellite.configStruct.SatelliteConfigClass;
import com.fang.database.postgresql.entity.CountPara;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.service.setExcpetionJuge.ThresholdInfo;
import com.fang.service.telemetryService.SatelliteTimeManager;
import com.fang.telemetry.TelemetryFrame;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ParaParser {
   String getDisplayValue(String paraCode, Double paraValue, SatelliteTimeManager satelliteTimeManager);

   void init(String satelliteName, SatelliteDb satelliteDb, List<ThresholdInfo> thresholdInfoList);

   FrameInfo parseFrameInfoFromBytes(byte[]receiveBytes);
   void parseTelemetryFrame(byte[]dataBytes,FrameInfo frameInfo,TelemetryFrame telemetryFrame);

   void setUnchangedParaValue();

   Map<String,Double>getUnchangedParaValue();
  void updateUnchangedParaValue();

  default void setSatelliteConfigClass(SatelliteConfigClass satelliteConfigClass){}
   SatelliteConfigClass getSatelliteConfigClass();

   FrameConfigClass getFrameConfigClass(Integer catalogCode,Integer frameCode,Integer reuseChannel);

   default double getSpecialFormulaValue(String paraCode,double paraValue){
       return paraValue;
   }



   default Map<String,Double> getInitRealMap(){
       return new HashMap<>();
   }


   void initThread();
   void destroyThread();

}
