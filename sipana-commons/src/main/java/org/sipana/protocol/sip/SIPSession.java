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

package org.sipana.protocol.sip;

import java.io.Serializable;
import java.util.List;

public interface SIPSession extends Serializable {
    public long getId();
    public void setId(long id);
    public String getCallId();
    public void setCallId(String callId);
    public long getDisconnectionStart();
    public void setDisconnectionStart(long time);
    public long getEndTime();
    public void setEndTime(long endTime);
    public long getEstablishedTime();
    public void setEstablishedTime(long establishedTime);
    public long getFirstResponseTime();
    public void setFirstResponseTime(long firstResponseTime);
    public String getMethod();
    public void setMethod(String method);
    public long getStartTime();
    public void setStartTime(long startTime);
    public long getSetupTime();
    public void setSetupTime(long setupTime);
    public int getState();
    public void setState(int state);
    public List<SIPRequest> getRequests();
    public void setRequests(List<SIPRequest> requests);
    public List<SIPResponse> getResponses();
    public void setResponses(List<SIPResponse> responses);
    public void addRequest(SIPRequest request);
    public void addResponse(SIPResponse response);
    public String getFromUser();
    public void setFromUser(String from);
    public String getToUser();
    public void setToUser(String to);
}
