
package org.sipana.agent.sender;

import java.util.List;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPSession;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public interface Sender {
    
    public void send(SIPMessage message) throws Exception;

    public void send(List<SIPMessage> messages) throws Exception;

    @Deprecated
    public void send(SIPSession session) throws Exception;

    public void start() throws Exception;

    public void stop() throws Exception;
}
