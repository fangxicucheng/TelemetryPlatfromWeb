package com.fang.webSocket;

import com.fang.config.contextConfig.SpringUtil;
import com.fang.service.replayService.ReplayService;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/telemetry")
public class TelemetryWebSocketServer  {
    private static final CopyOnWriteArraySet<TelemetryWebSocketServer> WEB_SOCKET_SERVER_SET = new CopyOnWriteArraySet<>();
    private Session session;
    // 全局静态变量，保存 ApplicationContext

    private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<String, Session>();
    private ReplayService replayService;

    @OnOpen
    public void onOpen(Session session) {

        this.session = session;
        WEB_SOCKET_SERVER_SET.add(this);
        this.replayService  = SpringUtil.getBean(ReplayService.class);
        log.info("接入了新的server");

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("【websocket消息】收到客户端消息:" + message);
        try {
            String[] messageArray = message.split(":");

            String telemetryFrameModel = this.replayService.getTelemetryFrameModel(Integer.parseInt(messageArray[1]), Integer.parseInt(messageArray[0]));
            session.getBasicRemote().sendText(telemetryFrameModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose() {
        try {
            WEB_SOCKET_SERVER_SET.remove(this);
            log.info("移除了wesocket！");

        } catch (Exception e) {
        }
    }


}
