package org.strix.mom.server.message.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.strix.mom.server.message.api.Message;
import org.strix.mom.server.message.api.MessageHandler;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/21/13
 * Time: 8:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonMessageHandler implements MessageHandler {
    private static Gson gson = new Gson();

    /**
     * Parse messages from the client
     * @param string
     */
    @Override
    public Message parseMessage(String string) {
        JsonMessage request = null;
        try {
            request = gson.fromJson(string, JsonMessage.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }

    /**
     * Get Json messages from the object
     * @param message
     */
    @Override
    public String getMessage(Message message) {
        String json = gson.toJson(message);
        return json;
    }
}
