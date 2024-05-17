package com.fang.service.telemetryService;
import com.fang.config.satellite.paraParser.FrameInfo;
import com.fang.config.satellite.paraParser.ParaParser;
import com.fang.database.postgresql.entity.CountPara;
import com.fang.service.countParaService.CountParaService;
import com.fang.service.dataBaseManager.DataBaseManagerService;
import com.fang.telemetry.TelemetryFrame;
import com.fang.utils.ConfigUtils;
import com.fang.utils.StringConvertUtils;
import lombok.Data;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
@Data
public class ParseTelemetry {
    private String satelliteId;
    private String satelliteName;
    private String stationName;
    private String stationId;
    private Thread parseThread;
    private LinkedBlockingDeque<byte[]> queue;
    private ParaParser paraParser;
    private DataQualityManager dataQualityManager;
    private long waitSeconds;
    private boolean isReceiving;
    private boolean hasInit;
    private Thread receiveThread;
    private boolean runMark;
    public ParseTelemetry(String satelliteName, String stationName, String stationId) {
        this.satelliteName = satelliteName;
        this.stationName = stationName;
        this.stationId = stationId;
        this.satelliteId = ConfigUtils.getSatelliteIdBySatelliteName(satelliteName);
        this.queue = new LinkedBlockingDeque<>();
        this.paraParser = ConfigUtils.getParaParser(satelliteName);
        this.dataQualityManager = new DataQualityManager();
        this.waitSeconds = 30;
        this.runMark=true;
        if (satelliteName.contains("北斗")) {
            this.waitSeconds = 60 * 5;
        }
    }
    public void enQueue(byte[] bytes) {
        queue.push(bytes);
    }
    //处理业务
    public void parseService() {
        while (true) {
            try {
                byte[] receiveBytes = this.isReceiving ? queue.poll(waitSeconds, TimeUnit.SECONDS) : queue.poll();
                if(!this.isReceiving)
                {
                    this.paraParser.initThread();
                }
                TelemetryFrame frame = new TelemetryFrame();
                FrameInfo frameInfo = this.paraParser.parseFrameInfoFromBytes(receiveBytes);
                frame.setRealTimeContent(StringConvertUtils.bytesToHex(frameInfo.getDataBytes()));
                this.dataQualityManager.setFrameInfo(frameInfo);
                this.paraParser.parseTelemetryFrame(receiveBytes, frameInfo, frame);
            } catch (InterruptedException e) {
                overTimeNotReceive();
                if(!runMark){
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void stop() {
        if (this.receiveThread != null) {
            this.runMark=false;
            this.receiveThread.interrupt();
        }
    }
    private void overTimeNotReceive() {
        this.isReceiving = false;
        destroyTheadLocal();
        this.dataQualityManager.refresh();
    }
    private void initThreadLocal() {
        if (!this.isReceiving) {
            this.isReceiving = true;
            this.dataQualityManager.refresh();
        }
        getCountPara();
    }
    private void getCountPara(){
        CountParaService countParaService = DataBaseManagerService.getCountParaService();
        List<CountPara> paraCountList = countParaService.getParaCountBySatelliteName(satelliteName);
        this.paraParser.setUnchangedParaValue(paraCountList);
    }
    private void destroyTheadLocal() {
        this.paraParser.destroyThread();
    }
    private void saveFile()
    {




    }
}
