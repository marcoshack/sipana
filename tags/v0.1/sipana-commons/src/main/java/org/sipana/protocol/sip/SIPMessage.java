package org.sipana.protocol.sip;

import java.io.Serializable;

public interface SIPMessage extends Serializable {
    public long getId();
    public void setId(long id);
    public String getCallID();
    public void setCallID(String callID);
    public String getFromUser();
    public void setFromUser(String fromUser);
    public String getSrcAddress();
    public void setSrcAddress(String srcIP);
    public int getSrcPort();
    public void setSrcPort(int srcIP);
    public String getDstAddress();
    public void setDstAddress(String dstIP);
    public int getDstPort();
    public void setDstPort(int dstPort);
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
    public String getFromDisplay();
    public void setFromDisplay(String fromDisplay);
    public String getToDisplay();
    public void setToDisplay(String toDisplay);
}
