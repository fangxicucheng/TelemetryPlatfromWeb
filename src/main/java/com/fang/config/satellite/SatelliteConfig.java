package com.fang.config.satellite;

import com.fang.config.exception.ExceptionManager;
import com.fang.config.exception.satelliteExcetionManager.BaseExceptionManager;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.database.postgresql.repository.SatelliteDbRepository;
import com.fang.service.setExcpetionJuge.ThresholdInfo;
import com.fang.utils.ConfigUtils;
import com.fang.utils.StringConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class SatelliteConfig {

    @Autowired
    private SatelliteDbRepository satelliteDbRepository;
    @Autowired
    @Qualifier("threadInfoListMap")
    private Map<String, List<ThresholdInfo>> thresholdInfoMap;
    //key:satelliteName,value satelliteNameBytes
//    @Bean(name="satelliteNameBytesMap")
//    @Qualifier("satelliteNameBytesMap")
//    public Map<String, byte[]> getSatelliteNameBytesMap(){
//        System.out.println("初始化1");
//        return new HashMap<>();
//    }
//    //key:satelliteName, value:satelliteId
//
//    @Bean(name="satelliteNameMap")
//    @Qualifier("satelliteNameMap")
//    public Map<String,String> getSatelliteNameMap(){
//        System.out.println("初始化2");
//        return new HashMap<>();
//    }
//    //Map
//    //key:ICardCode value:satelliteName
//    @Bean(name="bdICardMap")
//    @Qualifier("bdICardMap")
//    public Map<String,String> getBdICardMap(){
//        System.out.println("初始化3");
//        return  new HashMap();
//    }
//    @Bean(name="satelliteDbMap")
//    @Qualifier("satelliteDbMap")
//    Map<String, SatelliteDb> getSatelliteDbMap(@Qualifier("satelliteNameBytesMap") Map<String,byte[]>satelliteNameBytesMap,
//                                              @Qualifier("satelliteNameMap") Map<String,String> satelliteNameMap,
//                                              @Qualifier("bdICardMap") Map<String,String>bdICardMap) {
//        System.out.println("开始初始化");
//        Map<String, SatelliteDb> satelliteDbMap = new HashMap<>();
//        for (SatelliteDb satelliteDb : satelliteDbRepository.findAll()) {
//            try{
//                if (satelliteDb.getSatelliteId() == null) {
//                    continue;
//                }
//                satelliteDbMap.put(satelliteDb.getSatelliteName(), satelliteDb);
//                initSatelliteInfo(satelliteDb,satelliteNameBytesMap,satelliteNameMap,bdICardMap);
//                System.out.println(satelliteDb.getSatelliteName() + "初始化结束");
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        System.out.println("结束了");
//        return satelliteDbMap;
//    }


    @Bean(name = "satelliteDbMap")
    @Qualifier("satelliteDbMap")
//    @ConditionalOnBean(name="threadInfoListMap")
    Map<String, SatelliteDb> getSatelliteDbMap() {
        System.out.println("开始初始化");
        Map<String, SatelliteDb> satelliteDbMap = new HashMap<>();
        for (SatelliteDb satelliteDb : satelliteDbRepository.findAll()) {
            try {
                if (satelliteDb.getSatelliteId() == null) {
                    continue;
                }
                satelliteDbMap.put(satelliteDb.getSatelliteName(), satelliteDb);
                initSatelliteInfo(satelliteDb);
                System.out.println(satelliteDb.getSatelliteName() + "初始化结束");


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("结束了");
        return satelliteDbMap;
    }


    private void initSatelliteInfo(SatelliteDb satelliteDb) {
        setSatelliteName(satelliteDb);
        setSatelliteNameBytes(satelliteDb);
        setBdICard(satelliteDb);
        setSatelliteParaParser(satelliteDb);
    }


    private void setSatelliteName(SatelliteDb satelliteDb) {
        ConfigUtils.setSatelliteName(satelliteDb.getSatelliteName(), satelliteDb.getSatelliteId());
    }

    private void setSatelliteParaParser( SatelliteDb satelliteDb) {
        String satelliteName = satelliteDb.getSatelliteName();
        List<ThresholdInfo> thresholdInfoList = null;
        if (this.thresholdInfoMap.containsKey(satelliteName)) {
            thresholdInfoList = this.thresholdInfoMap.get(satelliteName);
        }
        ConfigUtils.setSatelliteParaParser(satelliteDb, thresholdInfoList);
    }


    private void setSatelliteNameBytes(SatelliteDb satelliteDb) {


        if (satelliteDb.getSatelliteBytesStr() == null || satelliteDb.getSatelliteBytesStr().isEmpty()) {
            return;
        }
        ConfigUtils.setMid(satelliteDb.getSatelliteName(), StringConvertUtils.hexStringToByteArray(satelliteDb.getSatelliteBytesStr()));
    }

    private void setBdICard(SatelliteDb satelliteDb) {
        if (satelliteDb.getBdICCardsStr() == null || satelliteDb.getBdICCardsStr().isEmpty()) {
            return;
        }
        for (String bdICard : satelliteDb.getBdICCardsStr().split(",")) {
            //bdICardMap.put(bdICard,satelliteDb.getSatelliteName());
            ConfigUtils.setIcCard(satelliteDb.getSatelliteName(), bdICard);
        }
    }
//    private void initSatelliteInfo(SatelliteDb satelliteDb,Map<String,byte[]>satelliteNameBytesMap,Map<String,String> satelliteNameMap,Map<String,String>bdICardMap) {
//        setSatelliteName(satelliteDb,satelliteNameMap);
//        setSatelliteNameBytes(satelliteDb,satelliteNameBytesMap);
//        setBdICard(satelliteDb,bdICardMap);
//    }
//    private void setSatelliteName(SatelliteDb satelliteDb,Map<String,String> satelliteNameMap) {
//        if (satelliteNameMap == null) {
//            satelliteNameMap = new HashMap<>();
//        }
//        satelliteNameMap.put(satelliteDb.getSatelliteName(), satelliteDb.getSatelliteId());
//    }
//    private void setSatelliteNameBytes(SatelliteDb satelliteDb,Map<String,byte[]>satelliteNameBytesMap) {
//
//        if (satelliteNameBytesMap == null) {
//            satelliteNameBytesMap = new HashMap<>();
//        }
//        if (satelliteDb.getSatelliteBytesStr() == null || satelliteDb.getSatelliteBytesStr().isEmpty()) {
//            return;
//        }
//        satelliteNameBytesMap.put(satelliteDb.getSatelliteName(), NumberConvertUtils.hexStringToByteArray(satelliteDb.getSatelliteBytesStr()));
//    }
//    private void setBdICard(SatelliteDb satelliteDb,Map<String,String>bdICardMap) {
//
//        if(bdICardMap==null){
//            bdICardMap=new HashMap<>();
//        }
//        if(satelliteDb.getBdICCardsStr()==null||satelliteDb.getBdICCardsStr().isEmpty()){
//            return;
//        }
//        for (String bdICard : satelliteDb.getBdICCardsStr().split(",")) {
//            bdICardMap.put(bdICard,satelliteDb.getSatelliteName());
//        }
//    }
}
