package org.strix.mom.server;

import org.strix.mom.server.webServer.WebSocketServer;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 7/4/13
 * Time: 9:12 AM
 */
public class MomServer {
    public static void main(String[] args) {
        WebSocketServer jc = new WebSocketServer();
        jc.init();
    }
}
