package org.sipana.server.ws.impl;

import org.sipana.server.ws.*;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.sipana.protocol.sip.impl.SIPSessionImpl;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.sip.SIPSessionManager;

/**
 *
 * @author mhack
 */
public class SIPSessionWSBean implements SIPSessionWS {

    private Logger logger = Logger.getLogger(SIPSessionWSBean.class);
    private SIPSessionManager sessionManager;

    public SIPSessionWSBean() {
        sessionManager = (SIPSessionManager) ServiceLocator.getInstance().getService(Service.SIP_SESSION_MANAGER);
    }

    public String getSessionList() {
        logger.debug("Processing SIPSession list request");
        
        Long startTime = null;
        Long endTime = null;
        String method = null;
        String fromUser = null;
        String toUser = null;
        String callId = null;
        List<String> ipAddrList = null;

        List<SIPSessionImpl> sessionList = sessionManager.getSIPSessions(
                startTime,
                endTime,
                method,
                fromUser,
                toUser,
                callId,
                ipAddrList);

        StringBuilder sbRes = new StringBuilder();
        for (SIPSessionImpl s : sessionList) {
            sbRes.append(new Date(s.getStartTime()));
            sbRes.append(", ").append(s.getMethod());
            sbRes.append(", ").append(s.getFromUser());
            sbRes.append(", ").append(s.getToUser());
            sbRes.append(", ").append(s.getState());
            sbRes.append("\n");
        }

        return sbRes.toString();
    }
}
