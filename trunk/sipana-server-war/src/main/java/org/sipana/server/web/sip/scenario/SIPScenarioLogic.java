package org.sipana.server.web.sip.scenario;

import java.io.OutputStream;
import java.util.ArrayList;
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
    private String call_id = null;
    
    @Parameter
    private String session_id = null;
    
    private SIPSessionManager sipSessionManager;
    
    public SIPScenarioLogic() {
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        sipSessionManager = (SIPSessionManager) serviceLocator.getService(Service.SIP_SESSION_MANAGER);
    }
    
    @Viewless
    public void show() throws Exception {
        if (session_id != null) {
            String strItems[] = session_id.split(",");
            List<Long> items = new ArrayList<Long>();
            
            for (String id : strItems) {
                items.add(Long.parseLong(id));
            }
            
            if (items.size() > 0) {
                messageList = sipSessionManager.getMessageListBySessionId(items);
            }
        } else if (call_id != null) {
            String strItems[] = call_id.split(",");
            List<String> items = new ArrayList<String>();
            
            for (String callId : strItems) {
                items.add(callId);
            }

            if (items.size() > 0) {
                messageList = sipSessionManager.getMessageListByCallID(items);
            }
        }

        if (messageList != null) {
            OutputStream outputStream = response.getOutputStream();
            SIPScenario scenario = new SIPScenario(messageList);
            scenario.create(outputStream);
        }
    }
}
