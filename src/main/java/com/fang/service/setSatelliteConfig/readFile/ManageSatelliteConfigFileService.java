package com.fang.service.setSatelliteConfig.readFile;

import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.database.postgresql.entity.FrameDb;
import com.fang.database.postgresql.entity.ParaConfigLineDb;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.utils.ExcelReader;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ManageSatelliteConfigFileService {
    public  SatelliteDb readSatelliteDbConfigFile(List<File> fileList) throws IOException {
        SatelliteDb satelliteDb = new SatelliteDb();
        List<String> paraCodeList = new ArrayList<>();
        for (File file : fileList) {
            if (file.getName().contains(".xlsx")) {
                readExcelFile(file, satelliteDb, paraCodeList);
            } else if (file.getName().contains(".txt")) {
                readTextFile(file, satelliteDb);
            }

        }
        if (satelliteDb.getFrameCatalogDbList() != null) {
            satelliteDb.getFrameCatalogDbList().sort((o1, o2) -> {
                return o1.getCatalogCode() - o2.getCatalogCode();
            });
            for (int i = 0; i < satelliteDb.getFrameCatalogDbList().size(); i++) {
                FrameCatalogDb catalogDb = satelliteDb.getFrameCatalogDbList().get(i);
                catalogDb.setNum(i + 1);
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
        }


        return satelliteDb;

    }

    public  void readExcelFile(File file, SatelliteDb satelliteDb, List<String> paraCodeList) throws FileNotFoundException {
        try {
            String[] fileNameInfo = file.getName().split(" ")[1].split("\\.")[0].split("_");
            if (fileNameInfo.length == 4) {
                String frameName = fileNameInfo[0].substring(0, fileNameInfo[0].length() - 1);
                int refuseChannel = Integer.parseInt(fileNameInfo[0].substring(fileNameInfo[0].length() - 1, fileNameInfo[0].length()));
                int frameCode = Integer.parseInt(fileNameInfo[1]);
                int catalogCode = Integer.parseInt(fileNameInfo[2]);
                if (satelliteDb.getFrameCatalogDbList() == null) {
                    satelliteDb.setFrameCatalogDbList(new ArrayList<>());
                }
                FrameCatalogDb catalogDb = null;
                for (FrameCatalogDb frameCatalogDb : satelliteDb.getFrameCatalogDbList()) {

                    if (frameCatalogDb.getCatalogCode() == catalogCode) {
                        catalogDb = frameCatalogDb;
                        break;
                    }
                }
                if (catalogDb == null) {
                    catalogDb = new FrameCatalogDb();
                    catalogDb.setCatalogCode(catalogCode);
                    satelliteDb.getFrameCatalogDbList().add(catalogDb);
                }

                if (catalogDb.getFrameDbList() == null) {
                    catalogDb.setFrameDbList(new ArrayList<>());
                }

                FrameDb frameDb = new FrameDb();
                frameDb.setFrameName(frameName);
                frameDb.setReuseChannel(refuseChannel);
                frameDb.setFrameCode(frameCode);
                frameDb.setParaConfigLineDbList(new ArrayList<>());
                readExcelFile(file, frameDb, paraCodeList);
                catalogDb.getFrameDbList().add(frameDb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void readTextFile(File file, SatelliteDb satelliteDb) throws IOException {

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

    public  void readExcelFile(File file, FrameDb frameDb, List<String> paraCodeList) throws FileNotFoundException {

        List<Object[]> objects = ExcelReader.importExcel(new FileInputStream(file));
        for (Object[] object : objects) {
            ParaConfigLineDb paraConfigLineDb = new ParaConfigLineDb();
            paraConfigLineDb.setRound(10);
            paraConfigLineDb.setBitStart(Integer.parseInt(((String) object[0]).replaceAll("\\D", "")));
            paraConfigLineDb.setBitNum(Integer.parseInt(((String) object[0]).replaceAll("\\D", "")));
            paraConfigLineDb.setDimension("1");
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
            String handleParam = ((String) object[5]).replaceAll("\\p{C}", "").replaceAll(" ", "").replaceAll("；", ";");
            if (handleParam.endsWith(";")) {
                handleParam = handleParam.substring(0, handleParam.length() - 2);
            }
            paraConfigLineDb.setHandleParam(handleParam.replaceAll(";", "\r\n"));

            frameDb.getParaConfigLineDbList().add(paraConfigLineDb);


        }
        for (int i = 0; i < frameDb.getParaConfigLineDbList().size(); i++) {
            frameDb.getParaConfigLineDbList().get(i).setNum(i + 1);
        }

    }
}
