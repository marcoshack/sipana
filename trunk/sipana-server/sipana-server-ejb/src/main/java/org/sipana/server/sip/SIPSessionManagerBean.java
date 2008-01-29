package org.sipana.server.sip;

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
    
    public List<SIPSessionImpl> getSIPSessions(long startTime, long endTime) {
        String strQuery = "select s from SIPSessionImpl s "
            + "where s.startTime >= :start and s.startTime <= :end "
            + "order by s.startTime desc";
        
        Query query = manager.createQuery(strQuery);
        query.setParameter("start", startTime);
        query.setParameter("end", endTime);
        
        return query.getResultList();
    }
    
    public void saveSIPSession(SIPSessionImpl session) {
        manager.merge(session);
    }
    
    public List<SIPMessage> getMessageListBySessionId(long id) {
    	Query query = manager.createNativeQuery("select * from sip_messages"
    			+ " where id_sip_session = " + id
    			+ " order by time");
    	
//    	String strQuery = "select m from SIPMessageImpl m, SIPSessionImpl s "
//    		+ "where s.id = :session_id "
//    		+ "  and m.id_sip_session = s.id "
//    		+ "order by m.time";
//    	
//    	Query query = manager.createQuery(strQuery);
//    	query.setParameter("session_id", id);
    	
    	return query.getResultList();
    }
}
