package org.sipana.server.web.sip.scenario;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.sipana.protocol.sip.SIPMessage;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.sip.SIPSessionManager;
import org.sipana.sip.scenario.SIPScenario;

public class SIPScenarioLogic {

    private List<SIPMessage> messageList;

    //list all SIP sessions
    private String[] callId = null;
    private String[] sessionId = null;

    private SIPSessionManager sipSessionManager;

    public SIPScenarioLogic() {
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        sipSessionManager = (SIPSessionManager) serviceLocator.getService(Service.SIP_SESSION_MANAGER);
    }

    public void show() throws Exception {
        if (sessionId != null) {

            List<Long> items = new ArrayList<Long>();

            for (String id : sessionId) {
                items.add(Long.parseLong(id));
            }

            if (items.size() > 0) {
                messageList = sipSessionManager.getMessageListBySessionId(items);
            }
        } else if (callId != null) {

            List<String> items = new ArrayList<String>();

            for (String id : callId) {
                items.add(id);
            }

            if (items.size() > 0) {
                messageList = sipSessionManager.getMessageListByCallID(items);
            }
        }

        if (messageList != null) {

            HttpServletResponse response = 
                (HttpServletResponse) FacesContext.getCurrentInstance()
                                            .getExternalContext().getResponse();

            OutputStream outputStream = response.getOutputStream();
            SIPScenario scenario = new SIPScenario(messageList);
            scenario.create(outputStream);

            FacesContext.getCurrentInstance().responseComplete();
        }

    }

    public void setSessionId(String[] sessionId) {
        this.sessionId = sessionId;
    }

    public String[] getSessionId() {
        return sessionId;
    }

    public void setCallId(String[] callId) {
        this.callId = callId;
    }

    public String[] getCallId() {
        return callId;
    }
}
