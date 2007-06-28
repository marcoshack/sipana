package net.sourceforge.sipana.sip;

public interface SIPSessionInfo {
    
    public long getStartTime();
    public long getEndTime();
    public long getDuration();
    public String getRequestMethod();
    
    public void addResponseInfo(SIPResponseInfo responseInfo);
    public void terminateSession();
}
