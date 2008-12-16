package org.sipana.server.ws;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.sipana.protocol.sip.impl.SIPSessionImpl;
import org.sipana.server.sip.SIPSessionManager;

/**
 *
 * @author mhack
 */

@Stateless
public class SIPSessionBean implements SIPSession {
    @EJB
    private SIPSessionManager manager;

    public String getSessionList() {
        Long startTime = null;
        Long endTime = null;
        String method = null;
        String fromUser = null;
        String toUser = null;
        String callId = null;
        List<String> ipAddrList = null;

        List<SIPSessionImpl> sessionList = manager.getSIPSessions(startTime, endTime, method, fromUser, toUser, callId, ipAddrList);
        
        return sessionList.toString();
    }

}
