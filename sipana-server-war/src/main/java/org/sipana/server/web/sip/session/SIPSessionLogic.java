package org.sipana.server.web.sip.session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPSessionStatus;
import org.sipana.protocol.sip.impl.SIPSessionImpl;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.sip.SIPSessionManager;
import org.vraptor.annotations.Component;
import org.vraptor.annotations.Out;
import org.vraptor.annotations.Parameter;

@Component
public class SIPSessionLogic {
    @Out
    private List<SIPSessionItem> sipSessionList = null;

    // List all SIP sessions (from 0 to "now") if startTime and endTime
    // parameters are not specified
    @Parameter
    private long startTime = 0;
    @Parameter
    private long endTime = Calendar.getInstance().getTimeInMillis();
    
    private SIPSessionManager sipSessionManager;

    public SIPSessionLogic() {
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        sipSessionManager = (SIPSessionManager) serviceLocator.getService(Service.SIP_SESSION_MANAGER);
    }

    public void list() {
        List<SIPSessionImpl> sipSessions = sipSessionManager.getSIPSessions(startTime, endTime);
        sipSessionList = new ArrayList<SIPSessionItem>();
        
        for (SIPSessionImpl session : sipSessions) {
            SIPSessionItem item = new SIPSessionItem();
            
            item.setId(session.getId());
            item.setMethod(session.getMethod());
            SIPRequest firstRequest = session.getRequests().get(0);
            item.setFromAddr(firstRequest.getFromUser());
            item.setToAddr(firstRequest.getToUser());
            item.setCallId(session.getCallId());
            item.setStatus(SIPSessionStatus.getStateString(session.getState()));
            
            sipSessionList.add(item);
        }
    }
}
