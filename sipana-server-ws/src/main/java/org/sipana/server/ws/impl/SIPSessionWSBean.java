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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
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

    public List<SIPSession> getSIPSessionList(
            Long startTime,
            Long endTime,
            String method,
            String fromUser,
            String toUser,
            String callId,
            String strIpAddrList) {

        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Processing getSIPSessionList");
            sb.append(": startTime = ").append(startTime);
            sb.append(", endTime = ").append(endTime);
            sb.append(", method = ").append(method);
            sb.append(", fromUser = ").append(fromUser);
            sb.append(", userUser = ").append(toUser);
            sb.append(", callId = ").append(callId);
            sb.append(", ipAddrList = ").append(strIpAddrList);
            logger.debug(sb);
        }

        List<String> ipAddrList = createIpAddrList(strIpAddrList);

        List<SIPSession> sessionList = sessionManager.getSIPSessions(startTime, endTime, method, fromUser, toUser, callId, ipAddrList);
        breakSIPSessionRefCycle(sessionList);

        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("getSIPSessionList processed. ");
            sb.append(sessionList.size()).append(" item(s) found.");
            logger.debug(sb);
        }

        return sessionList;
    }

    /**
     * JAXB throws an exception if there is a circular reference between
     * objects, this method breaks SIPSession/SIPMessage circular references.
     *
     * @param List<SIPSession>
     * @author Marcos Hack <marcoshack@gmail.com>
     */
    private void breakSIPSessionRefCycle(List<SIPSession> sessionList) {
        for (SIPSession s : sessionList) {
            breakSIPSessionRefCycle(s);
        }
    }

    private void breakSIPSessionRefCycle(SIPSession s) {
        for (SIPResponse res : s.getResponses()) {
            res.setSipSession(null);
        }
        for (SIPRequest req : s.getRequests()) {
            req.setSipSession(null);
        }
    }

    private List<String> createIpAddrList(String strIPAddrList) {
        List<String> addrList = null;

        if (strIPAddrList != null) {
            String csv = strIPAddrList.replaceAll(" +", ",");
            StringTokenizer tokenizer = new StringTokenizer(csv, ",");

            if (tokenizer.countTokens() > 0) {
                addrList = new ArrayList<String>();
                while (tokenizer.hasMoreTokens()) {
                    addrList.add(tokenizer.nextToken());
                }
            }
        }

        return addrList;
    }
}
