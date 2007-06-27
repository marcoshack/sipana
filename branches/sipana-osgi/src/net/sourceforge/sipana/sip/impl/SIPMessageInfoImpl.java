package net.sourceforge.sipana.sip.impl;

import net.sourceforge.sipana.sip.SIPMessageInfo;

public abstract class SIPMessageInfoImpl implements SIPMessageInfo {
    
    private String callID;
    private String srcIP;
    private String dstIP;
    private String fromUser;
    private String toUser;
    private long time;
    
    public String getCallID() {
        return callID;
    }
    
    public void setCallID(String callID) {
        this.callID = callID;
    }
    
    public String getDstIP() {
        return dstIP;
    }
    
    public void setDstIP(String dstIP) {
        this.dstIP = dstIP;
    }
    
    public String getFromUser() {
        return fromUser;
    }
    
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }
    
    public String getSrcIP() {
        return srcIP;
    }
    
    public void setSrcIP(String srcIP) {
        this.srcIP = srcIP;
    }
    
    public long getTime() {
        return time;
    }
    
    public void setTime(long time) {
        this.time = time;
    }
    
    public String getToUser() {
        return toUser;
    }
    
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }
}
