package org.strix.mom.server.message;

import org.strix.mom.server.message.api.Message;
import org.strix.mom.server.message.api.MessageHandler;
import org.strix.mom.server.message.file.FileHandler;
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
    private FileHandler fileHandler = null;

    /*public MessageProcessor() {
        messageHandler = new JsonMessageHandler();
        fileHandler = new FileHandler();
    }*/

    /**
     * Process messages from the client
     * @param string
     */
    public ServerMessage processMessage(String string) {
        Message message =  messageHandler.parseMessage(string);
        ServerMessage serverMessage = new ServerMessage();
        if(message!=null){

            if(message.getType()!=null && message.getType().equalsIgnoreCase("getDirectoryListing")){
                message.setData(fileHandler.getDirectoryListing());
                message.setAction("getDirectoryListing");
            }
            System.out.println("message"+message);
            String jsonResponse = messageHandler.getMessage(message);
            serverMessage.setSentReply(true);
            serverMessage.setResponseData(jsonResponse);
        }
        return serverMessage;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public void setFileHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }
}
