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
package org.sipana.server.ejb.impl;

import org.sipana.server.ejb.impl.SIPSessionFindParams;
import org.sipana.server.ejb.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.sipana.protocol.sip.SIPSession;

@Stateless
public class SIPSessionManagerBean extends AbstractManagerBean implements SIPSessionManager {

    public SIPSession find(long id) {
        return manager.find(SIPSession.class, id);
    }

    public SIPSession findByCallID(String callID) {
        String strQuery = "SELECT s FROM SIPSession s WHERE s.callId = :callid";
        Query query = manager.createQuery(strQuery);
        query.setParameter("callid", callID);

        try {
            return (SIPSession) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public long getSIPSessionCount() {
        String strQuery = "select count(*) from SIPSession";
        return (Long) createQuery(strQuery).getSingleResult();
    }

    // TODO [mhack] remade find(SIPSessionFindParams) method.
    public List<SIPSession> find(SIPSessionFindParams params) {
        List<String> ipAddrList = new ArrayList<String>();

        if (params.getDstAddressList() != null) {
            ipAddrList.addAll(params.getDstAddressList());
        }
        if (params.getSrcAddressList() != null) {
            ipAddrList.addAll(params.getSrcAddressList());
        }

        Date startDate = params.getStartDate();
        Long startTime = startDate != null ? startDate.getTime() : 0L;

        Date endDate = params.getEndDate();
        Long endTime;
        if (endDate != null) {
            endTime = endDate.getTime();
        } else {
            endTime = GregorianCalendar.getInstance().getTimeInMillis();
        }

        String method = params.getRequestMethod();
        String fromUser = params.getFromUser();
        String toUser = params.getToUser();
        String callID = params.getCallID();

        StringBuilder sbQuery = new StringBuilder("SELECT DISTINCT s FROM SIPSession s ");

        if (ipAddrList != null && ipAddrList.size() > 0) {
            sbQuery.append("INNER JOIN s.messages AS m ");
        }

        sbQuery.append("WHERE s.startTime >= :start AND s.endTime <= :end ");

        if (method != null && !method.equals("")) {
            sbQuery.append("AND s.requestMethod = '").append(method.toUpperCase()).append("' ");
        }

        if (fromUser != null && !fromUser.equals("")) {
            sbQuery.append("AND s.fromUser LIKE '%").append(fromUser).append("%' ");
        }

        if (toUser != null && !toUser.equals("")) {
            sbQuery.append("AND s.toUser LIKE '%").append(toUser).append("%' ");
        }

        if (callID != null && !callID.equals("")) {
            sbQuery.append("AND s.callId LIKE '%").append(callID).append("%' ");
        }

        if (ipAddrList != null && ipAddrList.size() > 0) {
            String csvList = StringUtils.join(ipAddrList, "','");
            String ipAddrInList = new StringBuilder("'").append(csvList).append("'").toString();
            sbQuery.append("AND (m.srcAddress IN (").append(ipAddrInList);
            sbQuery.append(") OR m.dstAddress IN (").append(ipAddrInList).append(")) ");
        }

        // If startTime/endTime is null set it to min/max values
        if (startTime == null) {
            startTime = 0L;
        }
        if (endTime == null) {
            endTime = Calendar.getInstance().getTimeInMillis();
        }

        sbQuery.append("ORDER BY s.startTime DESC");

        Query query = createQuery(sbQuery);
        query.setParameter("start", startTime);
        query.setParameter("end", endTime);

        return query.getResultList();
    }

    @Deprecated
    public List<SIPSession> find(
            Long startTime,
            Long endTime,
            String method,
            String fromUser,
            String toUser,
            String callId,
            List<String> ipAddrList) {
        SIPSessionFindParams params = new SIPSessionFindParams();

        Calendar calendar = GregorianCalendar.getInstance();
        if (startTime != null) {
            calendar.setTimeInMillis(startTime);
            params.setStartDate(calendar.getTime());
        }

        if (endTime != null) {
            calendar.setTimeInMillis(endTime);
            params.setEndDate(calendar.getTime());
        }

        params.setRequestMethod(method);
        params.setFromUser(fromUser);
        params.setToUser(toUser);
        params.setCallID(callId);
        params.setSrcAddressList(ipAddrList);

        return find(params);
    }

    public void save(SIPSession session) {
        if (session.getId() > 0) {
            manager.merge(session);
        } else {
            manager.persist(session);
        }
    }
}
