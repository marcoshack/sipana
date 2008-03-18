package org.sipana.server.web.sip.session;

import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPSessionStatus;
import org.sipana.protocol.sip.impl.SIPSessionImpl;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.sip.SIPSessionManager;
import org.sipana.sip.scenario.SIPScenario;

public class SIPSessionLogic {

    private String[] sessionId;

    private String call_id = null;

    private List<SelectItem> sipSessionList = null;

    private List<SIPMessage> messageList;

    // List all SIP sessions

    private long startTime;
    private long endTime;

    private SIPSessionManager sipSessionManager;

    public SIPSessionLogic() {
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        sipSessionManager = (SIPSessionManager) serviceLocator.getService(Service.SIP_SESSION_MANAGER);
    }

    public String list() {

        List<SIPSessionImpl> sipSessions = sipSessionManager.getSIPSessions(startTime,
                endTime);
        sipSessionList = new ArrayList<SelectItem>();

        for (SIPSessionImpl session : sipSessions) {

            String item = "id:" + session.getId() + ", method:"
                    + session.getMethod();
            SIPRequest firstRequest = session.getRequests().get(0);
            item += ", From:" + firstRequest.getFromUser() + ", To:"
                    + firstRequest.getToUser() + ", CallId:"
                    + session.getCallId() + ", Status:"
                    + SIPSessionStatus.getStateString(session.getState());

            sipSessionList.add(new SelectItem(session.getId(), item));
        }

        return "list";
    }

    public void show() throws Exception {
        if (sessionId.length > 0) {

            List<Long> items = new ArrayList<Long>();

            for (String id : sessionId) {
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

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                                                                            .getExternalContext()
                                                                            .getResponse();

            OutputStream outputStream = response.getOutputStream();
            SIPScenario scenario = new SIPScenario(messageList);
            scenario.create(outputStream);

            FacesContext.getCurrentInstance().responseComplete();
        }

    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        if (endTime == 0)
            endTime = Calendar.getInstance().getTimeInMillis();

        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List getSipSessionList() {
        return sipSessionList;
    }

    public String[] getSessionId() {
        return sessionId;
    }

    public void setSessionId(String[] sessionId) {
        this.sessionId = sessionId;
    }
}
