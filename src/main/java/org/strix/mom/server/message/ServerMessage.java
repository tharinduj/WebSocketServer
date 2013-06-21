package org.strix.mom.server.message;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/21/13
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerMessage {
    private String requestData;
    private boolean sentReply;
    private String channel;
    private String responseData;

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public boolean isSentReply() {
        return sentReply;
    }

    public void setSentReply(boolean sentReply) {
        this.sentReply = sentReply;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }
}
