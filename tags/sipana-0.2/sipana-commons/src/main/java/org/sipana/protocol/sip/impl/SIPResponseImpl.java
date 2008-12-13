package org.sipana.protocol.sip.impl;

import org.sipana.protocol.sip.SIPResponse;


public class SIPResponseImpl extends SIPMessageImpl implements SIPResponse
{
    private static final long serialVersionUID = -7490558556327953840L;
    private int statusCode;
    private String reasonPhrase;
    private String relatedRequestMethod;

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

    public String getRelatedRequestMethod() {
        return relatedRequestMethod;
    }
    
    public void setRelatedRequestMethod(String method) {
        relatedRequestMethod = method;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SIP Response, statusCode=").append(getStatusCode());
        sb.append(", reasonPhrase=").append(getReasonPhrase());
        sb.append(", relatedRequestMethod=").append(getRelatedRequestMethod());
        sb.append(", ").append(super.toString());
        return sb.toString();
    }
}
