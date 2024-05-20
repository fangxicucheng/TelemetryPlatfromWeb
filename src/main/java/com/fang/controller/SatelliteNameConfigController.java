package com.fang.controller;

import com.fang.database.postgresql.entity.SatelliteNameConfig;
import com.fang.database.postgresql.repository.SatelliteNameConfigRepository;
import com.fang.service.satelliteNameConfigService.SatelliteNameConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/satelliteNameConfig")
public class SatelliteNameConfigController {

    @Autowired
    private SatelliteNameConfigService satelliteNameConfigService;
    @GetMapping("/getAllData")
    public List<SatelliteNameConfig> getSatelliteNameConfigList(){
        return this.satelliteNameConfigService.getSatelliteNameConfigList();
    }
    @PostMapping("/saveOrUpdate")
    public List<SatelliteNameConfig> updateSatelliteNameConfig(@RequestBody SatelliteNameConfig satelliteNameConfig){
        this.satelliteNameConfigService.saveOrUpdateSatelliteNameConfig(satelliteNameConfig);
        return this.satelliteNameConfigService.getSatelliteNameConfigList();
    }
    @PostMapping("/saveOrUpdateList")
    public List<SatelliteNameConfig> updateSatelliteNameConfigList(@RequestBody List<SatelliteNameConfig> satelliteNameConfigList){
        this.satelliteNameConfigService.saveOrUpdateSatelliteNameConfigList(satelliteNameConfigList);
        return this.satelliteNameConfigService.getSatelliteNameConfigList();
    }
    @DeleteMapping("/delete/{id}")
    public List<SatelliteNameConfig> deleteSatelliteNameConfigById(@PathVariable Integer id){
        this.satelliteNameConfigService.deleteSatelliteNameConfig(id);
        return this.satelliteNameConfigService.getSatelliteNameConfigList();
    }
}
