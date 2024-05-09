package com.fang.service.stationService;

import com.fang.database.postgresql.entity.StationInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReceiveMangerService {
    @Bean(name="receiveManagerMap")
    @Qualifier("receiveManagerMap")
    public Map<String,ReceiveManager> initReceiveManagerMap(@Qualifier("stationInfoList")List<StationInfo> stationInfoList){
        Map<String,ReceiveManager>receiveManagerMap=new HashMap<>();
        return receiveManagerMap;
    }









}
