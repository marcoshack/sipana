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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.sipana.protocol.sip.SIPSession;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.dao.SIPSessionManager;

public class SIPSessionController {

    private SIPSessionManager sipSessionManager;
    private Logger logger = Logger.getLogger(SIPSessionController.class);

    // Logic parameters
    private List<SelectItem> sipSessionList = new ArrayList<SelectItem>();
    private String[] selectedItems;
    private long startTime;
    private long endTime;
    private String requestMethod;
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
            sbDebug.append(", method: ").append(requestMethod);
            sbDebug.append(", fromUser: ").append(fromUser);
            sbDebug.append(", toUser: ").append(toUser);
            sbDebug.append(", callId: ").append(callId);
            sbDebug.append(", ipAddr: ").append(ipAddrList);
            logger.debug(sbDebug);
        }

        List<SIPSession> sipSessions = sipSessionManager.getSIPSessions(
                startTime,
                endTimeFinal,
                requestMethod,
                fromUser,
                toUser,
                callId,
                createIpAddrList(ipAddrList));

        sipSessionList = new ArrayList<SelectItem>();

        for (SIPSession session : sipSessions) {
            StringBuilder item = new StringBuilder(getDateString(session.getStartTime()));
            item.append(" ").append(session.getRequestMethod());
            item.append(", From: ").append(session.getFromUser());
            item.append(", To: ").append(session.getToUser());
            item.append(", Status: ").append(session.getStateString());

            sipSessionList.add(new SelectItem(session.getId(), item.toString()));
        }
    }

    public void reset() {
        startTime = 0;
        endTime = 0;
        requestMethod = null;
        fromUser = null;
        toUser = null;
        callId = null;
        ipAddrList = null;
        selectedItems = null;
        sipSessionList = new ArrayList<SelectItem>();
    }

    public String details() {
        if (selectedItems.length > 0) {
            return "details";
        } else {
            return null;
        }
    }

    /* Getter and Setters */
    
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

    public List<SelectItem> getList() {
        return sipSessionList;
    }

    public String getMethod() {
        return requestMethod;
    }

    public void setMethod(String method) {
        this.requestMethod = method;
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

    public String[] getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(String[] selectedSessionId) {
        this.selectedItems = selectedSessionId;
    }

    public String getSelectedItemsCSV() {
        return StringUtils.join(selectedItems, ",");
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

    // TODO [mhack] obviouslly it isn't the right place to do that
    public String getServerAddress() {
        return System.getProperty("bind.address");
    }
}
