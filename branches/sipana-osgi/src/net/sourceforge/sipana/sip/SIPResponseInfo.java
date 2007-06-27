package net.sourceforge.sipana.sip;

public interface SIPResponseInfo extends SIPMessageInfo {
    
    public int getStatusCode();
    public String getReasonPhrase();
    
    public void setStatusCode(int code);
    public void setReasonPhrase(String phrase);
}
