package org.sipana.protocol.sip;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sipresponse")
public class SIPResponse extends SIPMessage
{
    private static final long serialVersionUID = -7490558556327953840L;
    private int statusCode;
    private String reasonPhrase;
    private String relatedRequestMethod;

    @XmlElement
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    @XmlElement
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @XmlElement
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
