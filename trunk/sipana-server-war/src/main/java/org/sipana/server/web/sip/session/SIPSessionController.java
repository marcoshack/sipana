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
package org.sipana.server.web.sip.session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPSessionState;
import org.sipana.protocol.sip.SIPSession;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.dao.SIPSessionManager;

public class SIPSessionController {
    private SIPSessionManager sipSessionManager;
    private Logger logger = Logger.getLogger(SIPSessionController.class);

    // Logic parameters
    private List<SelectItem> sipSessionList = new ArrayList<SelectItem>();
    private long startTime;
    private long endTime;
    private String method;
    private String fromUser;
    private String toUser;
    private String callId;
    private String ipAddrList;
    
    public SIPSessionController() {
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        sipSessionManager = (SIPSessionManager) serviceLocator.getService(Service.SIP_SESSION_MANAGER);
    }

    public void search() {
        long endTimeFinal = endTime == 0 ? Calendar.getInstance().getTimeInMillis() : endTime;

        if (logger.isDebugEnabled()) {
            StringBuilder sbDebug = new StringBuilder("Processing SIPSession list request: ");
            sbDebug.append("startTime: ").append(startTime);
            sbDebug.append(", endTime: ").append(endTimeFinal);
            sbDebug.append(", method: ").append(method);
            sbDebug.append(", fromUser: ").append(fromUser);
            sbDebug.append(", toUser: ").append(toUser);
            sbDebug.append(", callId: ").append(callId);
            sbDebug.append(", ipAddr: ").append(ipAddrList);
            logger.debug(sbDebug);
        }
        
        List<SIPSession> sipSessions = sipSessionManager.getSIPSessions(
                startTime, 
                endTimeFinal, 
                method, 
                fromUser, 
                toUser, 
                callId, 
                createIpAddrList(ipAddrList));
        
        sipSessionList = new ArrayList<SelectItem>();

        for (SIPSession session : sipSessions) {
            SIPRequest firstRequest = session.getRequests().get(0);
            
            StringBuilder item = new StringBuilder(getDateString(firstRequest.getTime()));
            item.append(" ").append(firstRequest.getMethod());
            item.append(", From: ").append(firstRequest.getFromUser());
            item.append(", To: ").append(firstRequest.getToUser());
            
            String state = SIPSessionState.getStateString(session.getState());
            item.append(", status: ").append(state);
            
            sipSessionList.add(new SelectItem(session.getId(), item.toString()));
        }
    }

    public void reset() {
        startTime  = 0;
        endTime    = 0;
        method     = null;
        fromUser   = null;
        toUser     = null;
        callId     = null;
        ipAddrList = null;
        sipSessionList.clear();
    }
    
    public String details() {
        return "details";
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

    public int getListSize() {
        return sipSessionList.size();
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getIpAddrList() {
        return ipAddrList;
    }

    public void setIpAddrList(String ipAddr) {
        this.ipAddrList = ipAddr;
    }
    
    
    private List<String> createIpAddrList(String strIPAddrList) {
        String csv = strIPAddrList.replaceAll(" +", ",");
        StringTokenizer tokenizer = new StringTokenizer(csv, ",");
        List<String> addrList = new LinkedList<String>();
        
        while (tokenizer.hasMoreTokens()) {
            addrList.add(tokenizer.nextToken());
        }
        
        return addrList;
    }

    // TODO [mhack] move to an appropriate utility class (date string)
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
}
