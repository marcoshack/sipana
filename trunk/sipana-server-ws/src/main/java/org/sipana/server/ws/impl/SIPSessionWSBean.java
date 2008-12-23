/**
 * This file is part of Sipana project <http://sipana.org/>
 *
 * Sipana is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * Sipana is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
 * @author Marcos Hack <marcoshack@gmail.com>
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

        logger.debug("getSIPSession processed");
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
        
        logger.debug("getSIPSessionList processed");
        return sessionList;
    }

    /**
     * JAXB throws an exception if there is a circular reference between
     * objects, this method breaks SIPSession/SIPMessage circular references.
     *
     * @param SIPSession
     * @author Marcos Hack <marcoshack@gmail.com>
     */
    private void breakSIPSessionRefCycle(SIPSession s) {
        for (SIPResponse res : s.getResponses()) {
            res.setSipSession(null);
        }
        for (SIPRequest req : s.getRequests()) {
            req.setSipSession(null);
        }
    }
}
