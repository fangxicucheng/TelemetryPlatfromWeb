package com.fang.service.gpsService.gpsManager;

import com.fang.telemetry.TelemetryFrameModel;
import com.fang.telemetry.TelemetryParameterModel;
import com.fang.utils.ConfigUtils;
import com.fang.utils.UTCUtils;
import lombok.Data;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GPSRecordManager {
    private String satelliteName;
    private String stationName;
    private List<GpsContentData> gpsContentDataList;
    private GpsContentData gpsContentData;
    private boolean hasGps;
    private GpsConfigInfo gpsConfigInfo;


    public GPSRecordManager(String satelliteName, String stationName) {
        this.satelliteName = satelliteName;
        this.stationName = stationName;
        this.hasGps = false;
        this.gpsContentDataList = new ArrayList<>();
    }

    public void refreshGpsConfigInfo() {
        this.gpsConfigInfo = ConfigUtils.getGpsConfigInfo(satelliteName);
        if (this.gpsConfigInfo != null) {
            this.hasGps = true;
        }

    }


    public void setGpsData(TelemetryFrameModel frame) {

        if (hasGps && gpsConfigInfo.checkGpsFrame(frame.getFrameName())) {
            for (TelemetryParameterModel parameterModel : frame.getParameterList()) {
                String paraCode = parameterModel.getParaCode();
                if (paraCode.equals(this.gpsConfigInfo.getTimeParaCode())) {
                    this.gpsContentData = new GpsContentData();
                    this.gpsContentDataList.add(gpsContentData);
                    this.gpsContentData.setTime(parameterModel.getParaDouble());
                } else if (paraCode.equals(this.gpsConfigInfo.getXLocationParaCode())) {
                    this.gpsContentData.setXLocation(parameterModel.getParaDouble());
                } else if (paraCode.equals(this.gpsConfigInfo.getYLocationParaCode())) {
                    this.gpsContentData.setYLocation(parameterModel.getParaDouble());
                } else if (paraCode.equals(this.gpsConfigInfo.getZLocationParaCode())) {
                    this.gpsContentData.setZLocation(parameterModel.getParaDouble());
                } else if (paraCode.equals(this.gpsConfigInfo.getXSpeedParaCode())) {
                    this.gpsContentData.setXSpeed(parameterModel.getParaDouble());
                } else if (paraCode.equals(this.gpsConfigInfo.getYSpeedParaCode())) {
                    this.gpsContentData.setYSpeed(parameterModel.getParaDouble());
                } else if (paraCode.equals(this.gpsConfigInfo.getZSpeedParaCode())) {
                    this.gpsContentData.setZSpeed(parameterModel.getParaDouble());
                }
            }
        }
    }

    public void saveGps() {
        if (this.gpsContentDataList.size() > 5) {
            if (!checkOverTime()||true) {

                try {
                    saveGPSFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        refresh();
    }

    private void saveGPSFile() throws IOException {
        String content = getContent();

        File gpsFile = createGpsFile(getDirectoryPath());

        FileUtils.writeStringToFile(gpsFile, content, "UTF-8");
    }

    private String getDirectoryPath() {
        String directoryPath = ConfigUtils.getGPSRootPath()+"\\" + this.satelliteName + "\\" + UTCUtils.getUTCDirectory();
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directoryPath;
    }

    private File createGpsFile(String directoryPath) throws IOException {
        String fileName = directoryPath + "\\" + "遥测轨道参数_" + this.satelliteName + "_" + this.stationName + "_" + UTCUtils.getUTCStr() + ".txt";
        File file = new File(fileName);
        if(!file.exists()){
            file.createNewFile();
        }
        return file;

    }


    private boolean checkOverTime() {
        double time = gpsContentData.getTime();
        LocalDateTime lastTime = UTCUtils.getUTCTime(time);
        //当前时间的24小时之前在最后一个点之后则超时
        return LocalDateTime.now().plusDays(-1).isAfter(lastTime);
    }

    private void refresh() {
        this.gpsContentDataList.clear();
        this.gpsContentData = null;
        this.gpsConfigInfo = null;
        this.hasGps=false;
    }

    private String getContent() {
        StringBuilder sb = new StringBuilder();
        int size = this.gpsContentDataList.size();
        for (int i = 0; i < size; i++) {
            sb.append(this.gpsContentDataList.get(i).getWriterStr());
            if (i < size - 1) {
                sb.append("\r\n");
            }
        }
        return sb.toString();
    }

}
