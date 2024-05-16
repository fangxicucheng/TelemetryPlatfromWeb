package com.fang.service.gpsConfigService;

import com.fang.database.postgresql.entity.GpsParaConfig;
import com.fang.database.postgresql.repository.GpsParaConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GPSConfigService {
    @Autowired
    private GpsParaConfigRepository gpsParaConfigDao;

    public List<GpsParaConfig> getGpsParaConfig() {
        return this.gpsParaConfigDao.findAll();
    }

    public GpsParaConfig getGpsParaConfigBySatelliteName(String satelliteName) {
        return this.gpsParaConfigDao.getGpsParaConfigBySatelliteName(satelliteName);
    }

    public void saveOrUpdateGpsParaConfig(GpsParaConfig gpsParaConfig) {
        this.gpsParaConfigDao.save(gpsParaConfig);
    }
    public void saveOrUpdateGpsParaConfigList(List<GpsParaConfig>gpsParaConfigList){
        this.gpsParaConfigDao.saveAll(gpsParaConfigList);
    }

}