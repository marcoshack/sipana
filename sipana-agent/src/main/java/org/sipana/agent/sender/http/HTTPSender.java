/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sipana.agent.sender.http;

import java.util.List;
import org.sipana.agent.sender.Sender;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPMessageList;
import org.sipana.protocol.sip.SIPSession;

/**
 *
 * @author mhack
 */
public class HTTPSender implements Sender {

    public void send(SIPMessage message) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void send(List<SIPMessage> messages) throws Exception {
        SIPMessageList list = new SIPMessageList(messages);

    }

    public void send(SIPSession session) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void start() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stop() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
