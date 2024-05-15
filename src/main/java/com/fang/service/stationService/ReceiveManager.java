package com.fang.service.stationService;

import com.fang.database.postgresql.entity.StationInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveManager {
    private DataReceive dataReceive;
    private DataTransfer dataTransfer;
    private String stationName;
    private String stationId;
    //接收站的接收参数
    private LinkedBlockingQueue<byte[]> receiveBlockQueue;
    public ReceiveManager(StationInfo stationInfo) {
        this.stationName=stationInfo.getStationName();
        this.stationId=stationInfo.getStationId();
        this.receiveBlockQueue=new LinkedBlockingQueue<>();
        this.dataReceive=new DataReceive(stationInfo,receiveBlockQueue);
        this.dataTransfer=new DataTransfer(stationInfo.getStationName(),stationInfo.getStationId(),receiveBlockQueue);
    }
}
