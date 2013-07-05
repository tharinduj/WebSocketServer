package org.strix.mom.server.message.api;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/21/13
 * Time: 9:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MessageHandler {
    Message parseMessage(String string);

    String getMessage(Message message);
}
