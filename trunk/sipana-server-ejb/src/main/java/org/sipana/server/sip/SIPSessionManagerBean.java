package org.sipana.server.sip;

import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    
    public long getSIPSessionCount() {
        String strQuery = "select count(*) from SIPSessionImpl";
        return (Long)manager.createQuery(strQuery).getSingleResult();
    }
    
    public List<SIPSessionImpl> getSIPSessions(long startTime, long endTime, String method, String fromUser, String toUser, String callId) {
        StringBuilder sbQuery = new StringBuilder("SELECT s FROM SIPSessionImpl s ");
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
    	StringBuilder sbQuery = new StringBuilder("select m "
    		+ "from SIPMessageImpl m, SIPSessionImpl s "
    		+ "where m.sipSession.id = s.id and (");
    	
    	for (int i = 0; i < sessionIdList.size(); ) {
    		sbQuery.append("s.id = ").append(sessionIdList.get(i));
    		
    		if (++i < sessionIdList.size()) {
    			sbQuery.append(" or ");
    		}
    	}
    	
    	sbQuery.append(") order by m.time");
    		
    	Query query = manager.createQuery(sbQuery.toString());
    	return query.getResultList();
    }
    
    
    public List<SIPMessage> getMessageListByCallID(List<String> callIdList) {
    	StringBuilder sbQuery = new StringBuilder("select m "
    		+ "from SIPMessageImpl m, SIPSessionImpl s "
    		+ "where m.sipSession.id = s.id and (");
    	
    	for (int i = 0; i < callIdList.size(); ) {
    		sbQuery.append("s.callId = '").append(callIdList.get(i)).append("'");
    		
    		if (++i < callIdList.size()) {
    			sbQuery.append(" or ");
    		}
    	}
    	
    	sbQuery.append(") order by m.time");
    	
    	Query query = manager.createQuery(sbQuery.toString());
    	return query.getResultList();
    }
}
