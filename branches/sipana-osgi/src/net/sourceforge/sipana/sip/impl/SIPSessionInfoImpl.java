package net.sourceforge.sipana.sip.impl;

import java.util.LinkedList;

import net.sourceforge.sipana.sip.SIPRequestInfo;
import net.sourceforge.sipana.sip.SIPResponseInfo;
import net.sourceforge.sipana.sip.SIPSessionInfo;

public class SIPSessionInfoImpl implements SIPSessionInfo {
    private long startTime;
    private long endTime;
    private String id;
    private String method;
    private LinkedList<SIPRequestInfo> requests;
    private LinkedList<SIPResponseInfo> responses;
    
    public SIPSessionInfoImpl(SIPRequestInfo requestInfo) {
        addRequestInfo(requestInfo);
        method = requestInfo.getMethod();
        startTime = requestInfo.getTime();
        endTime = -1;
        id = requestInfo.getCallID();
    }

    public String getRequestMethod() {
        return method;
    }

    public long getDuration() {
        return (endTime != -1 ? (endTime - startTime) : -1);
    }

    public long getEndTime() {
        return endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void addResponseInfo(SIPResponseInfo responseInfo) {
        synchronized (responseInfo) {
            responses.add(responseInfo);
        }
    }

    public void addRequestInfo(SIPRequestInfo requestInfo) {
        synchronized (requests) {
            requests.add(requestInfo);
        }
    }
    
    public void terminateSession(long time) {
        endTime = time;
    }

    public String getId() {
        return id;
    }
}
