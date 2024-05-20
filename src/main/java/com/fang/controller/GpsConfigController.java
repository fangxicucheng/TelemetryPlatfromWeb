package com.fang.controller;

import com.fang.database.postgresql.entity.GpsParaConfig;
import com.fang.service.gpsService.GPSConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gpsConfig")
public class GpsConfigController {
    @Autowired
    private GPSConfigService gpsConfigService;

    @GetMapping("/getAllData")
    public List<GpsParaConfig> getGpsParaConfigList() {
        return this.gpsConfigService.getGpsParaConfigList();
    }

    @PostMapping("/saveOrUpdate")
    public List<GpsParaConfig> saveOrUpdateGpsParaConfig(@RequestBody GpsParaConfig gpsParaConfig) {
        this.gpsConfigService.saveOrUpdateGpsParaConfig(gpsParaConfig);
        return this.gpsConfigService.getGpsParaConfigList();
    }

    @PostMapping("/saveOrUpdateList")
    public List<GpsParaConfig> saveOrUpdateGpsParaConfigList(@RequestBody List<GpsParaConfig> gpsParaConfigList) {
        this.gpsConfigService.saveOrUpdateGpsParaConfigList(gpsParaConfigList);
    return this.gpsConfigService.getGpsParaConfigList();
    }

    @DeleteMapping("/delete/{id}")
    public List<GpsParaConfig>deleteById(@PathVariable Integer id){
        this.gpsConfigService.deleteGpsParaConfig(id);
        return this.gpsConfigService.getGpsParaConfigList();
    }

}
