package org.sipana.protocol.sip;


public interface SIPResponse extends SIPMessage {
    public String getReasonPhrase();
    public void setReasonPhrase(String reasonPhrase);
    public int getStatusCode();
    public void setStatusCode(int statusCode);
    public String getRelatedRequestMethod();
    public void setRelatedRequestMethod(String method);
}
