package org.sipana.protocol.sip;

import java.text.ParseException;

public interface SIPFactory {
    public SIPMessage createMessage(String data) throws ParseException;
    public SIPSession createSession(SIPRequest request);
}
