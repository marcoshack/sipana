package org.sipana.protocol.sip;

import java.io.Serializable;

public interface SIPMessage extends Serializable {
    public long getId();
    public void setId(long id);
    public String getCallID();
    public void setCallID(String callID);
    public String getDstAddress();
    public void setDstAddress(String dstIP);
    public String getFromUser();
    public void setFromUser(String fromUser);
    public String getSrcAddress();
    public void setSrcAddress(String srcIP);
    public long getTime();
    public void setTime(long time);
    public String getToUser();
    public void setToUser(String toUser);
    public int getMaxForwards();
    public void setMaxForwards(int maxForwards);
    public String getRequestAddr();
    public void setRequestAddr(String requestAddr);
    public SIPSession getSipSession();
    public void setSipSession(SIPSession session);
}
