package org.sipana.protocol.sip.test;

import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.junit.Before;
import org.junit.Test;
import org.sipana.protocol.sip.SIPMessageList;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPResponse;
import org.sipana.protocol.sip.SIPSession;

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
        SIPSession session = new SIPSession();
        session.setId(123);

        req = new SIPRequest();
        req.setId(1);
        req.setFromUser("fromuser@example.com");
        req.setToUser("touser@example.com");
        req.setMethod("REGISTER");
        req.setCallID("123456@127.0.0.1");
        req.setSipSession(session);
        session.addMessage(req);

        res = new SIPResponse();
        res.setId(2);
        res.setFromUser("fromuser@example.com");
        res.setToUser("touser@example.com");
        res.setReasonPhrase("OK");
        res.setStatusCode(200);
        res.setCallID("callId-1");
        res.setSipSession(session);
        session.addMessage(res);

        list = new SIPMessageList();
        list.add(req);
        list.add(res);
    }

    @Test
    public void marchal() throws Exception {
        JAXBContext context = JAXBContext.newInstance(SIPMessageList.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
        marshaller.marshal(list, System.out);
    }
}
