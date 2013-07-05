package org.strix.mom.server.message;

import org.strix.mom.server.message.api.Message;
import org.strix.mom.server.message.api.MessageHandler;
import org.strix.mom.server.message.json.JsonMessageHandler;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/21/13
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageProcessor {
    private MessageHandler messageHandler = null;

    public MessageProcessor() {
        messageHandler = new JsonMessageHandler();
    }

    /**
     * Process messages from the client
     * @param string
     */
    public ServerMessage processMessage(String string) {
        Message message =  messageHandler.parseMessage(string);
        ServerMessage serverMessage = new ServerMessage();
        if(message!=null){
            System.out.println("message"+message);
            String jsonResponse = messageHandler.getMessage(message);
            serverMessage.setSentReply(true);
            serverMessage.setResponseData(jsonResponse);
        }
        return serverMessage;
    }
}
