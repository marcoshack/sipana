package org.sipana.server.web.sip.scenario;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.sipana.protocol.sip.SIPMessage;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.sip.SIPSessionManager;
import org.sipana.sip.scenario.SIPScenario;

public class SIPScenarioLogic {

    private HttpServletResponse response;

    private List<SIPMessage> messageList;
    
    private String call_id = null;
    
    private String[] sessionId = null;
    
    private SIPSessionManager sipSessionManager;
    
    public SIPScenarioLogic() {
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        sipSessionManager = (SIPSessionManager) serviceLocator.getService(Service.SIP_SESSION_MANAGER);
    }
    
    public void setSessionId(String[] sessionId) {
        this.sessionId = sessionId;
    }
}
