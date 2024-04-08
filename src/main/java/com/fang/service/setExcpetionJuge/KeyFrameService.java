package com.fang.service.setExcpetionJuge;

import com.fang.database.postgresql.entity.*;
import com.fang.database.postgresql.repository.KeyFrameRepository;
import com.fang.database.postgresql.repository.SatelliteDbRepository;
import com.fang.database.postgresql.repository.SatelliteSubsystemRepository;
import com.fang.telemetry.satelliteConfigModel.CheckConfigResult;
import com.fang.utils.ExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class KeyFrameService {

    @Autowired
    private KeyFrameRepository keyFrameRepository;
    @Autowired
    private SatelliteDbRepository satelliteDbRepository;
    @Autowired
    private SatelliteSubsystemRepository subsystemRepository;

    public CheckConfigResult updateKeyFrame(KeyFrameExceptionInfo keyFrameExceptionInfo, String action) {
        CheckConfigResult checkConfigResult = new CheckConfigResult();
        SatelliteDb satelliteDb = satelliteDbRepository.findById(keyFrameExceptionInfo.getSatelliteId()).get();
        Map<String, ParaConfigLineExceptionInfo> paraConfigLineExceptionInfoMap = new HashMap<>();
        for (int i = 0; i < keyFrameExceptionInfo.getParaConfigLineExceptionInfoList().size(); i++) {

            ParaConfigLineExceptionInfo paraConfigLineExceptionInfo = keyFrameExceptionInfo.getParaConfigLineExceptionInfoList().get(i);

            paraConfigLineExceptionInfoMap.put(paraConfigLineExceptionInfo.getParaCode(), paraConfigLineExceptionInfo);
            if (paraConfigLineExceptionInfo.getParaName().equals("未知")) {
                checkConfigResult.setErrorMsg("第" + (i + 1) + "行 " + paraConfigLineExceptionInfo.getParaCode() + "为未知参数名");

            }
            if (paraConfigLineExceptionInfo.getParaCode().isEmpty()) {
                checkConfigResult.setErrorMsg("第" + (i + 1) + "行 未设置参数代号");
            }
        }
        if (keyFrameExceptionInfo.getKeyFrameCode() == 1 && keyFrameExceptionInfo.getKeyFrameName().equals("重点参数遥测帧1")) {
            if (satelliteDb.getFrameCatalogDbList() != null) {
                for (FrameCatalogDb frameCatalogDb : satelliteDb.getFrameCatalogDbList()) {
                    if (frameCatalogDb.getFrameDbList() != null) {
                        for (FrameDb frameDb : frameCatalogDb.getFrameDbList()) {
                            for (ParaConfigLineDb paraConfigLineDb : frameDb.getParaConfigLineDbList()) {
                                if (paraConfigLineExceptionInfoMap.containsKey(paraConfigLineDb.getParaCode())) {
                                    paraConfigLineDb.setExp(paraConfigLineExceptionInfoMap.get(paraConfigLineDb.getParaCode()).getExp());
                                    paraConfigLineDb.setSubSystem(paraConfigLineExceptionInfoMap.get(paraConfigLineDb.getParaCode()).getSubSystem());
                                }
                            }
                        }
                    }

                }
            }
        }
        StringJoiner joiner = new StringJoiner(",");
        for (ParaConfigLineExceptionInfo paraConfigLineExceptionInfo : keyFrameExceptionInfo.getParaConfigLineExceptionInfoList()) {
            joiner.add(paraConfigLineExceptionInfo.getParaCode());
        }
        for (KeyFrame keyFrame : satelliteDb.getKeyFrameList()) {
            if (action.equals("modify") && keyFrame.getId() == keyFrameExceptionInfo.getKeyFrameId()) {

                keyFrame.setFrameName(keyFrameExceptionInfo.getKeyFrameName());
                keyFrame.setFrameCode(keyFrameExceptionInfo.getKeyFrameCode());
                keyFrame.setParaCodes(joiner.toString());
            } else {
                if (keyFrame.getFrameCode() == keyFrameExceptionInfo.getKeyFrameCode()) {
                    checkConfigResult.setErrorMsg("存在同名的重点参数帧");
                }
                if (keyFrame.getFrameName().equals(keyFrameExceptionInfo.getKeyFrameName())) {
                    checkConfigResult.setErrorMsg("重点帧编号重复");
                }
            }


        }
        if (!checkConfigResult.isHasWrong()) {
            if (action.equals("add")) {
                KeyFrame keyFrame = new KeyFrame();
                keyFrame.setFrameCode(keyFrameExceptionInfo.getKeyFrameCode());
                keyFrame.setFrameName(keyFrameExceptionInfo.getKeyFrameName());
                keyFrame.setParaCodes(joiner.toString());
                satelliteDb.getKeyFrameList().add(keyFrame);
            }
            satelliteDb.getKeyFrameList().sort((o1, o2) -> {
                return o1.getFrameCode() - o2.getFrameCode();
            });
            for (int i = 0; i < satelliteDb.getKeyFrameList().size(); i++) {
                satelliteDb.getKeyFrameList().get(i).setNum(i + 1);
            }
            satelliteDbRepository.save(satelliteDb);
        }


        return checkConfigResult;
    }

    public CheckConfigResult deleteKeyFrameByFrameId(Integer id) {
        CheckConfigResult checkConfigResult = new CheckConfigResult();
        keyFrameRepository.deleteById(id);
        return checkConfigResult;

    }


    public List<ParaConfigLineExceptionInfo> uploadKeyFrameFile(Integer satelliteId, Integer keyFrameId, MultipartFile file) throws IOException {
        SatelliteDb satelliteDb = this.satelliteDbRepository.findById(satelliteId).get();
        List<ParaConfigLineExceptionInfo> paraConfigLineExceptionInfoList = readExcelFile(file, 0);
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


    public List<ParaConfigLineExceptionInfo> readExcelFile(MultipartFile file, Integer index) throws IOException {
        List<ParaConfigLineExceptionInfo> paraConfigLineExceptionInfoList = new ArrayList<>();
        List<Object[]> readContentList = ExcelUtils.readSingleExcelFile(file).get(index);
        for (Object[] readContentArray : readContentList) {
            ParaConfigLineExceptionInfo paraConfigLineExceptionInfo = new ParaConfigLineExceptionInfo();
            paraConfigLineExceptionInfo.setParaNameRead(readContentArray[1] == null ? "" : readContentArray[1].toString());
            paraConfigLineExceptionInfo.setParaCode(readContentArray[2] == null ? "" : readContentArray[2].toString());
            paraConfigLineExceptionInfo.setExp(readContentArray[3] == null ? "" : readContentArray[3].toString());
            paraConfigLineExceptionInfo.setSubSystem(readContentArray[4] == null ? "" : readContentArray[4].toString());
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

    public void downloadSatelliteKeyFrameInfo(Integer satelliteId, HttpServletResponse response) throws IOException {
        SatelliteDb satelliteDb = satelliteDbRepository.findById(satelliteId).get();
        List<SatelliteSubsystem> satelliteSubsystemList = subsystemRepository.findBySatelliteName(satelliteDb.getSatelliteName());
        String fileName = satelliteDb.getSatelliteName() + "异常显示信息.xlsx";
        Map<String,ParaConfigLineDb> paraConfigLineDbHashMap=new HashMap<>();
        List<String> paraCodeList=new ArrayList<>();
        List<KeyFrame> keyFrameList = satelliteDb.getKeyFrameList();
        for (KeyFrame keyFrame : keyFrameList) {
            if(keyFrame.getFrameName().equals("重点参数遥测帧1")){
                for (String paraCode : keyFrame.getParaCodes().split(",")) {
                    paraConfigLineDbHashMap.put(paraCode,new ParaConfigLineDb());
                    paraCodeList.add(paraCode);
                }
                break;
            }
        }
        List<FrameCatalogDb> frameCatalogDbList = satelliteDb.getFrameCatalogDbList();
        if(frameCatalogDbList!=null&&frameCatalogDbList.size()>0){
            for (FrameCatalogDb frameCatalogDb : frameCatalogDbList) {
                List<FrameDb> frameDbList = frameCatalogDb.getFrameDbList();
                if(frameDbList!=null&&frameDbList.size()>0){
                    for (FrameDb frameDb : frameDbList) {
                        List<ParaConfigLineDb> paraConfigLineDbList = frameDb.getParaConfigLineDbList();
                        if(paraConfigLineDbList!=null&&paraConfigLineDbList.size()>0){
                            for (ParaConfigLineDb paraConfigLineDb : paraConfigLineDbList) {

                                if(paraConfigLineDbHashMap.containsKey(paraConfigLineDb.getParaCode())){
                                    paraConfigLineDbHashMap.put(paraConfigLineDb.getParaCode(),paraConfigLineDb);
                                }
                            }
                        }
                    }
                }
            }
        }
        List<String[]> keyFrameInfo=new ArrayList<>();
        String[] sheetName=new String[1];
        sheetName[0]="重点参数信息";
        keyFrameInfo.add(sheetName);
        String[]keyFrameSheetHeaderInfo=new String[5];
        keyFrameSheetHeaderInfo[0]="序号";
        keyFrameSheetHeaderInfo[1]="参数名称";
        keyFrameSheetHeaderInfo[2]="参数代号";
        keyFrameSheetHeaderInfo[3]="正常值范围";
        keyFrameSheetHeaderInfo[4]="所属子系统";
        keyFrameInfo.add(keyFrameSheetHeaderInfo);
        for (String paraCode : paraCodeList) {
            ParaConfigLineDb paraConfigLineDb = paraConfigLineDbHashMap.get(paraCode);
            int index = keyFrameInfo.size()-1;
            String[] paraCofingInfo=new String[5];
            paraCofingInfo[0]=index+"";
            paraCofingInfo[1]=paraConfigLineDb.getParaName();
            paraCofingInfo[2]=paraConfigLineDb.getParaCode();
            paraCofingInfo[3]=paraConfigLineDb.getExp();
            paraCofingInfo[4]=paraConfigLineDb.getSubSystem();
            keyFrameInfo.add(paraCofingInfo);
        }

        List<String[]> subSystemInfoList=new ArrayList<>();
        String[] sheet2Name=new String[1];
        sheet2Name[0]="参数所属子系统信息";
        subSystemInfoList.add(sheet2Name);
        String[] subSystemHeaderArray=new String[3];
        subSystemHeaderArray[0]="序号";
        subSystemHeaderArray[1]="子系统名称";
        subSystemHeaderArray[2]="子系统联系人信息";
        subSystemInfoList.add(subSystemHeaderArray);


        if(satelliteSubsystemList!=null&&satelliteSubsystemList.size()>0){
            for (SatelliteSubsystem satelliteSubsystem : satelliteSubsystemList) {

               int index= subSystemInfoList.size()- 1;
               String[] subSystemInfoArray=new String[3];
               subSystemInfoArray[0]=index+"";
               subSystemInfoArray[1]=satelliteSubsystem.getSubsystemName();
               subSystemInfoArray[2]=satelliteSubsystem.getSubsystemChargerContact();
               subSystemInfoList.add(subSystemInfoArray);
            }
        }


        List<List<String[]>>needExportContent=new ArrayList<>();
        needExportContent.add(keyFrameInfo);
        needExportContent.add(subSystemInfoList);
        ExcelUtils.exportExcelFileToWeb(needExportContent,response,fileName);

    }
}
