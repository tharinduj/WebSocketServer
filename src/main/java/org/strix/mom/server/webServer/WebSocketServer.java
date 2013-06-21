package org.strix.mom.server.webServer;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.jwebsocket.api.WebSocketConnector;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.config.JWebSocketConfig;
import org.jwebsocket.config.JWebSocketServerConstants;
import org.jwebsocket.factory.JWebSocketFactory;
import org.jwebsocket.kit.RawPacket;
import org.jwebsocket.kit.WebSocketServerEvent;
import org.jwebsocket.listener.WebSocketServerTokenEvent;
import org.jwebsocket.listener.WebSocketServerTokenListener;
import org.jwebsocket.server.TokenServer;
import org.jwebsocket.token.Token;
import org.strix.mom.server.client.ApplicationClient;
import org.strix.mom.server.message.ProtocolMessage;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tharinduj
 */
public class WebSocketServer implements WebSocketServerTokenListener {

    private TokenServer tokenServer;
    ApplicationClientManager applicationClientManager;
    public TokenServer getTokenServer() {

        return tokenServer;
    }

    public void init() {
        try {
//            System.setProperty(JWebSocketServerConstants.JWEBSOCKET_HOME,
//                    System.getenv(JWebSocketServerConstants.JWEBSOCKET_HOME));

            System.setProperty(JWebSocketServerConstants.JWEBSOCKET_HOME,
                    "G:\\Strix\\MyjWebSocketJavaClient\\WebSocketServer\\src\\main\\resources");

            //JWebSocketFactory.printCopyrightToConsole();
            // the following line must not be removed due to GNU LGPL 3.0 license!
// check if home, config or bootstrap path are passed by command line
            JWebSocketConfig.initForConsoleApp(null);
//start the jWebSocket Server
            JWebSocketFactory.start();
            tokenServer = (TokenServer) JWebSocketFactory.getServer("ts0");
            if (tokenServer != null) {
                System.out.println("server was found");
                tokenServer.addListener(this);
            } else {
                System.out.println("server was NOT found");
            }
            applicationClientManager = ApplicationClientManager.getInstance();
        } catch (Exception lEx) {
            lEx.printStackTrace();
        }
    }

    public void processToken(WebSocketServerTokenEvent serverTokenEvent, Token token) {
//        System.out.println("JwebSockClient.processToken"+token);
    }

    public void processClosed(WebSocketServerEvent event) {
        System.out.println("JwebSockClient.processClosed"+event);
        ApplicationClient applicationClient = new ApplicationClient();
        applicationClient.setId(event.getConnector().getId());
        applicationClientManager.removeApplicationClient(applicationClient.getId());
    }

    public void processOpened(WebSocketServerEvent event) {
        System.out.println("***********org.strix.mom.server.client.ApplicationClient '" + event.getSessionId()
                + "' connected.*********");

        ApplicationClient applicationClient = new ApplicationClient();
        applicationClient.setUid(event.getConnector().generateUID());
        applicationClient.setId(event.getConnector().getId());
        applicationClient.setStatus(event.getConnector().getStatus().getStatus());
        applicationClient.setAlive(event.getConnector().getEngine().isAlive());
        applicationClient.setUserName(event.getConnector().getUsername());
        applicationClient.setRemoteHostName(event.getConnector().getRemoteHost().toString());
        applicationClient.setRemoteHostPort(event.getConnector().getRemotePort());
        applicationClient.setWebSocketConnector(event.getConnector());
        applicationClientManager.addApplicationClient(applicationClient);
    }

    public void sendPacket(int slideNumber) {
        Map lConnectorMap = getTokenServer().getAllConnectors();

        Collection<WebSocketConnector> lConnectors = lConnectorMap.values();
        for (WebSocketConnector wsc : lConnectors) {
            String json = "{\"action\":\"slide\",\"uniqueId\":123,\"slideNumber\":" + slideNumber + "}";
            WebSocketPacket wsPacket = new RawPacket(json);
            getTokenServer().sendPacket(wsc, wsPacket);            
        }
    }

    public void processPacket(WebSocketServerEvent event, WebSocketPacket packet) {
        System.out.println("packet received " + packet.getString());
        ApplicationClient client = applicationClientManager.getApplicationClient(event.getConnector().getId());
        System.out.println("Connected clients"+applicationClientManager.getApplicationClients().size());
        client.setLastMessageReceived(new Date(System.currentTimeMillis()));
        System.out.println("Message From " + client);
        ProtocolMessage replyMessage = client.processMessage(packet.getString());
        if(replyMessage.isSentReply()){
            String json = "{\"action\":\"slide\",\"uniqueId\":123,\"slideNumber\":" + 1 + "}";
            WebSocketPacket wsPacket = new RawPacket(json);
            getTokenServer().sendPacket(client.getWebSocketConnector(), wsPacket);
        }
    }

    public static void main(String[] args) {

        WebSocketServer jc = new WebSocketServer();
        jc.init();
        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(3000);
                Object c = jc.getTokenServer().getAllConnectors();
//                System.out.println("C "+c);

                Map lConnectorMap = jc.getTokenServer().getAllConnectors();
                List<Map> lResultList = new ArrayList<Map>();
                Collection<WebSocketConnector> lConnectors = lConnectorMap.values();
                for (WebSocketConnector lConnector : lConnectors) {
                    Map lResultItem = new HashMap<String, Object>();
                    lResultItem.put("port", lConnector.getRemotePort());
                    lResultItem.put("unid", lConnector.getNodeId());
                    lResultItem.put("username", lConnector.getUsername());
                    lResultItem.put("isToken", true);
                    lResultList.add(lResultItem);
                }
                for (Map m : lResultList) {
//                    System.out.println("m "+m);
                }

                jc.sendPacket(i % 5 + 1);
            } catch (InterruptedException ex) {
                Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
