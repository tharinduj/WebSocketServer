package org.strix.mom.server.client;

import org.jwebsocket.api.WebSocketConnector;
import org.strix.mom.server.message.MessageProcessor;
import org.strix.mom.server.message.ServerMessage;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/21/13
 * Time: 11:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationClient {
    public static final int STATUS_UP = 1;
    public static final int STATUS_DOWN = 0;
    private String uid = null;
    private String id = null;
    private int status = STATUS_DOWN;
    private boolean isAlive = false;
    private String userName = null;
    private String remoteHostName = null;
    private int remoteHostPort = 0;
    private Date lastMessageReceived = null;
    private WebSocketConnector webSocketConnector = null;
    private MessageProcessor messageProcessor;

    public ApplicationClient() {
        messageProcessor = new MessageProcessor();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRemoteHostName() {
        return remoteHostName;
    }

    public void setRemoteHostName(String remoteHostName) {
        this.remoteHostName = remoteHostName;
    }

    public int getRemoteHostPort() {
        return remoteHostPort;
    }

    public void setRemoteHostPort(int remoteHostPort) {
        this.remoteHostPort = remoteHostPort;
    }

    public Date getLastMessageReceived() {
        return lastMessageReceived;
    }

    public void setLastMessageReceived(Date lastMessageReceived) {
        this.lastMessageReceived = lastMessageReceived;
    }

    public WebSocketConnector getWebSocketConnector() {
        return webSocketConnector;
    }

    public void setWebSocketConnector(WebSocketConnector webSocketConnector) {
        this.webSocketConnector = webSocketConnector;
    }

    @Override
    public String toString() {
        return "org.strix.mom.server.client.ApplicationClient{" +
                "uid='" + uid + '\'' +
                ", id='" + id + '\'' +
                ", status=" + status +
                ", isAlive=" + isAlive +
                ", userName='" + userName + '\'' +
                ", remoteHostName='" + remoteHostName + '\'' +
                ", remoteHostPort=" + remoteHostPort +
                '}';
    }


}
