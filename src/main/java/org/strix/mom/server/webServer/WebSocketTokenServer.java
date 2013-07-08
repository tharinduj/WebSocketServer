package org.strix.mom.server.webServer;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
import org.strix.mom.server.message.MessageProcessor;
import org.strix.mom.server.message.ServerMessage;
import org.strix.mom.server.message.file.FileHandler;
import org.strix.mom.server.communication.impl.UdpServer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.DatagramPacket;
import java.util.Date;


/**
 * @author tharinduj
 */
public class WebSocketTokenServer implements WebSocketServerTokenListener, UdpServer.Listener, PropertyChangeListener {
    private String resourcePath;
    private TokenServer tokenServer;
    private FileHandler fileHandler;
    private ApplicationClientManager applicationClientManager;
    private MessageProcessor messageProcessor;

    public TokenServer getTokenServer() {

        return tokenServer;
    }

    public void init() {
        try {
//            System.setProperty(JWebSocketServerConstants.JWEBSOCKET_HOME,
//                    System.getenv(JWebSocketServerConstants.JWEBSOCKET_HOME));

            System.setProperty(JWebSocketServerConstants.JWEBSOCKET_HOME,
                    resourcePath);

            //JWebSocketFactory.printCopyrightToConsole();
            // the following line must not be removed due to GNU LGPL 3.0 license!
// check if home, config or bootstrap path are passed by command line
            JWebSocketConfig.initForConsoleApp(null);
//start the jWebSocket Server
            JWebSocketFactory.start();
            tokenServer = (TokenServer) JWebSocketFactory.getServer("ts0");
            if (tokenServer != null) {
                System.out.println("Web Socket server was created");
                tokenServer.addListener(this);
            } else {
                System.out.println("server was NOT found");
            }
        } catch (Exception lEx) {
            lEx.printStackTrace();
        }
    }

    public void processToken(WebSocketServerTokenEvent serverTokenEvent, Token token) {
//        System.out.println("temp.JwebSockClient.processToken"+token);
    }

    public void processClosed(WebSocketServerEvent event) {
        System.out.println("SockServer.processClosed" + event);
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
//        applicationClient.setStatus(event.getConnector().getStatus().getStatus());
        applicationClient.setAlive(event.getConnector().getEngine().isAlive());
        applicationClient.setUserName(event.getConnector().getUsername());
        applicationClient.setRemoteHostName(event.getConnector().getRemoteHost().toString());
        applicationClient.setRemoteHostPort(event.getConnector().getRemotePort());
        applicationClient.setWebSocketConnector(event.getConnector());
        applicationClientManager.addApplicationClient(applicationClient);
    }

    /*public void sendPacket(int slideNumber) {
        Map lConnectorMap = getTokenServer().getAllConnectors();

        Collection<WebSocketConnector> lConnectors = lConnectorMap.values();
        for (WebSocketConnector wsc : lConnectors) {
            String json = "{\"action\":\"slide\",\"uniqueId\":123,\"slideNumber\":" + slideNumber + "}";
            WebSocketPacket wsPacket = new RawPacket(json);
            getTokenServer().sendPacket(wsc, wsPacket);            
        }
    }*/

    public void processPacket(WebSocketServerEvent event, WebSocketPacket packet) {
        System.out.println("packet received " + packet.getString());
        ApplicationClient client = applicationClientManager.getApplicationClient(event.getConnector().getId());
//        System.out.println("Connected clients"+applicationClientManager.getApplicationClients().size());
        client.setLastMessageReceived(new Date(System.currentTimeMillis()));
//        System.out.println("Message From " + client);
        ServerMessage replyMessage = messageProcessor.processMessage(packet.getString());
        if (replyMessage.isSentReply()) {
            WebSocketPacket wsPacket = new RawPacket(replyMessage.getResponseData());
            System.out.println("WebSocketTokenServer.sendpacket" + replyMessage.getResponseData());
            getTokenServer().sendPacket(client.getWebSocketConnector(), wsPacket);
        }
    }

    @Override
    public void packetReceived(UdpServer.Event evt) {
        DatagramPacket packet = evt.getUdpServer().getPacket(); // Not actually using this here.
        final String message = evt.getPacketAsString();
        //System.out.println(evt.getUdpServer().getType() + "UdpServer.Event " + message);

        switch (evt.getUdpServer().getType()){
            case FILE:
                fileHandler.processFrame(evt);
                break;
            case STREAM:

                break;
            case TEXT:

                break;
            case COMMANDS:

                break;
            default:
                break;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String prop = evt.getPropertyName();
        final Object oldVal = evt.getOldValue();
        final Object newVal = evt.getNewValue();
//        System.out.println("Property: " + prop + ", Old: " + oldVal + ", New: " + newVal );

        if (UdpServer.STATE_PROP.equals(prop)) {
            final UdpServer.State state = (UdpServer.State) newVal;
            switch (state) {
                case STARTING:
//                                    stateLabel.setText("Starting");
//                                    startStopButton.setEnabled(false);
                    break;
                case STARTED:
//                                    stateLabel.setText("Started");
//                                    startStopButton.setText("Stop");
//                                    startStopButton.setEnabled(true);
                    break;
                case STOPPING:
//                                    stateLabel.setText("Stopping");
//                                    startStopButton.setEnabled(false);
                    break;
                case STOPPED:
//                                    stateLabel.setText("Stopped");
//                                    startStopButton.setText("Start");
//                                    startStopButton.setEnabled(true);
                    break;
                default:
                    assert false : state;
                    break;
            }   // end switch
        }

        if (UdpServer.PORT_PROP.equals(evt.getPropertyName())) {


        } else if (UdpServer.GROUPS_PROP.equals(evt.getPropertyName())) {

        }
    }

    /*public static void main(String[] args) {

        WebSocketTokenServer jc = new WebSocketTokenServer();
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
                Logger.getLogger(WebSocketTokenServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }*/

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public void setFileHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public ApplicationClientManager getApplicationClientManager() {
        return applicationClientManager;
    }

    public void setApplicationClientManager(ApplicationClientManager applicationClientManager) {
        this.applicationClientManager = applicationClientManager;
    }

    public MessageProcessor getMessageProcessor() {
        return messageProcessor;
    }

    public void setMessageProcessor(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }
}
