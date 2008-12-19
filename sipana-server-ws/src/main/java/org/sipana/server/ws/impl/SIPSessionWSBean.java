package org.sipana.server.ws.impl;

import java.util.List;
import org.sipana.server.ws.*;
import org.apache.log4j.Logger;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPResponse;
import org.sipana.protocol.sip.SIPSession;
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

   public SIPSession getSIPSession(long sessionId) {
        logger.debug("Processing getSIPSession");
        SIPSession session = sessionManager.getSIPSession(sessionId);
        breakSIPSessionRefCycle(session);
        return session;
    }

    public List<SIPSession> getSIPSessionList() {
        logger.debug("Processing getSIPSessionList");

        Long startTime = null;
        Long endTime = null;
        String method = null;
        String fromUser = null;
        String toUser = null;
        String callId = null;
        List<String> ipAddrList = null;

        List<SIPSession> sessionList = sessionManager.getSIPSessions(startTime, endTime, method, fromUser, toUser, callId, ipAddrList);
        for (SIPSession s : sessionList) {
            breakSIPSessionRefCycle(s);
        }

        return sessionList;
    }

    private void breakSIPSessionRefCycle(SIPSession s) {
        for (SIPResponse res : s.getResponses()) {
            res.setSipSession(null);
        }
        for (SIPRequest req : s.getRequests()) {
            req.setSipSession(null);
        }
    }
}
