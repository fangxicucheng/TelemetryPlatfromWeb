package com.fang.telemetry.receiveManager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 *
 *
 *
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketInfo {
    private String socketWaveName;
    private String ip;
    private String port;
    private String localIp;
}
