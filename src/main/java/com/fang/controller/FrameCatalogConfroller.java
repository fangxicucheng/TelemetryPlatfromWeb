package com.fang.controller;

import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.database.postgresql.repository.FrameCatalogDbRepository;
import com.fang.service.setSatelliteConfig.FrameCatalogConfigService;
import com.fang.telemetry.satelliteConfigModel.CheckConfigResult;
import com.fang.telemetry.satelliteConfigModel.TeleFrameCatalogDbModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/frameCatalog")
public class FrameCatalogConfroller {
    @Autowired
    private FrameCatalogConfigService frameCatalogConfigService;
    @GetMapping("/{satelliteId}")
    public List<TeleFrameCatalogDbModel> getFrameCatalogInfo(@PathVariable Integer satelliteId) {
        return this.frameCatalogConfigService.getFrameCatalogInfo(satelliteId);
    }
    @PostMapping("/upload")
    public FrameCatalogDb uploadSatelliteConfileFiles(@RequestParam("files") List<MultipartFile> files){

    }
    public CheckConfigResult

    @PostMapping("/{satelliteId}/updateCatalog")
    public List<TeleFrameCatalogDbModel> updateFrameCatalogInfo(@PathVariable Integer satelliteId,@RequestBody TeleFrameCatalogDbModel teleFrameCatalogDbModel){
        List<TeleFrameCatalogDbModel> models = this.frameCatalogConfigService.updateFrameCatalogById(satelliteId, teleFrameCatalogDbModel);
        return models;
    }
    @DeleteMapping("/{satelliteId}/{id}")
    public List<TeleFrameCatalogDbModel> deleteFrameCatalogById(@PathVariable Integer satelliteId,@PathVariable Integer id){
        return this.frameCatalogConfigService.deleteFrameCatlogById(satelliteId,id);

    }
}
