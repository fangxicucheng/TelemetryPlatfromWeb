package com.fang.service.stationService;

import com.fang.database.postgresql.entity.StationInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataReceive {
    private String stationName;
    private String stationId;
    HashMap<String, SocketManager> waveSocketMap;
    private String waveInfo;
    private LinkedBlockingQueue<byte[]> receiveBlockQueue;
    private String localIp;

    public DataReceive(StationInfo stationInfo, LinkedBlockingQueue<byte[]> receiveBlockQueue) {
        this.receiveBlockQueue = receiveBlockQueue;
        this.stationId = stationInfo.getStationId();
        this.stationName = stationInfo.getStationName();
        this.waveInfo = stationInfo.getWaveInfo();
        this.localIp = stationInfo.getServerIp();
        initSocketMap();
    }

    public void initSocketMap() {

        if(this.waveInfo==null||this.waveInfo.isEmpty()){
            return;
        }
        String[] socketContentArray = this.waveInfo.split(";");
        for (String socketContent : socketContentArray) {

            String[] waveContentArray = socketContent.split(":");
            String waveName=waveContentArray[0];
            String[] socketInfoArray = waveContentArray[1].split(",");
            String ip=socketInfoArray[0];
            int port=Integer.parseInt(socketInfoArray[1]);
            SocketManager socketManager = new SocketManager(ip, port,this.localIp, this.stationName, waveName, this.receiveBlockQueue);
            if(this.waveSocketMap==null){
                this.waveSocketMap=new HashMap<>();
            }
            this.waveSocketMap.put(waveName,socketManager);
        }
    }


}
