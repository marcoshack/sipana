package org.sipana.server.web.sip.session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPSessionStatus;
import org.sipana.protocol.sip.impl.SIPSessionImpl;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.sip.SIPSessionManager;

public class SIPSessionLogic {
    private SIPSessionManager sipSessionManager;
    private Logger logger = Logger.getLogger(SIPSessionLogic.class);

    // Logic parameters
    private List<SelectItem> sipSessionList = new ArrayList<SelectItem>();
    private long startTime;
    private long endTime;
    private String method;
    private String fromUser;
    private String toUser;
    
    public SIPSessionLogic() {
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        sipSessionManager = (SIPSessionManager) serviceLocator.getService(Service.SIP_SESSION_MANAGER);
    }

    public void list() {

        long endTimeFinal;
        if (endTime == 0) {
            endTimeFinal = Calendar.getInstance().getTimeInMillis();
        } else {
            endTimeFinal = endTime;
        }
        
        if (logger.isDebugEnabled()) {
            StringBuilder sbDebug = new StringBuilder("Processing SIPSession list request: ");
            sbDebug.append("startTime: ").append(startTime);
            sbDebug.append(", endTime: ").append(endTimeFinal);
            sbDebug.append(", method: ").append(method);
            sbDebug.append(", fromUser: ").append(fromUser);
            sbDebug.append(", toUser: ").append(toUser);
            logger.debug(sbDebug);
        }
        
        List<SIPSessionImpl> sipSessions = sipSessionManager.getSIPSessions(startTime, endTimeFinal, method, fromUser, toUser);
        sipSessionList = new ArrayList<SelectItem>();

        for (SIPSessionImpl session : sipSessions) {
            SIPRequest firstRequest = session.getRequests().get(0);
            
            StringBuilder item = new StringBuilder(getDateString(firstRequest.getTime()));
            item.append(" ").append(firstRequest.getMethod());
            item.append(", From: ").append(firstRequest.getFromUser());
            item.append(", To: ").append(firstRequest.getToUser());
            
            String state = SIPSessionStatus.getStateString(session.getState());
            item.append(", status: ").append(state);
            
            sipSessionList.add(new SelectItem(session.getCallId(), item.toString()));
        }
    }
    
    public void reset() {       
        startTime = 0;
        endTime = 0;
        sipSessionList.clear();
    }
    
    public String details() {
        return "details";
    }

    
    // TODO [mhack] move to an appropriate utility class
    private String getDateString(long dateInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Calendar now = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(dateInMillis);
        
        if (now.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
            dateFormat.applyPattern("HH:mm:ss,S");
            return dateFormat.format(date.getTime());
        } else {
            dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss,S");
            return dateFormat.format(date.getTime());
        }
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

    public List<SelectItem> getSipSessionList() {
        return sipSessionList;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String from) {
        this.fromUser = from;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String to) {
        this.toUser = to;
    }
}
