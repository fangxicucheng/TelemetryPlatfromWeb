package com.fang.service.saveService;

import com.fang.service.dataBaseManager.DataBaseManagerService;
import com.fang.utils.ConfigUtils;
import com.fang.utils.OBSUtils;
import com.fang.utils.UTCUtils;
import lombok.Data;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class FileSaver {
    private List<byte[]> fileLines;
    private String startTimeStr;
    private String satelliteName;
    private String stationName;
    private DataBaseManagerService dataBaseManagerService;
    private String filePath;
    private

    public FileSaver() {
        this.fileLines = new ArrayList<>();
    }

    public void save() {

    }

    private void setFilePath() {

        this.filePath = getDirectory()+"\\"+getFileName();
    }
    private void saveLocalFile(String filePath){

        File file=new File(filePath);
        file.createNewFile();
        FileUtils.writeByteArrayToFile();

    }
    private void saveOBSFile(String filePath,byte[] bytes){
        OBSUtils.saveFile(bytes,filePath);
    }


    private byte[] getBytes(){



    }

    private String getFileName() {
        return "遥测数据_" + this.stationName + "_" + this.satelliteName + "_" + this.startTimeStr + ".dat";
    }

    private String getDirectory() {

        String directoryPath=ConfigUtils.getDataRootPath() + "\\" + UTCUtils.getUTCDirectory().replaceAll("/", "\\");
        File directory=new File(directoryPath);
        if(!directory.exists()){
            directory.mkdirs();
        }
        return directoryPath;
    }


}
