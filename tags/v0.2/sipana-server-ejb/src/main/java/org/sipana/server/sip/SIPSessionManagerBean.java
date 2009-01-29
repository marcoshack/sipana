package org.sipana.server.sip;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.impl.SIPSessionImpl;

@Stateless
public class SIPSessionManagerBean implements SIPSessionManager
{
    @PersistenceContext(unitName="sipana") private EntityManager manager;

    public void createSIPSession(SIPSessionImpl session) {
        manager.persist(session);
    }

    public SIPSessionImpl getSIPSession(long id) {
        return manager.find(SIPSessionImpl.class, id);
    }
    
    public SIPSessionImpl getSIPSessionByCallID(String callID) {
        String strQuery = "SELECT s FROM SIPSessionImpl s WHERE s.callId = :callid";
        Query query = manager.createQuery(strQuery);
        query.setParameter("callid", callID);
        
        try {
            return (SIPSessionImpl) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public long getSIPSessionCount() {
        String strQuery = "select count(*) from SIPSessionImpl";
        return (Long)manager.createQuery(strQuery).getSingleResult();
    }
    
    public List<SIPSessionImpl> getSIPSessions(
    		long startTime, 
    		long endTime, 
    		String method, 
    		String fromUser, 
    		String toUser,    
    		String callId,
    		List<String> ipAddrList)
    {
        StringBuilder sbQuery = new StringBuilder("SELECT DISTINCT s FROM SIPSessionImpl s ");

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
        
        sbQuery.append("ORDER BY s.startTime DESC");
        
        Query query = manager.createQuery(sbQuery.toString());
        query.setParameter("start", startTime);
        query.setParameter("end", endTime);
        
        return query.getResultList();
    }
    
    public void saveSIPSession(SIPSessionImpl session) {
        manager.merge(session);
    }
    
    public List<SIPMessage> getMessageListBySessionId(List<Long> sessionIdList) {
    	StringBuilder sbQuery = new StringBuilder("SELECT m FROM SIPMessageImpl m INNER JOIN m.sipSession AS s WHERE s.id IN (");
    	sbQuery.append("'").append(StringUtils.join(sessionIdList, "','"));
    	sbQuery.append("') ORDER BY m.time");
    	
    	Query query = manager.createQuery(sbQuery.toString());
    	return query.getResultList();
    }
    
    
    public List<SIPMessage> getMessageListByCallID(List<String> callIdList) {
    	StringBuilder sbQuery = new StringBuilder("SELECT m FROM SIPMessageImpl m WHERE m.callId IN (");
    	sbQuery.append("'").append(StringUtils.join(callIdList, "','")).append("') ORDER BY m.time");
    	
    	Query query = manager.createQuery(sbQuery.toString());
    	return query.getResultList();
    }
}
