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
package org.sipana.server.sip;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPSession;

@Stateless
public class SIPSessionManagerBean implements SIPSessionManager
{
    @PersistenceContext(unitName="sipana") private EntityManager manager;

    public void createSIPSession(SIPSession session) {
        manager.persist(session);
    }

    public SIPSession getSIPSession(long id) {
        return manager.find(SIPSession.class, id);
    }
    
    public SIPSession getSIPSessionByCallID(String callID) {
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
        return (Long)manager.createQuery(strQuery).getSingleResult();
    }
    
    public List<SIPSession> getSIPSessions(
    		Long startTime,
    		Long endTime,
    		String method, 
    		String fromUser, 
    		String toUser,    
    		String callId,
    		List<String> ipAddrList)
    {
        StringBuilder sbQuery = new StringBuilder("SELECT DISTINCT s FROM SIPSession s ");

        if (ipAddrList != null && ipAddrList.size() > 0) {
        	sbQuery.append("INNER JOIN s.requests AS r ");
        }
        
        sbQuery.append("WHERE s.startTime >= :start AND s.startTime <= :end ");
        
        if (method != null && !method.equals("")) {
        	sbQuery.append("AND s.method = '").append(method.toUpperCase()).append("' ");
        }
        
        if (fromUser != null && !fromUser.equals("")) {
        	sbQuery.append("AND s.fromUser LIKE '%").append(fromUser).append("%' ");
        }
        
        if (toUser != null && !toUser.equals("")) {
        	sbQuery.append("AND s.toUser LIKE '%").append(toUser).append("%' ");
        }
        
        if (callId != null && !callId.equals("")) {
        	sbQuery.append("AND s.callId LIKE '%").append(callId).append("%' ");
        }
        
        if (ipAddrList != null && ipAddrList.size() > 0) {
            String ipAddrInList = new StringBuilder("'").append(StringUtils.join(ipAddrList, "','")).append("'").toString();
        	sbQuery.append("AND (r.srcAddress IN (").append(ipAddrInList);
        	sbQuery.append(") OR r.dstAddress IN (").append(ipAddrInList).append(")) ");
        }

        // If startTime/endTime is null set it to min/max values
        if (startTime == null) {
            startTime = 0L;
        }
        if (endTime == null) {
            endTime = Calendar.getInstance().getTimeInMillis();
        }
        
        sbQuery.append("ORDER BY s.startTime DESC");
        
        Query query = manager.createQuery(sbQuery.toString());
        query.setParameter("start", startTime);
        query.setParameter("end", endTime);
        
        return query.getResultList();
    }
    
    public void saveSIPSession(SIPSession session) {
        manager.merge(session);
    }
    
    public List<SIPMessage> getMessageListBySessionId(List<Long> sessionIdList) {
    	StringBuilder sbQuery = new StringBuilder("SELECT m FROM SIPMessage m INNER JOIN m.sipSession AS s WHERE s.id IN (");
    	sbQuery.append("'").append(StringUtils.join(sessionIdList, "','"));
    	sbQuery.append("') ORDER BY m.time");
    	
    	Query query = manager.createQuery(sbQuery.toString());
    	return query.getResultList();
    }
    
    
    public List<SIPMessage> getMessageListByCallID(List<String> callIdList) {
    	StringBuilder sbQuery = new StringBuilder("SELECT m FROM SIPMessage m WHERE m.callId IN (");
    	sbQuery.append("'").append(StringUtils.join(callIdList, "','")).append("') ORDER BY m.time");
    	
    	Query query = manager.createQuery(sbQuery.toString());
    	return query.getResultList();
    }
}
