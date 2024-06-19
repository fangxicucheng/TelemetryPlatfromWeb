package com.fang.service.setSatelliteConfig;

import com.fang.database.mysql.entity.TeleSatelliteNameMq;
import com.fang.database.mysql.repository.TeleSatelliteNameMqRepository;
import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.database.postgresql.entity.FrameDb;
import com.fang.database.postgresql.entity.ParaConfigLineDb;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.database.postgresql.repository.SatelliteDbRepository;
import com.fang.service.telemetryService.BaseParserService;
import com.fang.service.setSatelliteConfig.readFile.ManageSatelliteConfigFileService;
import com.fang.telemetry.satelliteConfigModel.CheckConfigResult;
import com.fang.telemetry.satelliteConfigModel.TeleSatelliteDbModel;
import com.fang.utils.DownLoadUtils;
import com.fang.utils.ExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class SatelliteConfigService {

    @Autowired
    private TeleSatelliteNameMqRepository satelliteNameMqRepository;
    @Autowired
    private SatelliteDbRepository satelliteDbRepository;
    @Autowired
    ManageSatelliteConfigFileService manageSatelliteConfigFileService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
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
        this.satelliteDbRepository.updateSatelliteDbInfo(satelliteDbModel.getId(), satelliteDbModel.getSatelliteName(),satelliteDbModel.getSatelliteId(),satelliteDbModel.getSatelliteBytesStr(),satelliteDbModel.getBdICCardsStr());
        return getTeleSatelliteDbModelList();
    }

    public void updateSatelliteInfo(TeleSatelliteDbModel satelliteDbModel)
    {
        this.satelliteDbRepository.updateSatelliteDbInfo(satelliteDbModel.getId(), satelliteDbModel.getSatelliteName(),satelliteDbModel.getSatelliteId());
    }

    public List<TeleSatelliteDbModel> deleteTeleSatelliteDbInfoById(int id) {
        this.satelliteDbRepository.deleteById(id);
        return getTeleSatelliteDbModelList();
    }
    public List<String>getAllSatelliteNameList(){
        return this.satelliteDbRepository.getSatelliteNameList();
    }

    public List<String> getSatelliteNameList(){

        List<String> satelliteNameList= new ArrayList<>();
        for (String satelliteNameSave : this.satelliteDbRepository.getSatelliteNameList()) {

            String satelliteName = satelliteNameSave.replaceAll("_北斗", "");

            if(!satelliteNameList.contains(satelliteName)){
                satelliteNameList.add(satelliteName);
            }

        }

        return satelliteNameList;

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
        if (!result.isHasWrong()) {
            satelliteDbRepository.save(satelliteDb);
        }
        return result;
    }

    public void downloadSatelliteConfigFile(Integer satelliteId, HttpServletResponse response) {
        SatelliteDb satelliteDb = satelliteDbRepository.findById(satelliteId).get();
        List<FrameCatalogDb> frameCatalogDbList = satelliteDb.getFrameCatalogDbList();
        List<String> catalogInfoList = new ArrayList<>();
        //压缩文件夹
        ZipOutputStream zipStream = null;
        String directory = baseDirectoryPath + "/配置信息/" + sdf.format(new Date()) + "/";
        File directoryFile = new File(directory);
        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        String strZipPath = directory + satelliteDb.getSatelliteName() + ".zip";
        List<Map<Integer, String[]>>excelCellListSelectShowMapList=new ArrayList<>();
        Map<Integer,String[]>excelCellListSelectShowMap=new HashMap<>();
        excelCellListSelectShowMapList.add(excelCellListSelectShowMap);
        excelCellListSelectShowMap.put(4,new String[]{"无符号","补码","有符号","有符号位","反码"});
        excelCellListSelectShowMap.put(5,new String[]{"原码显示","十进制显示","公式","状态码(十六进制)","状态码(十进制)","状态码(二进制)","时间","32位单精度浮点数"});
        File zipFile = new File(strZipPath);
        try {
            zipStream = new ZipOutputStream(new FileOutputStream(zipFile));
            if (frameCatalogDbList != null && frameCatalogDbList.size() > 0) {
                for (int index = 0; index < frameCatalogDbList.size(); index++) {
                    FrameCatalogDb frameCatalogDb = frameCatalogDbList.get(index);
                    catalogInfoList.add(frameCatalogDb.getCatalogCode() + ":" + frameCatalogDb.getCatalogName());
                    List<FrameDb> frameDbList = frameCatalogDb.getFrameDbList();
                    if (frameDbList != null && frameDbList.size() > 0) {
                        for (FrameDb frameDb : frameDbList) {
                            List<List<String[]>> frameInfoSheetList = new ArrayList<>();
                            List<String[]> frameInfoList = new ArrayList<>();
                            frameInfoSheetList.add(frameInfoList);
                            String[] strArray = new String[1];
                            strArray[0] = "配置信息";
                            String fileName = (index + 5) + "." +
                                    frameDb.getFrameCode() + "." + frameDb.getReuseChannel() + " " + frameDb.getFrameName() + frameDb.getReuseChannel() + "_" + frameDb.getFrameCode() + "_" + frameCatalogDb.getCatalogCode() + "_ZT-C002.xlsx";
                            ZipEntry zipEntry = new ZipEntry(fileName);
                            zipStream.putNextEntry(zipEntry);
                            frameInfoList.add(strArray);
                            String[] headerContentArray = new String[11];
                            headerContentArray[0] = "起始位置";
                            headerContentArray[1] = "长度";
                            headerContentArray[2] = "遥测代号";
                            headerContentArray[3] = "遥测名称";
                            headerContentArray[4] = "原码存储方式";
                            headerContentArray[5] = "工程值处理方式";
                            headerContentArray[6] = "处理参数";
                            headerContentArray[7] = "工程值单位";
                            headerContentArray[8] = "保留小数位";
                            headerContentArray[9] = "预警值";
                            headerContentArray[10] = "量纲";
                            frameInfoList.add(headerContentArray);
                            List<ParaConfigLineDb> paraConfigLineDbList = frameDb.getParaConfigLineDbList();
                            if (paraConfigLineDbList != null && paraConfigLineDbList.size() > 0) {
                                for (ParaConfigLineDb paraConfigLineDb : paraConfigLineDbList) {
                                    String[] lineContent = new String[11];
                                    lineContent[0] = paraConfigLineDb.getBitStart() + "";
                                    lineContent[1] = paraConfigLineDb.getBitNum() + "";
                                    lineContent[2] = paraConfigLineDb.getParaCode()+"";
                                    lineContent[3] = paraConfigLineDb.getParaName()+"";
                                    lineContent[4] = paraConfigLineDb.getSourceCodeSaveType()+"";
                                    lineContent[5] = paraConfigLineDb.getHandleType()+"";
                                    lineContent[6] = paraConfigLineDb.getHandleParam()+"";

                                    switch (paraConfigLineDb.getParseType()){
                                        case 2:{
                                            lineContent[4]="补码";
                                            lineContent[5]="十进制显示";
                                        }break;
                                        case 3:
                                        case 4:
                                        {
                                            lineContent[4]="无符号";
                                            lineContent[5]="原码显示";
                                        }break;
                                        case 5:{
                                            lineContent[4]="无符号";
                                            lineContent[5]="32位单精度浮点数";
                                        }break;
                                        case 0:
                                        case 9:{

                                            lineContent[4]="无符号";
                                            lineContent[5]="十进制显示";
                                        }break;
                                        case 10:{
                                            lineContent[6]=paraConfigLineDb.getHandleParam()+"";
                                            if(paraConfigLineDb.getHandleType().contains("状态码")){
                                                lineContent[6]=lineContent[6].replaceAll("\r\n",";\r\n");
                                            }
                                            lineContent[5]=lineContent[5].replaceAll(" ","");

                                            switch(paraConfigLineDb.getHandleType()){
                                                case"原码":
                                                case"源码":
                                                case"原码显示":
                                                case"十六进制显示":
                                                case"十六进制":
                                                {
                                                    lineContent[5]="原码显示";
                                                }break;
                                            }
                                        }break;
                                    }
                                    lineContent[7] = "";
                                    lineContent[8] = "";
                                    lineContent[9] = "";
                                    lineContent[10] = paraConfigLineDb.getDimension()+"";
                                    frameInfoList.add(lineContent);
                                }
                            }
                            ExcelUtils.exportExcelFile(frameInfoSheetList, zipStream,excelCellListSelectShowMapList);
                        }
                    }
                }
            }
            String catalogConfigFileName = "配置文件.txt";
            ZipEntry zipEntry = new ZipEntry(catalogConfigFileName);
            zipStream.putNextEntry(zipEntry);
            downloadFrameCatalogConfigFile(catalogInfoList, zipStream);
            zipStream.flush();
            zipStream.close();
            if (zipFile.exists()) {
                DownLoadUtils.download(response, satelliteDb.getSatelliteName() + ".zip", zipFile.getPath());
            }
            FileUtils.deleteDirectory(directoryFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void downloadFrameCatalogConfigFile(List<String> catalogInfoList, OutputStream out) throws IOException {
        for (int i = 0; i < catalogInfoList.size(); i++) {
            String catalogInfo = catalogInfoList.get(i);
            if (i != catalogInfoList.size() - 1) {
                catalogInfo += "\r\n";
            }
            out.write(catalogInfo.getBytes("UTF-8"));
        }

    }
}
