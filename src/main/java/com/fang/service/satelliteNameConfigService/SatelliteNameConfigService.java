package com.fang.service.satelliteNameConfigService;

import com.fang.database.postgresql.entity.SatelliteNameConfig;
import com.fang.database.postgresql.repository.SatelliteNameConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SatelliteNameConfigService {
    @Autowired
    SatelliteNameConfigRepository satelliteNameConfigRepository;


    public List<SatelliteNameConfig> getSatelliteNameConfigList(){
        return this.satelliteNameConfigRepository.findAll();
    }

    public void saveOrUpdateSatelliteNameConfig(SatelliteNameConfig satelliteNameConfig){
        this.satelliteNameConfigRepository.save(satelliteNameConfig);
    }
    public void saveOrUpdateSatelliteNameConfigList(List<SatelliteNameConfig>satelliteNameConfigList){
        this.satelliteNameConfigRepository.saveAll(satelliteNameConfigList);

    }
    public void deleteSatelliteNameConfig(int id){
        this.satelliteNameConfigRepository.deleteById(id);
    }

}
