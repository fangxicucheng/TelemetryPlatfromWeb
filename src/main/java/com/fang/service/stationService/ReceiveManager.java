package com.fang.service.stationService;

import com.fang.database.postgresql.entity.StationInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.LinkedBlockingDeque;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveManager {
    private DataRecvie dataRecvie;
    private DataTransfer dataTranfer;
    private String stationName;
    private String stationId;
    private LinkedBlockingDeque<byte[]>receiveBlockQueue;


    public ReceiveManager(StationInfo satetionInof) {
        this.stationName=satetionInof.getStationName();
        this.stationId=satetionInof.getStationId();
        this.receiveBlockQueue=new LinkedBlockingDeque<>();
        this.dataRecvie=new DataRecvie(satetionInof,receiveBlockQueue);

    }
}
