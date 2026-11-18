package com.cube001.cubeboot.websocket;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: DataWebSocket
 * DataWebSocket
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/2/21 11:26
 */
@ServerEndpoint(value = "/websocket/{keyword}")
@Component
public class WebSocket {
    private final static Logger logger= LoggerFactory.getLogger(WebSocket.class);
    /** 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineNumber = 0;
    /** 以keyword为key，WebSocket为对象保存起来 */
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
    /** 会话*/
    private Session session;
    /** 用户名称*/
    private String keyword;

    /**
     * 建立连接
     * @param keyword
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("keyword") String keyword, Session session){
        onlineNumber++;
        logger.info("现在来连接大屏id：" + session.getId() + "，大屏编号：" + keyword);
        this.session = session;
        this.keyword = keyword;
        logger.info("有新连接加入！ 当前在线大屏" + onlineNumber);
        clients.put(keyword, this);
    }

    /**
     * 服务端错误
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("服务端发生了错误" + error.getMessage());
    }
    /**
     * 连接关闭
     */
    @OnClose
    public void onClose()
    {
        onlineNumber--;
        clients.remove(this.keyword);
        logger.info("有大屏连接关闭！ 当前在线大屏" + onlineNumber);
    }

    /**
     * 执行操作
     * @param type up-"上移" left-"左移" right-"右移" down-"下移" ok-"确认" back-"返回"
     * @param keyword 接收大屏
     * @throws Exception
     */
    public static void sendOperation(String type,String keyword) throws Exception {
        for (WebSocket item : clients.values()) {
            if (item.keyword.equals(keyword) ) {
                item.session.getAsyncRemote().sendText(type);
                break;
            }
        }
    }

    /**
     * 判断大屏是否存在
     * @param keyword 大屏编号
     * @return
     */
    public static boolean getWebSocket(String keyword){
        for (WebSocket item : clients.values()) {
            if (item.keyword.equals(keyword) ) {
               return true;
            }
        }
        return false;
    }
}
