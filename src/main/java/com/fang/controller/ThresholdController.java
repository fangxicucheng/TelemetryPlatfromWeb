package com.fang.controller;

import com.fang.service.setExcpetionJuge.ThresholdInfo;
import com.fang.service.setExcpetionJuge.ThresholdService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/threshold")
public class ThresholdController {

    @Autowired
    private ThresholdService thresholdService;
    @PostMapping("/refresh/{satelliteId}")
    public void refreshThreshold(@PathVariable Integer satelliteId) throws IOException {
        thresholdService.refreshThreshold(satelliteId);
    }

    @PostMapping("/getThresholdList/{satelliteId}")
    public List<ThresholdInfo> readThresholdFromFile(@PathVariable Integer satelliteId) throws IOException {
        return this.thresholdService.getThresholdInfoListBySatelliteId(satelliteId);
    }
    @PostMapping("/update/{satelliteName}")
    public void updateSatelliteThresholdFile(@PathVariable String satelliteName,@RequestBody List<ThresholdInfo>thresholdInfoList) throws IOException {
        this.thresholdService.rebuildThresholdFile(satelliteName,thresholdInfoList);
    }

    @PostMapping("/download")
    public void downLoadThresholdFile(@RequestBody String satelliteName, HttpServletResponse response) throws IOException {
            this.thresholdService.downLoadThresholdFile(satelliteName.replaceAll("\"",""),response);

    }

}
