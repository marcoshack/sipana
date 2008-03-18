package org.sipana.server.web.sip.session;

import javax.faces.model.SelectItem;

public class SIPSessionItem{
    private long id;
    private String method;
    private String callId;
    private String fromAddr;
    private String toAddr;
    private String status;
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getCallId() {
        return callId;
    }
    public void setCallId(String callId) {
        this.callId = callId;
    }
    public String getFromAddr() {
        return fromAddr;
    }
    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }
    public String getToAddr() {
        return toAddr;
    }
    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
