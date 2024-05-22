package com.fang.service.stationService;

import com.fang.service.telemetryService.ParseTelemetry;
import com.fang.utils.ParseBDUtils;
import com.fang.utils.ConfigUtils;
import com.fang.utils.StringConvertUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Log4j
public class DataTransfer {
    private String stationName;
    private String stationId;

    private Map<String, LocalTime> satelliteReceiveDateTime;
    private Map<String, ParseTelemetry> parseTelemetryMap;
    //转换线程;
    private Thread transferThread;

    private LinkedBlockingQueue<byte[]> queue;

    public DataTransfer(String stationName, String stationId,LinkedBlockingQueue<byte[]> receiveQueue) {
        this.stationName = stationName;
        this.stationId = stationId;
        this.satelliteReceiveDateTime = new HashMap<>();
        this.queue=receiveQueue;
        start();
    }


    public void start() {
        if(this.stationName.contains("北斗")){
            this.transferThread=new Thread(()->transferBDNormalData());

        }else{
            this.transferThread=new Thread(()->transferNormalData());
        }
        this.transferThread.start();
    }


    private void transferNormalData() {

        while (true) {
            try {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                System.out.println(stationName + "启动了transfer线程");
                byte[] bytes = this.queue.take();

                byte[] receiveBuf = Arrays.copyOfRange(bytes, 1, bytes.length );
                byte missionTypeByte1 = receiveBuf[10];
                byte missionTypeByte2 = receiveBuf[11];
                byte missionTypeByte3 = receiveBuf[12];
                byte missionTypeByte4 = receiveBuf[13];
                if (missionTypeByte1 != 0x11 ||
                        missionTypeByte2 != 0x01 ||
                        missionTypeByte3 != 0x11 ||
                        missionTypeByte4 != 0x00) {
                    continue;
                }

                byte midByte1 = receiveBuf[8];
                byte midByte2 = receiveBuf[9];
                String satelliteName = ConfigUtils.getSatelliteNameByMid(midByte1, midByte2);
                if (satelliteName == null) {
                    continue;
                }
                if (bytes[0] == 0) {
                    if (this.satelliteReceiveDateTime.containsKey(satelliteName)
                            && this.satelliteReceiveDateTime.get(satelliteName) != null
                            && Duration.between(LocalDateTime.now(), this.satelliteReceiveDateTime.get(satelliteName)).toSeconds() < 2
                    ) {
                        break;
                    }
                } else {
                    this.satelliteReceiveDateTime.put(satelliteName, LocalTime.now());
                }
                if(parseTelemetryMap==null){
                    parseTelemetryMap=new HashMap<>();
                }
                if (!parseTelemetryMap.containsKey(satelliteName)) {
                    parseTelemetryMap.put(satelliteName, new ParseTelemetry(satelliteName,this.stationName,this.stationId));
                }
                parseTelemetryMap.get(satelliteName).enQueue(receiveBuf);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // throw new RuntimeException(e);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    private void transferBDNormalData() {
        while (true) {
            try {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                System.out.println(stationName + "启动了transfer线程");
                byte[] bytes = this.queue.take();
                String receiveStr = StringConvertUtils.bytesConvertToASCIIStr(bytes);
                if (!ParseBDUtils.bdMsgValidate(receiveStr)) {
                    continue;
                }
                String[] receiveStrArray = receiveStr.split(",");
                String bdICard = receiveStrArray[2];
                String satelliteName = ConfigUtils.getSatelliteNameByIcCard(bdICard);
                if (satelliteName == null) {
                    continue;
                }
                String dataContent = receiveStrArray[5].split("\\*")[0];

                if (!ParseBDUtils.validateBDMsgHeader(dataContent)) {
                    continue;
                }
                if (!parseTelemetryMap.containsKey(satelliteName)) {
                    parseTelemetryMap.put(satelliteName, new ParseTelemetry(satelliteName,this.stationName,this.stationId));
                }
                parseTelemetryMap.get(satelliteName).enQueue(bytes);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }

    public void stop() {
        if(this.transferThread!=null){
            this.transferThread.interrupt();
        }
        this.transferThread=null;

    }
}
