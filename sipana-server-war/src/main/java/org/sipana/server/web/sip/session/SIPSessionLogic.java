package org.sipana.server.web.sip.session;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

import javax.faces.model.SelectItem;

import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPSessionStatus;
import org.sipana.protocol.sip.impl.SIPSessionImpl;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.sip.SIPSessionManager;

public class SIPSessionLogic {

    private List<SelectItem> sipSessionList = null;

    private long startTime;
    private long endTime;

    private SIPSessionManager sipSessionManager;

    public SIPSessionLogic() {
        sipSessionList = new ArrayList<SelectItem>();
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        sipSessionManager = (SIPSessionManager) serviceLocator.getService(Service.SIP_SESSION_MANAGER);
    }

    public void list() {
        
        if(endTime==0)
            endTime=Calendar.getInstance().getTimeInMillis();

        List<SIPSessionImpl> sipSessions = sipSessionManager.getSIPSessions(startTime,
                endTime);
        sipSessionList = new ArrayList<SelectItem>();

        for (SIPSessionImpl session : sipSessions) {
            
            SIPRequest firstRequest = session.getRequests().get(0);

            String item = "CallId: "+ session.getCallId() + ", method: "
                    + session.getMethod() + ", From: " + firstRequest.getFromUser() +
                    ", To: " + firstRequest.getToUser() + ", Status: "
                    + SIPSessionStatus.getStateString(session.getState());

            sipSessionList.add(new SelectItem(session.getCallId(), item));
        }
    }
    
    public void reset() {       
        startTime = 0;
        endTime = 0;
        sipSessionList = new ArrayList<SelectItem>();
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List getSipSessionList() {
        return sipSessionList;
    }
    
}
