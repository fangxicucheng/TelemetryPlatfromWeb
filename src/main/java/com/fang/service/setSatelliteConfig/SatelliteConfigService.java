package com.fang.service.setSatelliteConfig;

import com.fang.database.mysql.entity.TeleSatelliteNameMq;
import com.fang.database.mysql.repository.TeleSatelliteNameMqRepository;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.database.postgresql.repository.SatelliteDbRepository;
import com.fang.service.parseTelemetry.BaseParserService;
import com.fang.service.setSatelliteConfig.readFile.ManageSatelliteConfigFileService;
import com.fang.telemetry.satelliteConfigModel.CheckConfigResult;
import com.fang.telemetry.satelliteConfigModel.TeleSatelliteDbModel;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SatelliteConfigService {

    @Autowired
    private TeleSatelliteNameMqRepository satelliteNameMqRepository;
    @Autowired
    private SatelliteDbRepository satelliteDbRepository;
    @Autowired
    ManageSatelliteConfigFileService manageSatelliteConfigFileService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Value("${gorit.file.root.path}")
    private String baseDirectoryPath;
    @Autowired
    private BaseParserService baseParserService;

    public void copySatelliteData() {
        List<TeleSatelliteNameMq> satelliteMqList = this.satelliteNameMqRepository.findAll();
        List<SatelliteDb> satelliteDbList = new ArrayList<>();
        for (TeleSatelliteNameMq teleSatelliteNameMq : satelliteMqList) {
            SatelliteDb satelliteDb = new SatelliteDb(teleSatelliteNameMq);
            satelliteDbList.add(satelliteDb);
        }
        this.satelliteDbRepository.saveAll(satelliteDbList);

    }

    public SatelliteDb uploadSatelliteConfileFiles(List<MultipartFile> files) throws IOException {
        SatelliteDb satelliteDb;
        String format = sdf.format(new Date());
        Path directoryPath = Paths.get(baseDirectoryPath + "上传文件\\" + format + "\\");
        List<File> destFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            System.out.println(fileName);
            File dest = new File(directoryPath + "\\" + fileName);
            System.out.println(fileName);
            dest.getParentFile().mkdirs();
            file.transferTo(dest);
            destFiles.add(dest);
        }
        satelliteDb = manageSatelliteConfigFileService.readSatelliteDbConfigFile(destFiles);

        System.out.println("文件保存完成");
        FileUtils.deleteDirectory(new File(baseDirectoryPath + "上传文件\\" + format + "\\"));
        return satelliteDb;
    }


    public List<TeleSatelliteDbModel> getTeleSatelliteDbModelList() {
        return this.satelliteDbRepository.querySatelliteName();
    }

    public List<TeleSatelliteDbModel> updateTeleSatelliteDbInfo(TeleSatelliteDbModel satelliteDbModel) {
        this.satelliteDbRepository.updateSatelliteDbInfo(satelliteDbModel.getId(), satelliteDbModel.getSatelliteName());
        return getTeleSatelliteDbModelList();

    }

    public List<TeleSatelliteDbModel> deleteTeleSatelliteDbInfoById(int id) {
        this.satelliteDbRepository.deleteById(id);
        return getTeleSatelliteDbModelList();
    }

    public CheckConfigResult insertSatellite(SatelliteDb satelliteDb) {
        CheckConfigResult result = new CheckConfigResult();
        if (satelliteDb.getSatelliteName() == null || satelliteDb.getSatelliteName().isEmpty()) {
            result.setErrorMsg("未设置卫星名");
        }
        List<String> satelliteNameList = satelliteDbRepository.getSatelliteNameList();
        if (satelliteNameList.contains(satelliteDb.getSatelliteName())) {
            result.setErrorMsg("卫星名重复");
        }
        baseParserService.validateSatelliteDb(satelliteDb, result);
        if(!result.isHasWrong()){
            satelliteDbRepository.save(satelliteDb);
        }

        return result;
    }
}
