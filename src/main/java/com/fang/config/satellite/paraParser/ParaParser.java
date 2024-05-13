package com.fang.config.satellite.paraParser;

import com.fang.config.exception.ExceptionManager;
import com.fang.config.satellite.configStruct.FrameConfigClass;
import com.fang.config.satellite.configStruct.SatelliteConfigClass;
import jakarta.persistence.criteria.CriteriaBuilder;

public interface ParaParser {
   String getDisplayValue(String paraCode,Double paraValue);



   void setSatelliteConfigClass(SatelliteConfigClass satelliteConfigClass);
   SatelliteConfigClass getSatelliteConfigClass();

   FrameConfigClass getFrameConfigClass(Integer catalogCode,Integer frameCode,Integer reuseChannel);

   void setExceptionManager(ExceptionManager exceptionManager);

   void threadInit();
   void threadFinsh();

}
