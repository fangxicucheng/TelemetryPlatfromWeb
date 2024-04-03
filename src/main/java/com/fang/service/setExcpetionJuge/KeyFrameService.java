package com.fang.service.setExcpetionJuge;

import com.fang.database.postgresql.entity.*;
import com.fang.database.postgresql.repository.KeyFrameRepository;
import com.fang.database.postgresql.repository.SatelliteDbRepository;
import com.fang.utils.ExcelReader;
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
import java.util.*;

@Service
public class KeyFrameService {

    @Autowired
    private KeyFrameRepository keyFrameRepository;
    @Autowired
    private SatelliteDbRepository satelliteDbRepository;


    public List<ParaConfigLineExceptionInfo> uploadKeyFrameFile(Integer satelliteId, Integer keyFrameId, MultipartFile file) throws IOException {
        SatelliteDb satelliteDb = this.satelliteDbRepository.findById(satelliteId).get();
        List<ParaConfigLineExceptionInfo> paraConfigLineExceptionInfoList = readExcelFile(file,0);
        Map<String, ParaConfigLineExceptionInfo> paraCodeMap = new HashMap<>();
        for (ParaConfigLineExceptionInfo paraConfigLineExceptionInfo : paraConfigLineExceptionInfoList) {
            paraCodeMap.put(paraConfigLineExceptionInfo.getParaCode(), paraConfigLineExceptionInfo);
        }
        if (satelliteDb.getFrameCatalogDbList() != null && satelliteDb.getFrameCatalogDbList().size() > 0) {
            for (FrameCatalogDb frameCatalogDb : satelliteDb.getFrameCatalogDbList()) {
                if (frameCatalogDb.getFrameDbList() != null && frameCatalogDb.getFrameDbList().size() > 0) {
                    for (FrameDb frameDb : frameCatalogDb.getFrameDbList()) {
                        if (frameDb.getParaConfigLineDbList() != null && frameDb.getParaConfigLineDbList().size() > 0) {
                            for (ParaConfigLineDb paraConfigLineDb : frameDb.getParaConfigLineDbList()) {
                                if (paraCodeMap.containsKey(paraConfigLineDb.getParaCode())) {
                                    ParaConfigLineExceptionInfo paraConfigLineExceptionInfo = paraCodeMap.get(paraConfigLineDb.getParaCode());
                                    paraConfigLineExceptionInfo.setId(paraConfigLineDb.getId());
                                    paraConfigLineExceptionInfo.setParaName(paraConfigLineDb.getParaName());
                                }
                            }
                        }
                    }
                }
            }
        }
        return paraConfigLineExceptionInfoList;
    }


    public List<ParaConfigLineExceptionInfo> readExcelFile(MultipartFile file,Integer index) throws IOException {
        List<ParaConfigLineExceptionInfo> paraConfigLineExceptionInfoList = new ArrayList<>();
        List<Object[]> readContentList = ExcelReader.readSingleExcelFile(file).get(index);
        for (Object[] readContentArray : readContentList) {
            ParaConfigLineExceptionInfo paraConfigLineExceptionInfo = new ParaConfigLineExceptionInfo();
            paraConfigLineExceptionInfo.setParaNameRead(readContentArray[1].toString());
            paraConfigLineExceptionInfo.setParaCode(readContentArray[2].toString());
            paraConfigLineExceptionInfo.setExp(readContentArray[3].toString());
            paraConfigLineExceptionInfo.setSubSystem(readContentArray[4].toString());
            paraConfigLineExceptionInfo.setParaName("未知");
            paraConfigLineExceptionInfo.setId(-1);
            paraConfigLineExceptionInfoList.add(paraConfigLineExceptionInfo);
        }
        return paraConfigLineExceptionInfoList;
    }

    public List<KeyFrame> getKeyFrameListBySatelliteId(Integer satelliteId) {
        return this.keyFrameRepository.getKeyFramesBySatelliteId(satelliteId);
    }

    public List<ParaConfigLineExceptionInfo> getKeyFrameParaConfigleLine(Integer satelliteId, Integer keyFrameId) {
        KeyFrame keyFrame = this.keyFrameRepository.findById(keyFrameId).get();
        String[] paraCodeList = keyFrame.getParaCodes().split(",");
        Map<String, ParaConfigLineExceptionInfo> paraConfigLineExceptionInfoMap = new HashMap<>();
        if (paraCodeList.length >= 1) {
            for (String paraCode : paraCodeList) {
                paraConfigLineExceptionInfoMap.put(paraCode, new ParaConfigLineExceptionInfo());
            }
        }
        List<ParaConfigLineExceptionInfo> paraConfigLineExceptionInfoList = new ArrayList<>();

        SatelliteDb satelliteDb = satelliteDbRepository.findById(satelliteId).get();
        if (satelliteDb.getFrameCatalogDbList() != null && satelliteDb.getFrameCatalogDbList().size() > 0) {
            for (FrameCatalogDb frameCatalogDb : satelliteDb.getFrameCatalogDbList()) {
                if (frameCatalogDb.getFrameDbList() != null && frameCatalogDb.getFrameDbList().size() > 0) {
                    for (FrameDb frameDb : frameCatalogDb.getFrameDbList()) {
                        if (frameDb.getParaConfigLineDbList() != null && frameDb.getParaConfigLineDbList().size() > 0) {
                            for (ParaConfigLineDb paraConfigLineDb : frameDb.getParaConfigLineDbList()) {
                                if (paraConfigLineExceptionInfoMap.containsKey(paraConfigLineDb.getParaCode())) {
                                    ParaConfigLineExceptionInfo paraConfigLineExceptionInfo = paraConfigLineExceptionInfoMap.get(paraConfigLineDb.getParaCode());
                                    paraConfigLineExceptionInfo.setParaName(paraConfigLineDb.getParaName());
                                    paraConfigLineExceptionInfo.setParaNameRead(paraConfigLineDb.getParaName());
                                    paraConfigLineExceptionInfo.setParaCode(paraConfigLineDb.getParaCode());
                                    paraConfigLineExceptionInfo.setId(paraConfigLineDb.getId());
                                    paraConfigLineExceptionInfo.setExp(paraConfigLineDb.getExp());
                                    paraConfigLineExceptionInfo.setSubSystem(paraConfigLineDb.getSubSystem());
                                }

                            }
                        }

                    }
                }

            }
        }
        for (String paraCode : paraCodeList) {

            paraConfigLineExceptionInfoList.add(paraConfigLineExceptionInfoMap.get(paraCode));
        }
        return paraConfigLineExceptionInfoList;


    }
}
