package com.fang.config.satellite.paraParser;

import com.fang.config.exception.ExceptionManager;
import com.fang.config.satellite.configStruct.FrameConfigClass;
import com.fang.config.satellite.configStruct.SatelliteConfigClass;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.service.setExcpetionJuge.ThresholdInfo;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface ParaParser {
   String getDisplayValue(String paraCode,Double paraValue);

   void init(String satelliteName, SatelliteDb satelliteDb, List<ThresholdInfo> thresholdInfoList);


  default void setSatelliteConfigClass(SatelliteConfigClass satelliteConfigClass){}
   SatelliteConfigClass getSatelliteConfigClass();

   FrameConfigClass getFrameConfigClass(Integer catalogCode,Integer frameCode,Integer reuseChannel);





   void threadInit();
   void threadFinsh();

}
