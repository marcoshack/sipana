package org.sipana.protocol.sip.impl;

import java.io.Serializable;

import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPSession;

public abstract class SIPMessageImpl implements SIPMessage, Serializable 
{
    private static final long serialVersionUID = 8938899488648772992L;
    public static final Integer REQUEST  = 1;
    public static final Integer RESPONSE = 2;
    private long id;
    private String callID;
    private String srcAddr;
    private String dstAddr;
    private String requestAddr;
    private String fromUser;
    private String toUser;
    private int maxForwards;
    private long msgTime;
    private SIPSession sipSession;
    
    public SIPMessageImpl() {}
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("callId=");
        sb.append(getCallID());
        sb.append(", srcIP=").append(getSrcAddress());
        sb.append(", dstIP=").append(getDstAddress());
        sb.append(", fromUser=").append(getFromUser());
        sb.append(", toUser=").append(getToUser());
        sb.append(", time=").append(getTime());
        return sb.toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public String getCallID() {
        return callID;
    }
    
    public void setCallID(String callID) {
        this.callID = callID;
    }
    
    public String getDstAddress() {
        return dstAddr;
    }
    
    public void setDstAddress(String dstIP) {
        this.dstAddr = dstIP;
    }
    
    public String getFromUser() {
        return fromUser;
    }
    
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }
    
    public String getSrcAddress() {
        return srcAddr;
    }
    
    public void setSrcAddress(String srcIP) {
        this.srcAddr = srcIP;
    }
    
    public long getTime() {
        return msgTime;
    }
    
    public void setTime(long time) {
        this.msgTime = time;
    }
    
    public String getToUser() {
        return toUser;
    }
    
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public int getMaxForwards() {
        return maxForwards;
    }

    public void setMaxForwards(int maxForwards) {
        this.maxForwards = maxForwards;
    }    

    public String getRequestAddr() {
        return requestAddr;
    }

    public void setRequestAddr(String requestAddr) {
        this.requestAddr = requestAddr;
    }

	public SIPSession getSipSession() {
		return sipSession;
	}

	public void setSipSession(SIPSession sipSession) {
		this.sipSession = sipSession;
	}
}
