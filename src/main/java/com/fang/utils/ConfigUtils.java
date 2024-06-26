package com.fang.utils;

import com.fang.config.exception.ExceptionManager;
import com.fang.config.exception.satelliteExceptionManager.BaseExceptionManager;
import com.fang.config.satellite.paraParser.BaseParaParser;
import com.fang.config.satellite.paraParser.ParaParser;
import com.fang.database.postgresql.entity.CommandCount;
import com.fang.database.postgresql.entity.GpsParaConfig;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.service.gpsService.gpsManager.GpsConfigInfo;
import com.fang.service.setExcpetionJuge.ThresholdInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ConfigUtils {

    private static Map<String, String> satelliteNameMap = new HashMap<>();
    private static Map<String, String> satelliteIdMap = new HashMap<>();
    private static Map<String, byte[]> midMap = new HashMap<>();
    private static Map<String, String> icCardMap = new HashMap<>();
    private static Map<String, ParaParser> satelliteParaParserMap = new HashMap<>();
    private static Map<String, List<CommandCount>> commandCountMap = new HashMap<>();
    private static Map<String, GpsConfigInfo> gpsParaConfigMap = new HashMap<>();
    private static String rootPath="D:\\卫星遥测数据监控平台";
    private static String gpsPath="GPS参数";
    private static String dataPath="遥测\\源码";

    public static void setGpsParaConfigInfo(GpsParaConfig gpsParaConfig) {
        gpsParaConfigMap.put(gpsParaConfig.getSatelliteName(), new GpsConfigInfo(gpsParaConfig));
    }



    public static List<CommandCount>getCommandCountList(String satelliteName){
        if(commandCountMap.containsKey(satelliteName)){
            return commandCountMap.get(satelliteName);
        }
        return null;
    }

    public static void setCommandList(String satelliteName,List<CommandCount>commandCountList){
        if(!commandCountMap.containsKey(satelliteName)){
            commandCountMap.put(satelliteName,commandCountList);
        }


    }
    public static String getGPSRootPath(){
        return rootPath+"\\"+gpsPath;
    }
    public static String getDataRootPath(){
        return rootPath+"\\"+dataPath;
    }

    public static GpsConfigInfo getGpsConfigInfo(String satelliteName) {

        if (gpsParaConfigMap.containsKey(satelliteName)) {
            return gpsParaConfigMap.get(satelliteName);
        }
        return null;
    }

    public static void setSatelliteParaParser(SatelliteDb satelliteDb, List<ThresholdInfo> thresholdInfoList) {
        ParaParser paraParser = createParaParser(satelliteDb, thresholdInfoList);
        satelliteParaParserMap.put(satelliteDb.getSatelliteName(), paraParser);
    }

    public static ParaParser getParaParser(String satelliteName) {
        ParaParser paraParser = null;
        if (satelliteParaParserMap.containsKey(satelliteName)) {
            paraParser = satelliteParaParserMap.get(satelliteName);
        }
        return paraParser;

    }

    public static ParaParser createParaParser(SatelliteDb satelliteDb, List<ThresholdInfo> thresholdInfoList) {
        ParaParser paraParser = null;
        String satelliteName = satelliteDb.getSatelliteName();
        if (satelliteName.contains("-")) {
            satelliteName = satelliteName.replaceAll("-", "_");
        }
        String className = "com.fang.config.satellite.paraParser.satelliteParaParser.ParaParser_" + satelliteName;
        try {
            paraParser = (ParaParser) Class.forName(className).newInstance();
            // ConfigUtils.setSatelliteExceptionManager(satelliteName, exceptionManager);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println(satelliteName + "ParaParser未找到类");
            paraParser = new BaseParaParser();
            //  ConfigUtils.setSatelliteExceptionManager(satelliteName, exceptionManager);
        }
        paraParser.init(satelliteName, satelliteDb, thresholdInfoList);
        return paraParser;
    }


//    public static void setSatelliteExceptionManager(String satelliteName,ExceptionManager exceptionManager){
//        exceptionManagerMap.put(satelliteName,exceptionManager);
//    }

//    public static void initSatelliteConfigExceptionManager(String satelliteName, List<ThresholdInfo>thresholdInfoList){
//       // ExceptionManager exceptionManager=exceptionManagerMap.get(satelliteName);
//
//        if(satelliteConfigClassMap.containsKey(satelliteName))
//        {
//            SatelliteConfigClass satelliteConfigClass = satelliteConfigClassMap.get(satelliteName);
//            Map<String, ParaConfigLineConfigClass>paraConfigLineConfigClassMap=new HashMap<>();
//            for (FrameCatalogConfigClass catalog : satelliteConfigClass.getCatalogCodeConfigClassMap().values()) {
//                for (FrameConfigClass frame : catalog.getFrameNameMap().values()) {
//
//                    for (ParaConfigLineConfigClass paraConfigLineConfigClass : frame.getConfigLineList()) {
//
//                        paraConfigLineConfigClassMap.put(paraConfigLineConfigClass.getParaCode(),paraConfigLineConfigClass);
//                    }
//                }
//
//            }
//            for (ThresholdInfo thresholdInfo : thresholdInfoList) {
//                if(!paraConfigLineConfigClassMap.containsKey(thresholdInfo.getParaCode())){
//                    paraConfigLineConfigClassMap.get(thresholdInfo.getParaCode()).initExceptionManager(thresholdInfo,exceptionManager);
//                }
//
//            }
//        }
//
//    }
//    public static void setSatelliteConfigClassMap(SatelliteDb satelliteDb){
//        SatelliteConfigClass satelliteConfigClass=new SatelliteConfigClass(satelliteDb);
//        satelliteConfigClassMap.put(satelliteDb.getSatelliteName(),satelliteConfigClass);
//    }

    //    public static SatelliteConfigClass getSatelliteConfigClass(String satelliteName){
//        SatelliteConfigClass satelliteConfigClass=null;
//        if(satelliteConfigClassMap.containsKey(satelliteName)){
//            satelliteConfigClass=satelliteConfigClassMap.get(satelliteName);
//        }
//        return satelliteConfigClass;
//    }
    public static ExceptionManager createExceptionManager(String satelliteName) {
        if (satelliteName.contains("-")) {
            satelliteName = satelliteName.replaceAll("-", "_");
        }
        String className = "com.fang.config.exception.satelliteExceptionManager.ExceptionManager_" + satelliteName;
        try {
            return (ExceptionManager) Class.forName(className).newInstance();
            // ConfigUtils.setSatelliteExceptionManager(satelliteName, exceptionManager);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println(satelliteName + "Exception未找到类");
            return new BaseExceptionManager();
            //  ConfigUtils.setSatelliteExceptionManager(satelliteName, exceptionManager);
        }
    }

    //    public static void setExceptionManager(String satelliteName) {
//        if (satelliteName.contains("-")) {
//            satelliteName = satelliteName.replaceAll("-", "_");
//        }
//        String className = "com.fang.config.satelliteExceptionManager.ExceptionManager_" + satelliteName;
//        try {
//            ExceptionManager   exceptionManager = (ExceptionManager) Class.forName(className).newInstance();
//            ConfigUtils.setSatelliteExceptionManager(satelliteName, exceptionManager);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            System.out.println(satelliteName + "Exception未找到类");
//            ExceptionManager     exceptionManager = new BaseExceptionManager();
//            ConfigUtils.setSatelliteExceptionManager(satelliteName, exceptionManager);
//        }
//    }
    public static void setSatelliteName(String satelliteName, String satelliteId) {
        if (!satelliteNameMap.containsKey(satelliteName)) {
            satelliteNameMap.put(satelliteName, satelliteId);
        }
        if (!satelliteIdMap.containsKey(satelliteId)) {
            satelliteIdMap.put(satelliteId, satelliteName);
        }
    }

    public static void setIcCard(String satelliteName, String icCard) {
        icCardMap.put(icCard, satelliteName);
    }

    public static void setMid(String satelliteName, byte[] midBytes) {
        if (!midMap.containsKey(satelliteName)) {
            midMap.put(satelliteName, midBytes);
        }
    }

    public static String getSatelliteNameByMid(byte midByte1, byte midByte2) {
        String satelliteName = null;
        for (String satelliteNameKey : midMap.keySet()) {
            byte[] bytes = midMap.get(satelliteNameKey);
            if (bytes[0] == midByte1 && bytes[1] == midByte2) {
                satelliteName = satelliteNameKey;
                break;
            }
        }
        return satelliteName;
    }

    public static String getSatelliteIdBySatelliteName(String satelliteName) {
        String satelliteId = null;
        if (satelliteNameMap.containsKey(satelliteName)) {
            satelliteId = satelliteNameMap.get(satelliteName);
        }
        return satelliteId;
    }

    public static String getSatelliteNameByIcCard(String icCard) {
        String satelliteName = null;
        if (icCardMap.containsKey(icCard)) {
            satelliteName = icCardMap.get(icCard);
        }
        return satelliteName;
    }

    public static String getSatelliteNameBySatelliteId(String satelliteId) {
        String satelliteName = null;
        if (satelliteIdMap.containsKey(satelliteId)) {

            satelliteName = satelliteIdMap.get(satelliteId);

        }
        return satelliteName;

    }


}
