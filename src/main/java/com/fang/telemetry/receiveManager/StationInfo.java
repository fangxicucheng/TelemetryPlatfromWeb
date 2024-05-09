package com.fang.telemetry.receiveManager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data

@AllArgsConstructor
public class StationInfo {

    private String stationName;
    private Map<String,SocketInfo>waveSocketInfo;

    public StationInfo() {
        this.waveSocketInfo=new HashMap<>();
    }
}
