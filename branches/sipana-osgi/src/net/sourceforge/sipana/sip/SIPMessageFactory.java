package net.sourceforge.sipana.sip;

import java.text.ParseException;

public interface SIPMessageFactory {
    public SIPMessageInfo createMessage(byte msg[]) throws ParseException;
}
