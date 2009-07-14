package org.sipana.client.http;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.sipana.client.SipanaClient;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPMessageList;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public interface SipanaHttpClient extends SipanaClient {

    @POST
    @Path("/sipmessages")
    @Consumes("application/xml")
    public void send(SIPMessage message);

    @POST
    @Path("/sipmessages")
    @Consumes("application/xml")
    public void send(SIPMessageList messages);
}
