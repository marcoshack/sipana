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
import org.sipana.server.ws.xml.*;
import org.apache.log4j.Logger;
import org.sipana.protocol.sip.SIPSession;
import org.sipana.protocol.sip.SIPSessionList;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.dao.SIPSessionManager;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class SIPSessionWSImpl implements SIPSessionWSXML {

    private Logger logger = Logger.getLogger(SIPSessionWSImpl.class);
    private SIPSessionManager sipSessionManager;

    public SIPSessionWSImpl() {
        sipSessionManager = (SIPSessionManager) ServiceLocator.getInstance().getService(Service.SIP_SESSION_MANAGER);
    }

    public SIPSession getSIPSession(long sessionId) {
        logger.debug("Processing getSIPSession");

        SIPSession session = sipSessionManager.find(sessionId);

        logger.debug("getSIPSession processed");
        return session;
    }

    public SIPSessionList getSIPSessionList(
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

        List<SIPSession> result = sipSessionManager.find(startTime, endTime, method, fromUser, toUser, callId, ipAddrList);

        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("getSIPSessionList processed. ");
            sb.append(result.size()).append(" item(s) found.");
            logger.debug(sb);
        }

        return new SIPSessionList(result);
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
