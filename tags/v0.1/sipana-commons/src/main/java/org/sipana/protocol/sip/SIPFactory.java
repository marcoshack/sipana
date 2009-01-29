package org.sipana.protocol.sip;

import java.text.ParseException;

public interface SIPFactory {
    public SIPMessage createMessage(byte msg[]) throws ParseException;
    public SIPSession createSession(SIPRequest request);
}
