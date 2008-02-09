package org.sipana.server.web;

import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.impl.SIPSessionImpl;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.service.ServiceLocatorException;
import org.sipana.server.sip.SIPSessionManager;
import org.sipana.sip.scenario.SIPScenario;
import org.vraptor.annotations.Component;
import org.vraptor.annotations.In;
import org.vraptor.annotations.Out;
import org.vraptor.annotations.Parameter;

@Component
public class SIPSessionLogic {

    @In
    private HttpServletResponse response;

    @Out
    private List<SIPSessionImpl> sipSessions = null;

    @Out
    private List<SIPMessage> sipMessages = null;

    @Parameter
    private long id = 0;

    @Parameter
    private String call_id_list = null;

    private SIPSessionManager sipSessionManager;

    SIPSessionLogic() throws ServiceLocatorException {
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        sipSessionManager = (SIPSessionManager) serviceLocator.getService(Service.SIP_SESSION_MANAGER);
    }

    public void list() throws Exception {
        long timeNow = Calendar.getInstance().getTimeInMillis();
        this.sipSessions = sipSessionManager.getSIPSessions(0, timeNow);
    }

    public void show() throws Exception {
        if (id != 0) {
            sipMessages = sipSessionManager.getMessageListBySessionId(id);
        } else if (call_id_list != null) {
            String items[] = call_id_list.split(",");
            sipMessages = sipSessionManager.getMessageListByCallID(items);
        }

        if (sipMessages != null) {
            OutputStream outputStream = response.getOutputStream();
            SIPScenario.createSIPScenario(sipMessages, outputStream);
        }
    }
}
