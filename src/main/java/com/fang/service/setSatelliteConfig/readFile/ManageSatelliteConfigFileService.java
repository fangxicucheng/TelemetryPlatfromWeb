package com.fang.service.setSatelliteConfig.readFile;

import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.database.postgresql.entity.FrameDb;
import com.fang.database.postgresql.entity.ParaConfigLineDb;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.telemetry.satelliteConfigModel.ConfigFileInfoClass;
import com.fang.utils.ExcelReader;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ManageSatelliteConfigFileService {
    public SatelliteDb readSatelliteDbConfigFile(List<File> fileList) throws IOException {
        SatelliteDb satelliteDb = new SatelliteDb();
        List<String> paraCodeList = new ArrayList<>();
        for (File file : fileList) {
            if (file.getName().contains(".xlsx")) {
                readSatelliteDbFromExcelFile(file, satelliteDb, paraCodeList);
            } else if (file.getName().contains(".txt")) {
                readTextFile(file, satelliteDb);
            }
        }
        reflashFrameCatalogNumber(satelliteDb);
        return satelliteDb;

    }

    public FrameCatalogDb readFrameCatalogDbConfigFile(List<File> fileList){
        FrameCatalogDb frameCatalogDb=new FrameCatalogDb();
        List<String> paraCodeList=new ArrayList<>();
        for (File file : fileList) {
            if(file.getName().contains(".xlsx")){
                readFrameCatalogFromExcelFile(file,frameCatalogDb,paraCodeList);
            }
        }
        reflashFrameNumber(frameCatalogDb);
        return frameCatalogDb;
    }

    public void reflashFrameCatalogNumber(SatelliteDb satelliteDb) {

        if (satelliteDb.getFrameCatalogDbList() != null) {
            satelliteDb.getFrameCatalogDbList().sort((o1, o2) -> {
                return o1.getCatalogCode() - o2.getCatalogCode();
            });
            for (int i = 0; i < satelliteDb.getFrameCatalogDbList().size(); i++) {
                FrameCatalogDb catalogDb = satelliteDb.getFrameCatalogDbList().get(i);
                catalogDb.setNum(i + 1);
                reflashFrameNumber(catalogDb);

            }
        }
    }

    public void reflashFrameNumber(FrameCatalogDb catalogDb) {
        if (catalogDb.getFrameDbList() != null) {
            catalogDb.getFrameDbList().sort((o1, o2) -> {

                if (o1.getFrameCode() == o2.getFrameCode()) {
                    return o1.getReuseChannel() - o2.getReuseChannel();
                }
                return o1.getFrameCode() - o2.getFrameCode();

            });
            for (int i1 = 0; i1 < catalogDb.getFrameDbList().size(); i1++) {

                FrameDb frameDb = catalogDb.getFrameDbList().get(i1);
                frameDb.setNum(i1 + 1);
            }

        }
    }


    public void readFrameCatalogFromExcelFile(File file,FrameCatalogDb catalogDb,List<String> paraCodeList) {
        try {
            ConfigFileInfoClass fileInfo = getFrameInfo(file);
            if (fileInfo.isHasInit()) {
                FrameDb frameDb = getFrameDb(fileInfo, file, paraCodeList);
                setFrameCatalog(frameDb,catalogDb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readSatelliteDbFromExcelFile(File file, SatelliteDb satelliteDb, List<String> paraCodeList) throws FileNotFoundException {
        try {
            ConfigFileInfoClass fileInfo = getFrameInfo(file);
            if (fileInfo.isHasInit()) {
                FrameDb frameDb = getFrameDb(fileInfo, file, paraCodeList);
                setSatelliteDb(frameDb,satelliteDb,fileInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSatelliteDb(FrameDb frameDb,SatelliteDb satelliteDb,ConfigFileInfoClass fileInfo){
        FrameCatalogDb catalogDb = null;
        if (satelliteDb.getFrameCatalogDbList() == null) {
            satelliteDb.setFrameCatalogDbList(new ArrayList<>());
        }
        for (FrameCatalogDb frameCatalogDb : satelliteDb.getFrameCatalogDbList()) {
            if (frameCatalogDb.getCatalogCode() == fileInfo.getCatalogCode()) {
                catalogDb = frameCatalogDb;
                break;
            }
        }
        if (catalogDb == null) {
            catalogDb = new FrameCatalogDb();
            catalogDb.setCatalogCode(fileInfo.getCatalogCode());
            satelliteDb.getFrameCatalogDbList().add(catalogDb);
        }
        setFrameCatalog(frameDb,catalogDb);


    }



    public void setFrameCatalog(FrameDb frameDb,FrameCatalogDb catalogDb){
        if (catalogDb.getFrameDbList() == null) {
            catalogDb.setFrameDbList(new ArrayList<>());
        }
        catalogDb.getFrameDbList().add(frameDb);
    }





    public void readTextFile(File file, SatelliteDb satelliteDb) throws IOException {

        String fileContent = FileUtils.readFileToString(file, "UTF-8");
        String[] catalogContentInfoArray = fileContent.replaceAll("\n", "").split("\r");
        if (satelliteDb.getFrameCatalogDbList() == null) {
            satelliteDb.setFrameCatalogDbList(new ArrayList<>());
        }
        List<FrameCatalogDb> frameCatalogDbList = satelliteDb.getFrameCatalogDbList();

        for (String catalogContentInfo : catalogContentInfoArray) {
            String[] catalogInfoArray = catalogContentInfo.split(":");
            int catalogCode = Integer.parseInt(catalogInfoArray[0]);
            String catalogName = catalogInfoArray[1];
            boolean hasFound = false;
            for (FrameCatalogDb catalogDb : frameCatalogDbList) {

                if (catalogCode == catalogDb.getCatalogCode()) {
                    catalogDb.setCatalogName(catalogName);
                    hasFound = true;
                    break;
                }
            }
            if (!hasFound) {
                FrameCatalogDb catalogDb = new FrameCatalogDb();
                catalogDb.setCatalogName(catalogName);
                catalogDb.setCatalogCode(catalogCode);
                frameCatalogDbList.add(catalogDb);
            }
        }
    }

    public ConfigFileInfoClass getFrameInfo(File file) {
        return new ConfigFileInfoClass(file.getName());

    }

    public FrameDb readFrameDbFromFile(File file) throws FileNotFoundException {
        FrameDb frameDb = new FrameDb();
        frameDb.setParaConfigLineDbList(readFrameExcel(file,new ArrayList<>()));

        return frameDb;
    }

    public FrameDb getFrameDb(ConfigFileInfoClass frameInfo, File file, List<String> paraCodeList) throws FileNotFoundException {

        FrameDb frameDb = new FrameDb();
        frameDb.setFrameName(frameInfo.getFrameName());
        frameDb.setFrameCode(frameInfo.getFrameCode());
        frameDb.setReuseChannel(frameInfo.getRefuseChannel());

        frameDb.setParaConfigLineDbList(readFrameExcel(file, paraCodeList));

        return frameDb;

    }


    public List<ParaConfigLineDb> readFrameExcel(File file, List<String> paraCodeList) throws FileNotFoundException {
        List<ParaConfigLineDb> result = new ArrayList<>();
        List<Object[]> objects = ExcelReader.importExcel(new FileInputStream(file));
        for (Object[] object : objects) {
            ParaConfigLineDb paraConfigLineDb = new ParaConfigLineDb();
            paraConfigLineDb.setRound(10);
            paraConfigLineDb.setBitStart(Integer.parseInt(((String) object[0]).replaceAll("\\D", "")));
            paraConfigLineDb.setBitNum(Integer.parseInt(((String) object[1]).replaceAll("\\D", "")));
            String dimension = ((String) object[10]).replaceAll("\\p{C}", "").replaceAll(" ", "");
            if (dimension.isEmpty()) {
                dimension = "1";
            }


            paraConfigLineDb.setDimension(dimension);
            String paraCode = ((String) object[2]).replaceAll("\\p{C}", "").replaceAll(" ", "");
            paraConfigLineDb.setSourceCodeSaveType(((String) object[4]).replaceAll("\\p{C}", "").replaceAll(" ", ""));
            if (paraCodeList.contains(paraCode)) {

                String finalParaCode = paraCode;
                paraCode = paraCode + "_" + paraCodeList.stream().filter(t -> t.contains(finalParaCode)).count();
            }

            paraConfigLineDb.setParaCode(paraCode);

            paraConfigLineDb.setParaName(((String) object[3]).replaceAll("\\p{C}", "").replaceAll(" ", ""));
            paraConfigLineDb.setHandleType(((String) object[5]).replaceAll("\\p{C}", "").replaceAll(" ", ""));
            paraConfigLineDb.setParseType(10);
            if (paraConfigLineDb.getParaCode().equals("ZT-C002") && paraConfigLineDb.getBitNum() == 32 && paraConfigLineDb.getBitStart() == 16) {

                paraConfigLineDb.setHandleType("十进制显示");
            }
            if (paraConfigLineDb.getParaCode().contains("ZT-C002_") && paraConfigLineDb.getBitNum() == 32 && paraConfigLineDb.getBitStart() == 16) {

                paraConfigLineDb.setHandleType("时间");
            }
            String handleParam = ((String) object[6]).replaceAll("\\p{C}", "").replaceAll(" ", "").replaceAll("；", ";");
            if (handleParam.endsWith(";")) {
                handleParam = handleParam.substring(0, handleParam.length() - 2);
            }
            paraConfigLineDb.setHandleParam(handleParam.replaceAll(";", "\r\n"));


            result.add(paraConfigLineDb);


        }
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setNum(i + 1);
        }
        return result;

    }
}
