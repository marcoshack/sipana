package net.sourceforge.sipana.sip;

public interface SipanaSipProvider {
    public void processRequest(SIPRequestInfo requestInfo);
    public void processResponse(SIPResponseInfo responseInfo);
}
