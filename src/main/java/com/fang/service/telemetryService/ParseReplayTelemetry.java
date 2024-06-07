package com.fang.service.telemetryService;

import com.fang.config.satellite.paraParser.FrameInfo;
import com.fang.config.satellite.paraParser.ParaParser;
import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.service.dataBaseManager.DataBaseManagerService;
import com.fang.service.gpsService.gpsManager.GPSRecordManager;
import com.fang.service.kafkaService.KafkaRestTemplateService;
import com.fang.service.saveService.FileSaver;
import com.fang.telemetry.TelemetryFrameModel;
import com.fang.utils.ConfigUtils;
import com.fang.utils.LoadFileUtils;
import com.fang.utils.StringConvertUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ParseReplayTelemetry {

    public static List<TelemetryFrameModel> parseTelemetry( String filePathRequest) throws IOException {
        List<TelemetryFrameModel> frameList = new ArrayList<>();
        String filePath = filePathRequest.replaceAll("\\\\","/");
        String[] filePathArray = filePath.split("/");
        String[] infoArray = filePathArray[filePathArray.length - 1].split("_");


        String satelliteName = infoArray[2];
        String stationName = infoArray[1];
        if (stationName.contains("北斗")&&satelliteName.contains("_北斗")) {
            satelliteName += "_北斗";
        }
        String satelliteId = ConfigUtils.getSatelliteIdBySatelliteName(satelliteName);
        String stationId = "StationId";
        ParaParser paraParser = ConfigUtils.getParaParser(satelliteName);
        DataQualityManager dataQualityManager = new DataQualityManager();
        String telemetryPlanId = satelliteId + "_" + stationId + "_-1";

        List<byte[]> bytesList = LoadFileUtils.readFile(filePath);
        parsePrePare(paraParser,dataQualityManager);
        for (byte[] bytes : bytesList) {
            frameList.add(parseService(bytes, paraParser, dataQualityManager, telemetryPlanId));
        }
        destroyEnd(paraParser);
        return frameList;
    }


    //处理业务
    public static TelemetryFrameModel parseService(byte[] receiveBytes, ParaParser paraParser, DataQualityManager dataQualityManager, String telemetryPlanId) {
        TelemetryFrameModel frame = new TelemetryFrameModel();
        try {
            frame.setTelemetryPlanId(telemetryPlanId);
            FrameInfo frameInfo = paraParser.parseFrameInfoFromBytes(receiveBytes);
            frame.setRealTimeContent(StringConvertUtils.bytesToHex(frameInfo.getDataBytes()));
            dataQualityManager.setFrameInfo(frameInfo);
            paraParser.parseTelemetryFrame(frameInfo, frame);
            dataQualityManager.serFrame(frame);
            if (frame.getParameterList() == null) {
                System.out.println("开始le");
            }
            if (frame == null) {
                System.out.println("空数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return frame;
    }


    private static void parsePrePare(ParaParser paraParser, DataQualityManager dataQualityManager) {
        dataQualityManager.refresh();
        paraParser.initThread();
    }

    private static void destroyEnd(ParaParser paraParser) {

        paraParser.destroyThread();
    }


}
