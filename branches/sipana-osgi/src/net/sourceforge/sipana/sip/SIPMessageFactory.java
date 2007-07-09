package net.sourceforge.sipana.sip;

import java.text.ParseException;

public interface SIPMessageFactory {
    public SIPMessageFactory getInstance();
    public SIPMessageInfo createMessage(byte msg[]) throws ParseException;
}
