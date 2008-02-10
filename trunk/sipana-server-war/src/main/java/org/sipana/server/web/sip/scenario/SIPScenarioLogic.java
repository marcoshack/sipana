package org.sipana.server.web.sip.scenario;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.sipana.protocol.sip.SIPMessage;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.sip.SIPSessionManager;
import org.sipana.sip.scenario.SIPScenario;
import org.vraptor.annotations.Component;
import org.vraptor.annotations.In;
import org.vraptor.annotations.Out;
import org.vraptor.annotations.Parameter;
import org.vraptor.annotations.Viewless;

@Component
public class SIPScenarioLogic {
    @In
    private HttpServletResponse response;

    @Out
    private List<SIPMessage> messageList;
    
    @Parameter
    private String call_id_list = null;
    
    @Parameter
    private long session_id = 0;
    
    private SIPSessionManager sipSessionManager;
    
    public SIPScenarioLogic() {
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        sipSessionManager = (SIPSessionManager) serviceLocator.getService(Service.SIP_SESSION_MANAGER);
    }
    
    @Viewless
    public void show() throws Exception {
        if (session_id != 0) {
            messageList = sipSessionManager.getMessageListBySessionId(session_id);
        } else if (call_id_list != null) {
            String items[] = call_id_list.split(",");
            messageList = sipSessionManager.getMessageListByCallID(items);
        }

        if (messageList != null) {
            OutputStream outputStream = response.getOutputStream();
            SIPScenario.createSIPScenario(messageList, outputStream);
        }
    }
}
