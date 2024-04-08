package com.fang.controller;

import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.service.setSatelliteConfig.FrameCatalogConfigService;
import com.fang.service.setSatelliteConfig.SatelliteConfigService;
import com.fang.telemetry.satelliteConfigModel.CheckConfigResult;
import com.fang.telemetry.satelliteConfigModel.TeleSatelliteDbModel;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/satellite")
public class SatellIteController {



    @Autowired
    private SatelliteConfigService satelliteConfigService;
    @GetMapping
    public List<TeleSatelliteDbModel> getSatelliteNameInfo() {
        return this.satelliteConfigService.getTeleSatelliteDbModelList();
    }
    @PostMapping("/updateSatelliteInfo")
    public List<TeleSatelliteDbModel> updateSatelliteNameInfo(@RequestBody TeleSatelliteDbModel teleSatelliteDbModel) {
        return this.satelliteConfigService.updateTeleSatelliteDbInfo(teleSatelliteDbModel);
    }
    @DeleteMapping("/deleteSatellite/{id}")
    public List<TeleSatelliteDbModel> deleteSatelliteConfig(@PathVariable int id) {
        return this.satelliteConfigService.deleteTeleSatelliteDbInfoById(id);
    }
    @PostMapping("/upload")
    public SatelliteDb uploadSatelliteConfileFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
        return satelliteConfigService.uploadSatelliteConfileFiles(files);
    }
    @PostMapping("/insertSatellite")
    public CheckConfigResult insertSatelliteDb(@RequestBody SatelliteDb satellite){

        return satelliteConfigService.insertSatellite(satellite);
    }

    @PostMapping("/download")
    public void downLoadSatelliteConfigFile(@RequestBody Integer satellteId, HttpServletResponse reponse){

        this.satelliteConfigService.downloadSatelliteConfigFile(satellteId,reponse);

    }


}
