package com.fang.controller;

import com.fang.database.postgresql.entity.SatelliteSubsystem;
import com.fang.database.postgresql.repository.SatelliteSubsystemRepository;
import com.fang.service.setExcpetionJuge.SubSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/subSystem")
public class SubSystemController {

    @Autowired
    private SubSystemService subSystemService;

    @PostMapping("/upload")
    public List<SatelliteSubsystem> uploadSubsystemFile(@RequestParam MultipartFile file) throws IOException {
        return this.subSystemService.uploadSatelliteSubSystemList(file);
    }

    @GetMapping("/getSubSystem/{satelliteName}")
    public List<SatelliteSubsystem> getSatelliteSubSystemList(@PathVariable String satelliteName) {
        return this.subSystemService.getSatelliteSubSystemList(satelliteName);

    }

    @PostMapping("/update/{satelliteName}")
    public void updateSatelliteSubSystemList(@PathVariable String satelliteName,@RequestBody List<SatelliteSubsystem> satelliteSubsystemList) {
        this.subSystemService.updateSatelliteSubSystemList(satelliteName,satelliteSubsystemList);
    }
}
