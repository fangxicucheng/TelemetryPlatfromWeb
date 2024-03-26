package com.fang.controller;

import com.fang.database.postgresql.repository.SatelliteDbRepository;
import com.fang.service.setSatelliteConfig.SatelliteConfigService;
import com.fang.telemetry.satelliteConfigModel.TeleSatelliteDbModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/satellite")
public class SatellIteController {


    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    @Value("${gorit.file.root.path}")
    private String baseDirectoryPath;
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
    public String uploadSatelliteConfileFiles(@RequestParam("files") List<MultipartFile> files) {
        String format=sdf.format(new Date());
        for (MultipartFile file : files) {


            String fileName = file.getOriginalFilename();
            System.out.println(fileName);
            Path directoryPath=Paths.get(baseDirectoryPath+"上传文件\\"+format+"\\");
            File dest=new File(directoryPath+"\\"+fileName);
            try{
                if(!Files.exists(directoryPath)){
                    Files.createDirectories(directoryPath);
                }
                file.transferTo(dest);
            }
            catch (Exception e){
                e.printStackTrace();
            }



        }
        return "success";
    }


}
