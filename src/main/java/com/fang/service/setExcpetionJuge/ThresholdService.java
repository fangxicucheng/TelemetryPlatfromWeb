package com.fang.service.setExcpetionJuge;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.TypeReference;
import com.fang.database.postgresql.entity.*;
import com.fang.database.postgresql.repository.SatelliteDbRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ThresholdService {
    @Autowired
    private SatelliteDbRepository satelliteDbRepository;
    @Value("${gorit.file.root.path}")
    private String baseDirectoryPath;
    public void refreshThreshold(Integer satelliteId) throws IOException {
        SatelliteDb satelliteDb = satelliteDbRepository.findById(satelliteId).get();
        List<ThresholdInfo> thresholdInfoList = getThresholdInfoList(satelliteDb);
        rebuildThresholdFile(satelliteDb.getSatelliteName(),thresholdInfoList);
    }
    public List<ThresholdInfo> getThresholdInfoListBySatelliteId(Integer satelliteId) throws IOException {
        SatelliteDb satelliteDb = satelliteDbRepository.findById(satelliteId).get();
        return getThresholdInfoList(satelliteDb);
    }
    public List<ThresholdInfo>getThresholdInfoList(SatelliteDb satelliteDb) throws IOException {
        List<ThresholdInfo> thresholdInfoList = new ArrayList<>();
        Map<String, ThresholdInfo> thresholdInfoMap = new HashMap<>();
        for (KeyFrame keyFrame : satelliteDb.getKeyFrameList()) {
            if (keyFrame.getFrameName().equals("重点参数遥测帧1") && keyFrame.getFrameCode() == 1) {
                String[] paraCodeArray = keyFrame.getParaCodes().split(",");
                for (int index = 0; index < paraCodeArray.length; index++) {
                    ThresholdInfo thresholdInfo = new ThresholdInfo();
                    thresholdInfoList.add(thresholdInfo);
                    thresholdInfo.setNum(index + 1);
                    thresholdInfo.setParaCode(paraCodeArray[index]);
                    thresholdInfo.setModified(false);
                    thresholdInfoMap.put(paraCodeArray[index], thresholdInfo);
                }
            }
        }
        List<FrameCatalogDb> frameCatalogDbList = satelliteDb.getFrameCatalogDbList();
        if (frameCatalogDbList != null && frameCatalogDbList.size() > 0) {
            for (FrameCatalogDb frameCatalogDb : frameCatalogDbList) {
                List<FrameDb> frameDbList = frameCatalogDb.getFrameDbList();
                if (frameDbList != null && frameDbList.size() > 0) {
                    for (FrameDb frameDb : frameDbList) {
                        List<ParaConfigLineDb> paraConfigLineDbList = frameDb.getParaConfigLineDbList();
                        if (paraConfigLineDbList != null && paraConfigLineDbList.size() > 0) {
                            for (ParaConfigLineDb paraConfigLineDb : paraConfigLineDbList) {
                                if (thresholdInfoMap.containsKey(paraConfigLineDb.getParaCode())) {
                                    thresholdInfoMap.get(paraConfigLineDb.getParaCode()).setParaName(paraConfigLineDb.getParaName());
                                }
                            }
                        }
                    }
                }
            }
        }
        for (ThresholdInfo thresholdInfo : readThresholdFile(satelliteDb.getSatelliteName())) {
            thresholdInfoMap.put(thresholdInfo.getParaCode(), thresholdInfo);
        }
        for (ThresholdInfo thresholdInfo : thresholdInfoList) {
            if (thresholdInfoMap.containsKey(thresholdInfo.getParaCode())) {
                ThresholdInfo thresholdInfoOld = thresholdInfoMap.get(thresholdInfo.getParaCode());
                thresholdInfo.setThresholdMax(thresholdInfoOld.getThresholdMax());
                thresholdInfo.setThresholdMin(thresholdInfoOld.getThresholdMin());
                thresholdInfo.setCondition(thresholdInfoOld.getCondition());
            }
        }
        return thresholdInfoList;
    }

    public void downLoadThresholdFile(String satelliteName, HttpServletResponse response){

        String fileName=satelliteName + ".json";
        String filePath = baseDirectoryPath + "/" +fileName;
        File file = new File(filePath);
        response.setHeader();

    }
    public List<ThresholdInfo> readThresholdFile(String satelliteName) throws IOException {
        List<ThresholdInfo> thresholdInfoList = new ArrayList<>();
        String filePath = baseDirectoryPath + "/" + satelliteName + ".json";
        File file = new File(filePath);
        String jsonString = FileUtils.readFileToString(file);
        thresholdInfoList.addAll(JSON.parseObject(jsonString, new TypeReference<List<ThresholdInfo>>() {
        }));
        return thresholdInfoList;
    }
    //重新生成
    public void rebuildThresholdFile(String satelliteName, List<ThresholdInfo> thresholdInfoList) throws IOException {
        String filePath = baseDirectoryPath + "/" + satelliteName + ".json";
        String jsonStr = (new JSONArray(thresholdInfoList)).toJSONString();
        File file = new File(filePath);
        FileUtils.writeStringToFile(file, jsonStr);
    }
}
