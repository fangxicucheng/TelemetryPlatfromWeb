package com.fang.controller;

import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.database.postgresql.entity.FrameDb;
import com.fang.service.setSatelliteConfig.FrameConfigService;
import com.fang.telemetry.satelliteConfigModel.CheckConfigResult;
import com.fang.telemetry.satelliteConfigModel.TeleFrameDbModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/frame")
public class FrameController {
    @Autowired
    private FrameConfigService frameConfigService;
    @GetMapping("/{catalogId}")
    public List<TeleFrameDbModel>getFrameListByCatalogId(@PathVariable Integer catalogId){

     return this.frameConfigService.getFrameDbModelList(catalogId);
    }

    @PostMapping("/{catalogId}/updateFrame")
    public List<TeleFrameDbModel>updateFrameByFrameInfo(@PathVariable Integer catalogId,@RequestBody TeleFrameDbModel teleFrameDbModel){

        this.frameConfigService.updateFrameByFrameInfo(teleFrameDbModel);

        return this.frameConfigService.getFrameDbModelList(catalogId);
    }

    @PostMapping("/upload")
    public FrameDb uploadFrameConfigFiles(@RequestParam("file") MultipartFile file) throws IOException {
        return this.frameConfigService.uploadFrameConfigFile(file);
    }


    @PostMapping("/insertFrame/{catalogId}")
    public CheckConfigResult insertFrameDb(@PathVariable int catalogId, @RequestBody FrameDb frameDb){

        return this.frameConfigService.insertFrameCatalog(catalogId,frameDb);
    }

    @DeleteMapping("/{catalogId}/{frameId}")
    public List<TeleFrameDbModel>deleteFrameById(@PathVariable Integer catalogId,@PathVariable Integer frameId){
        this.frameConfigService.deleteFrmeById(frameId);
        return this.frameConfigService.getFrameDbModelList(catalogId);
    }
}
