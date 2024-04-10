package com.fang.service.setExcpetionJuge;

import com.fang.database.mysql.entity.TeleSubsystemMq;
import com.fang.database.mysql.repository.TeleSubsystemMqRepository;
import com.fang.database.postgresql.entity.FrameDb;
import com.fang.database.postgresql.entity.SatelliteSubsystem;
import com.fang.database.postgresql.repository.SatelliteSubsystemRepository;
import com.fang.utils.ExcelUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SubSystemService {
    @Autowired
    private SatelliteSubsystemRepository satelliteSubsystemRepository;
    @Autowired
    private TeleSubsystemMqRepository teleSubsystemMqRepository;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Value("${gorit.file.root.path}")
    private String baseDirectoryPath;

    public void copySatelliteSubsystemFromMysql() {
        this.satelliteSubsystemRepository.deleteAll();
        List<TeleSubsystemMq> teleSubsystemMqList = teleSubsystemMqRepository.findAll();
        for (TeleSubsystemMq teleSubsystemMq : teleSubsystemMqList) {
            this.satelliteSubsystemRepository.save(new SatelliteSubsystem(teleSubsystemMq));
        }
    }

    public List<SatelliteSubsystem> getSatelliteSubSystemList(String satelliteName) {
        return this.satelliteSubsystemRepository.findBySatelliteName(satelliteName);
    }

    public List<SatelliteSubsystem> uploadSatelliteSubSystemList(MultipartFile file) throws IOException {
        List<SatelliteSubsystem> satelliteSubsystemList = new ArrayList<>();
        String format = sdf.format(new Date());
        Path directoryPath = Paths.get(baseDirectoryPath + "上传文件\\" + format + "\\");
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        File dest = new File(directoryPath + "\\" + fileName);
        System.out.println(fileName);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);
        List<Object[]> objects = ExcelUtils.importExcel(new FileInputStream(dest)).get(1);
        for (int i = 1; i < objects.size(); i++) {
            Object[] objectArray = objects.get(i);
            SatelliteSubsystem satelliteSubsystem=new SatelliteSubsystem();
            satelliteSubsystem.setSubsystemName(objectArray[1].toString());
            satelliteSubsystem.setSubsystemChargerContact(objectArray[2].toString());
            satelliteSubsystemList.add(satelliteSubsystem);
        }
        System.out.println("文件保存完成");
        FileUtils.deleteDirectory(new File(baseDirectoryPath + "上传文件\\" + format + "\\"));
        return satelliteSubsystemList;

    }

    public void updateSatelliteSubSystemList(String satelliteName,List<SatelliteSubsystem> satelliteSubsystemList) {
        this.satelliteSubsystemRepository.deleteBySatelliteName(satelliteName);
        this.satelliteSubsystemRepository.saveAll(satelliteSubsystemList);
    }

}
