package com.fang.service.saveService;

import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.service.dataBaseManager.DataBaseManagerService;
import com.fang.utils.ConfigUtils;
import com.fang.utils.OBSUtils;
import com.fang.utils.UTCUtils;
import lombok.Data;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class FileSaver {
    private List<byte[]> dataBytesList;
    private LocalDateTime startTime;
    private LocalDateTime updateTime;
    private boolean hasStart;
    private String startTimeStr;
    private String satelliteName;
    private String stationName;
    private DataBaseManagerService dataBaseManagerService;

    private String filePath;

    public FileSaver(String satelliteName, String stationName) {
        this.satelliteName = satelliteName;
        this.stationName = stationName;
        this.dataBytesList = new ArrayList<>();
    }

    public void refresh() {
        this.startTimeStr = "";
        this.startTime = null;
        this.hasStart = false;
        this.dataBytesList.clear();
        this.updateTime = null;
        this.filePath = "";
    }

    public void writeLine(byte[] dataBytes) {
        this.updateTime = LocalDateTime.now();
        if (!this.hasStart) {
            this.hasStart = true;
            this.startTime = this.updateTime;

            this.startTimeStr = UTCUtils.convertLocalTimeToStrFilePath(this.startTime);
        }
        this.dataBytesList.add(dataBytes);
    }

    private ReceiveRecord getRecord() {
        Date startRecord = UTCUtils.convertLocalDateTimeToDate(this.startTime);
        Date endRecord = UTCUtils.convertLocalDateTimeToDate(this.updateTime);
        ReceiveRecord receiveRecord=new ReceiveRecord();
        receiveRecord.setFilePath(this.filePath);
        receiveRecord.setStartTime(startRecord);
        receiveRecord.setEndTime(endRecord);
        return receiveRecord;

        //DataBaseManagerService.saveReceiveRecord(startRecord,endRecord,this.satelliteName,this.filePath);
    }


    // public FileSaver() {

    //  }

    public ReceiveRecord save() {
        byte[] saveBytes = getSaveBytes();
        if (saveBytes != null) {
            this.filePath = getFilePath();
            saveLocalFile(this.filePath, saveBytes);
            saveOBSFile(this.filePath, saveBytes);
        }
        ReceiveRecord receiveRecord=getRecord();
        refresh();
        return receiveRecord;
    }

    private String getFilePath() {

        return getDirectory() + "\\" + getFileName();
    }

    private void saveLocalFile(String filePath, byte[] dataBytes) {
        try {
            File file = new File(filePath);
            file.createNewFile();
            FileUtils.writeByteArrayToFile(file, dataBytes);
        } catch (IOException e) {

        }
    }

    private void saveOBSFile(String filePath, byte[] bytes) {
        OBSUtils.saveFile(bytes, filePath);
    }

    private byte[] getSaveBytes() {
        byte[] dataBytes = null;
        int length = 0;
        for (byte[] bytes : this.dataBytesList) {

            length += bytes.length;
        }
        if (length > 0) {
            dataBytes = new byte[length];

            int index = 0;
            for (byte[] bytes : this.dataBytesList) {
                System.arraycopy(bytes, 0, dataBytes, index, bytes.length);
                index += bytes.length;
            }

        }
        return dataBytes;
    }


    private String getExtension() {
        if (checkIsBd()) {
            return ".txt";
        } else {
            return ".dat";
        }
    }

    private boolean checkIsBd() {
        return this.satelliteName.contains("北斗");
    }

    private String getFileName() {
        return "遥测数据_" + this.stationName + "_" + this.satelliteName + "_" + this.startTimeStr + getExtension();
    }

    private String getDirectory() {
        String directoryPath = ConfigUtils.getDataRootPath() + "\\" + UTCUtils.getUTCDirectory().replaceAll("/", "\\\\");
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directoryPath;
    }


}
