package net.sourceforge.sipana.sip;

/**
 * Holds SIP message informations used by Sipana. Don't have all SIP message 
 * fields.
 * 
 * @author mhack
 */
public interface SIPMessageInfo {
    
    public String getSrcIP();
    public String getDstIP();
    public long getTime();
    public String getCallID();
    public String getFromUser();
    public String getToUser();
    
    public void setSrcIP(String addr);
    public void setDstIP(String addr);
    public void setTime(long time);
    public void setCallID(String id);
    public void setFromUser(String name);
    public void setToUser(String name);
}
