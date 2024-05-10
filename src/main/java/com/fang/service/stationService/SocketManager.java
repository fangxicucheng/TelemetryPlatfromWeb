package com.fang.service.stationService;

import lombok.Data;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

@Data
public class SocketManager {
    private String ip;
    private int port;
    private String localIp;
    private MulticastSocket socket;
    private String stationName;
    private String waveName;
    private LinkedBlockingQueue<byte[]> queue;
    private Thread receiveThread;

    public SocketManager(String ip, int port, String stationName, String waveName, LinkedBlockingQueue<byte[]> queue) {
        this.ip = ip;
        this.port = port;
        this.stationName = stationName;
        this.waveName = waveName;
        this.queue = queue;
        start();
    }

    public void start() {

        if(initSocket()){
            this.receiveThread=new Thread(()->startThread());
            this.receiveThread.start();
        }

    }


    public boolean initSocket() {
        boolean result = true;
        try {
            NetworkInterface localNetWorkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName(this.localIp));
            InetAddress group = InetAddress.getByName(this.ip);
            this.socket = new MulticastSocket(this.port);
            this.socket.setNetworkInterface(localNetWorkInterface);
            this.socket.joinGroup(group);
        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.out.println(this.stationName + " :" + this.ip + "," + this.port + "启动失败");
            e.printStackTrace();
            result = false;
        }
        return result;
    }


    public void startThread() {
        while(!Thread.interrupted()){
            try{
                byte[]buffer=new byte[1024];
                DatagramPacket packet=new DatagramPacket(buffer, buffer.length);
                System.out.println(stationName + ":" + this.ip + "," + this.port + "启动成功");
                this.socket.receive(packet);
                byte[] receiveBytes = Arrays.copyOfRange(packet.getData(), 0, packet.getLength() - 1);
                this.queue.add(receiveBytes);
                if(Thread.interrupted()){
                    break;
                }
            }
            catch(IOException ioE){
                if(Thread.interrupted()){
                    continue;
                }else{
                    ioE.printStackTrace();
                }
            }

        }

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
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.socket = null;
        }
    }
}
