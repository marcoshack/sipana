package net.sourceforge.sipana.sip.impl;

public class SIPResponseInfoImpl extends SIPMessageInfoImpl {

    private int statusCode;

    private String reasonPhrase;

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
