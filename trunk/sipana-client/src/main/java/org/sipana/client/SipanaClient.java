package org.sipana.client;

import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPMessageList;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public interface SipanaClient {
    public void send(SIPMessage message);
    public void send(SIPMessageList messages);
}
