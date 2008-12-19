package org.sipana.server.sip;

import java.util.List;
import javax.ejb.Local;

import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPSession;

@Local
public interface SIPSessionManager {
    public void createSIPSession(SIPSession session);
    public void saveSIPSession(SIPSession session);
    public long getSIPSessionCount();
    public SIPSession getSIPSession(long id);
    public SIPSession getSIPSessionByCallID(String callID);
    public List<SIPSession> getSIPSessions(Long startTime, Long endTime, String method, String fromUser, String toUser, String callId, List<String> ipAddrList);
    public List<SIPMessage> getMessageListBySessionId(List<Long> sessionIdList);
    public List<SIPMessage> getMessageListByCallID(List<String> callIdList);
}
