package com.fang.controller;

import com.fang.config.satellite.SatelliteModelConfig;
import com.fang.telemetry.TeleSatelliteNameModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/satelliteNameModel")

public class SatelliteNameModelController {
    @GetMapping("/{satelliteName}")
    public TeleSatelliteNameModel getSatelliteNameModel(@PathVariable String satelliteName) {
        return SatelliteModelConfig.getSatelliteNameModel(satelliteName);
    }
}
