package org.sipana.protocol.sip.test;

import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.junit.Before;
import org.junit.Test;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPMessageList;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPResponse;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class SIPMessageJAXBTest {

    private Logger logger = Logger.getLogger(SIPMessageJAXBTest.class.getName());
    private SIPRequest req;
    private SIPResponse res;
    private SIPMessageList list;

    @Before
    public void init() {
        req = new SIPRequest();
        req.setFromUser("fromuser@example.com");
        req.setToUser("touser@example.com");
        req.setMethod("REGISTER");
        req.setCallID("callId-1");

        res = new SIPResponse();
        res.setFromUser("fromuser@example.com");
        res.setToUser("touser@example.com");
        res.setReasonPhrase("OK");
        res.setStatusCode(200);
        res.setCallID("callId-1");

        list = new SIPMessageList();
        list.add(req);
        list.add(res);
    }

    @Test
    public void marchal() throws Exception {
        JAXBContext context = JAXBContext.newInstance(SIPMessageList.class, SIPMessage.class, SIPRequest.class, SIPResponse.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
        marshaller.marshal(list, System.out);
    }
}
