package net.sourceforge.sipana.sip.impl;

public class SIPRequestInfoImpl extends SIPMessageInfoImpl {
    
    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
