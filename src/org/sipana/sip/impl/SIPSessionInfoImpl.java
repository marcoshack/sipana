
/**
 * This file is part of Sipana project <http://sipana.org/>
 * 
 * Sipana is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sipana is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */ 

package org.sipana.sip.impl;

import java.util.LinkedList;

import org.sipana.sip.SIPRequestInfo;
import org.sipana.sip.SIPResponseInfo;
import org.sipana.sip.SIPSessionInfo;

public class SIPSessionInfoImpl implements SIPSessionInfo {
    private long startTime;
    private long endTime;
    private long disconnectDelay;
    private long firstResponseTime;
    private long disconnectStartTime;
    private long establishedTime;
    private int state;
    private String id;
    private String method;
    private LinkedList<SIPRequestInfo> requests;
    private LinkedList<SIPResponseInfo> responses;
    
    public SIPSessionInfoImpl(SIPRequestInfo requestInfo) {
        requests = new LinkedList<SIPRequestInfo>();
        responses = new LinkedList<SIPResponseInfo>();
        state = SIPSessionInfo.ACTIVE;
        
        method    = requestInfo.getMethod();
        id        = requestInfo.getCallID();
        startTime = requestInfo.getTime();
        
        endTime             = -1;
        disconnectStartTime = -1;
        firstResponseTime   = -1;
        establishedTime     = -1;
        
        addRequestInfo(requestInfo);
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
    
    public void setEndTime(long time) {
        endTime = time;
        disconnectDelay = time - disconnectStartTime;
    }

    public String getId() {
        return id;
    }
    
    public void setDisconnectionStartTime(long time) {
        disconnectStartTime = time;
    }
    
    public long getDisconnectDelay() {
        return disconnectDelay;
    }
    
    public void setEstablishedTime(long time) {
        establishedTime = time;
    }
    
    public long getEstablishedTime() {
        return establishedTime;
    }
    
    public long getRequestDelay() {
        return (firstResponseTime >= 0 ? firstResponseTime - startTime : -1);
    }
    
    public void setFirstResponseTime(long time) {
        firstResponseTime = time;
    }

    public int getState() {
        return state;
    }
    
    public void setState(int state) {
        this.state = state;
    }
}
