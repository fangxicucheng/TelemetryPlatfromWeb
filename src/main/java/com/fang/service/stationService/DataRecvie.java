package com.fang.service.stationService;

import com.fang.database.postgresql.entity.StationInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingDeque;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRecvie {
    private String stationName;
    private String stationId;
    HashMap<String, SocketManager> waveSocketMap;

    private String waveInfo;
    private LinkedBlockingDeque<byte[]> receiveBlockQueue;
    private String localIp;

    public DataRecvie(StationInfo stationInfo, LinkedBlockingDeque<byte[]> receiveBlockQueue) {
        this.receiveBlockQueue = receiveBlockQueue;
        this.stationId = stationInfo.getStationId();
        this.stationName = stationInfo.getStationName();
        this.waveInfo = stationInfo.getWaveInfo();
        this.localIp = stationInfo.getServerIp();
    }

    public void start() {


    }


}
