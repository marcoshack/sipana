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
    public List<SIPSessionImpl> getSIPSessions(long startTime, long endTime, String method, String fromUser, String toUser, String callId);
    public List<SIPMessage> getMessageListBySessionId(List<Long> sessionIdList);
    public List<SIPMessage> getMessageListByCallID(List<String> callIdList);
}
