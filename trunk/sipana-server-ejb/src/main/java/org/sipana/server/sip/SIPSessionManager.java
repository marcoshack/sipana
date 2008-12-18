package org.sipana.server.sip;

import java.util.List;
import javax.ejb.Local;

import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.impl.SIPSessionImpl;

@Local
public interface SIPSessionManager {
    public void createSIPSession(SIPSessionImpl session);
    public void saveSIPSession(SIPSessionImpl session);
    public long getSIPSessionCount();
    public SIPSessionImpl getSIPSession(long id);
    public SIPSessionImpl getSIPSessionByCallID(String callID);
    public List<SIPSessionImpl> getSIPSessions(Long startTime, Long endTime, String method, String fromUser, String toUser, String callId, List<String> ipAddrList);
    public List<SIPMessage> getMessageListBySessionId(List<Long> sessionIdList);
    public List<SIPMessage> getMessageListByCallID(List<String> callIdList);
}
