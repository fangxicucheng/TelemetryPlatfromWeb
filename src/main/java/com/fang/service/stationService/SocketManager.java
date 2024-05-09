package com.fang.service.stationService;

import lombok.Data;

import java.net.MulticastSocket;
import java.util.concurrent.LinkedBlockingDeque;

@Data
public class SocketManager {
    private String ip;
    private int port;
    private String localIp;
    private MulticastSocket multicastSocket;
    private String stationName;
    private String waveName;
    private LinkedBlockingDeque<byte[]> queue;
    private Thread receiveThread;

    public SocketManager(String ip, int port, String stationName, String waveName, LinkedBlockingDeque<byte[]> queue) {
        this.ip = ip;
        this.port = port;
        this.stationName = stationName;
        this.waveName = waveName;
        this.queue = queue;
    }

    public void start() {

    }


    public void initSocket() {


    }


    public void startThread(){

    }
    public void stop() {
        stopThread();
        stopSocket();
    }

    public void stopThread() {
        try {
            if (receiveThread != null) {
                receiveThread.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            receiveThread = null;
        }
    }

    public void stopSocket() {
        try {
            if (this.multicastSocket != null) {
                this.multicastSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.multicastSocket = null;
        }
    }
}
