package com.fang.config.station;

import com.fang.database.postgresql.entity.StationInfo;
import com.fang.database.postgresql.repository.StationInfoRepository;
import com.fang.config.satellite.SatelliteConfig;
import com.fang.utils.NetWorkInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class StationConfig {
    @Autowired
    private StationInfoRepository stationInfoRepository;
    @Autowired
    private SatelliteConfig satelliteService;

    @Bean(name="stationInfoList")
    @Qualifier("stationInfoList")
    @ConditionalOnBean(name = {"satelliteDbMap"})
    public List<StationInfo> getStationInfoList() {
        List<StationInfo> stationInfoList = findStationInfoList();
        List<InetAddress> machineIpAddressList = NetWorkInfoUtils.getMachineIpAddress();
        List<String> localIpList = new ArrayList<>();
        for (InetAddress inetAddress : machineIpAddressList) {
            String hostAddress = inetAddress.getHostAddress();
            localIpList.add(hostAddress);
        }
        List<StationInfo> needStartStationInfoList = new ArrayList<>();
        for (StationInfo stationInfo : stationInfoList) {
            if (localIpList.contains(stationInfo.getServerIp()) && stationInfo.getWaveInfo() != null && !stationInfo.getWaveInfo().isEmpty()) {
                needStartStationInfoList.add(stationInfo);
            }
        }
        if (needStartStationInfoList.size() == 0) {
            for (StationInfo stationInfo : stationInfoList) {
                stationInfo.setServerIp("127.0.0.1");
                needStartStationInfoList.add(stationInfo);
            }
        }
        for (StationInfo stationInfo : needStartStationInfoList) {
            System.out.println(stationInfo);
        }
        return needStartStationInfoList;
    }
    public List<StationInfo> findStationInfoList() {
        return this.stationInfoRepository.findAll();
    }
}
