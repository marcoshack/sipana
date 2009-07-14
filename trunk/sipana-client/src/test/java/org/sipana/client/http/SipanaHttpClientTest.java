package org.sipana.client.http;

import org.junit.Test;
import org.sipana.client.SipanaClient;
import org.sipana.protocol.sip.SIPRequest;

/**
 *
 * @author mhack
 */
public class SipanaHttpClientTest {

    @Test
    public void createClient() {
        SipanaClient client = SipanaHttpClientFactory.createSipanaClient();

        SIPRequest req = new SIPRequest();
        req.setMethod("INVITE");
        req.setCallID("testcallid");
        
        client.send(req);
    }
}
