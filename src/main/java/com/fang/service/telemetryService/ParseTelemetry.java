package com.fang.service.telemetryService;


import com.fang.config.satellite.paraParser.FrameInfo;
import com.fang.config.satellite.paraParser.ParaParser;
import com.fang.service.dataBaseManager.DataBaseManagerService;
import com.fang.service.gpsService.gpsManager.GPSRecordManager;
import com.fang.service.kafkaService.KafkaRestTemplateService;
import com.fang.service.restartTimeService.RestartTimeService;
import com.fang.service.saveService.FileSaver;
import com.fang.service.saveService.ReceiveRecordService;
import com.fang.telemetry.TelemetryFrame;
import com.fang.utils.ConfigUtils;
import com.fang.utils.StringConvertUtils;
import lombok.Data;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Data

public class ParseTelemetry {
    private String satelliteId;
    private String satelliteName;
    private String stationName;
    private String stationId;
    private Thread parseThread;
    private LinkedBlockingQueue<byte[]> queue;
    private ParaParser paraParser;
    private DataQualityManager dataQualityManager;
    private long waitSeconds;
    private boolean isReceiving;
    private boolean hasInit;
    private Thread receiveThread;
    private boolean runMark;
    private String telemetryPlanId;
    private FileSaver fileSaver;
    private GPSRecordManager gpsRecordManager;


    public ParseTelemetry(String satelliteName, String stationName, String stationId) {
        this.satelliteName = satelliteName;
        this.stationName = stationName;
        this.stationId = stationId;
        this.satelliteId = ConfigUtils.getSatelliteIdBySatelliteName(satelliteName);
        this.queue = new LinkedBlockingQueue<>();
        this.paraParser = ConfigUtils.getParaParser(satelliteName);
        this.dataQualityManager = new DataQualityManager();
        this.waitSeconds = 30;
        this.runMark = true;
        this.fileSaver = new FileSaver(satelliteName, stationName);
        this.gpsRecordManager=new GPSRecordManager(this.satelliteName,this.stationName);
        if (satelliteName.contains("北斗")) {
            this.waitSeconds = 60 * 5;
        }
        this.telemetryPlanId = this.satelliteId + "_" + this.stationId + "_-1";
        start();
    }
    public void enQueue(byte[] bytes) {
        queue.add(bytes);
    }
    //处理业务
    public void parseService() {

        while (true) {
            try {
                byte[] receiveBytes = this.isReceiving ? queue.poll(waitSeconds, TimeUnit.SECONDS) : queue.take();
                if(receiveBytes==null){
                    overTimeNotReceive();
                    continue;
                }
                if (!this.isReceiving) {
                    initThreadLocal();
                }

                this.fileSaver.writeLine(receiveBytes);
                TelemetryFrame frame = new TelemetryFrame();
                frame.setTelemetryPlanId(telemetryPlanId);
                FrameInfo frameInfo = this.paraParser.parseFrameInfoFromBytes(receiveBytes);
                frame.setRealTimeContent(StringConvertUtils.bytesToHex(frameInfo.getDataBytes()));
                this.dataQualityManager.setFrameInfo(frameInfo);
                this.paraParser.parseTelemetryFrame( frameInfo, frame);
                this.dataQualityManager.serFrame(frame);
                this.gpsRecordManager.setGpsData(frame);
                KafkaRestTemplateService.sendKafkaMsg("telemetry",frame);
            } catch (InterruptedException e) {
                overTimeNotReceive();
                if (!runMark) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void stop() {

        if (this.receiveThread != null) {
            this.runMark = false;
            this.receiveThread.interrupt();
        }




    }

    public void start() {
        stop();
        this.receiveThread = new Thread(() -> parseService());
        this.receiveThread.start();
    }

    private void overTimeNotReceive() {
        this.isReceiving = false;
        destroyTheadLocal();
        this.dataQualityManager.refresh();
        saveFile();
        this.gpsRecordManager.saveGps();
    }

    private void initThreadLocal() {

        this.isReceiving = true;
        this.dataQualityManager.refresh();
        this.paraParser.initThread();
        this.fileSaver.refresh();
        this.gpsRecordManager.refreshGpsConfigInfo();

    }

    private void destroyTheadLocal() {
        this.paraParser.destroyThread();
    }

    private void saveFile() {
        this.fileSaver.save();
    }
}
