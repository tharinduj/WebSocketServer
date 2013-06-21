package org.strix.mom.server.message;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/21/13
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageProcessor {
    private static MessageProcessor messageProcessor = new MessageProcessor();

    private MessageProcessor() {
    }

    public static MessageProcessor getInstance() {
        return messageProcessor;
    }

    /**
     * Process messages from the client
     * @param string
     */
    public static ProtocolMessage processMessage(String string) {
        return null;
    }
}
