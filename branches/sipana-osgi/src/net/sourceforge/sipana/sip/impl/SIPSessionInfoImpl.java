package net.sourceforge.sipana.sip.impl;

import net.sourceforge.sipana.sip.SIPRequestInfo;
import net.sourceforge.sipana.sip.SIPResponseInfo;
import net.sourceforge.sipana.sip.SIPSessionInfo;

public class SIPSessionInfoImpl implements SIPSessionInfo {
    private long duration;

    private long startTime;

    private long endTime;
    
    public SIPSessionInfoImpl(SIPRequestInfo requestInfo) {
        startTime = requestInfo.getTime();
    }

    public String getRequestMethod() {
        // TODO Auto-generated method stub
        return null;
    }

    public long getDuration() {
        return duration;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void addResponseInfo(SIPResponseInfo responseInfo) {
        // TODO Auto-generated method stub
        
    }

    public void terminateSession() {
        // TODO Auto-generated method stub
        
    }
}
