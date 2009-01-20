/**
 * This file is part of Sipana project <http://sipana.org/>
 *
 * Sipana is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
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
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Where;

@XmlRootElement(name = "sipsession")
@XmlAccessorType(XmlAccessType.NONE)
public class SIPSession implements Serializable
{
    private static final long serialVersionUID = -1294316602762618073L;

    @XmlAttribute
    private long id;

    @XmlAttribute
    private long startTime;

    @XmlAttribute
    private long endTime;

    @XmlAttribute
    private long firstResponseTime;

    @XmlAttribute
    private long disconnectionStart;

    @XmlAttribute
    private long establishedTime;

    @XmlAttribute
    private long setupTime;

    @XmlAttribute
    private int state;

    @XmlAttribute
    private String callId;

    @XmlAttribute
    private String method;

    @XmlAttribute
    private String fromUser;

    @XmlAttribute
    private String toUser;

    @Where(clause="sip_message_type = 1")
    private List<SIPRequest> requests;

    @Where(clause="sip_message_type = 2")
    private List<SIPResponse> responses;

    public SIPSession() {
        requests = new LinkedList<SIPRequest>();
        responses = new LinkedList<SIPResponse>();
        firstResponseTime = 0; // no first response
        setupTime = 0; // no setup time
    }
    
    public SIPSession(SIPRequest request) {
        this();
        addRequest(request);
        setMethod(request.getMethod());
        setCallId(request.getCallID());
        setStartTime(request.getTime());
        setState(SIPSessionState.INITIATED);
        setFromUser(request.getFromUser());
        setToUser(request.getToUser());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public long getDisconnectionStart() {
        return disconnectionStart;
    }

    public void setDisconnectionStart(long time) {
        disconnectionStart = time;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getEstablishedTime() {
        return establishedTime;
    }

    public void setEstablishedTime(long establishedTime) {
        this.establishedTime = establishedTime;
    }

    public long getFirstResponseTime() {
        return firstResponseTime;
    }

    public void setFirstResponseTime(long firstResponseTime) {
        this.firstResponseTime = firstResponseTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getSetupTime() {
        return setupTime;
    }

    public void setSetupTime(long setupTime) {
        this.setupTime = setupTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @XmlAttribute
    public String getStateString() {
        return SIPSessionState.getStateString(getState());
    }

    public List<SIPRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<SIPRequest> requests) {
        synchronized (this.requests) {
            this.requests = requests;            
        }
    }

    public List<SIPResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<SIPResponse> responses) {
        synchronized (this.responses) {
            this.responses = responses;
        }
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String from) {
        this.fromUser = from;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String to) {
        this.toUser = to;
    }
    
    public void addRequest(SIPRequest request) {
        synchronized (requests) {
            requests.add(request);
        }
    }
    
    public void addResponse(SIPResponse response) {
        synchronized (responses) {
            responses.add(response);
        }
    }
    
    public void addMessage(SIPMessage message) {
        if (message instanceof SIPRequest) {
            addRequest((SIPRequest) message);
        } else if (message instanceof SIPRequest) {
            addResponse((SIPResponse) message);
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SIPSession: ");
        sb.append("Initial request method=").append(getMethod());
        sb.append(", id=").append(getId());
        sb.append(", startTime=").append(getStartTime());
        sb.append(", firstResponseTime=").append(getFirstResponseTime());
        sb.append(", establishedTime=").append(getEstablishedTime());
        sb.append(", disconnectionStart=").append(getDisconnectionStart());
        sb.append(", endTime=").append(getEndTime());
        sb.append(", state=").append(getState());

        sb.append(". Request list: ");
        if (!requests.isEmpty()) {
            for (SIPRequest request : requests) {
                sb.append(request);
                if (requests.iterator().hasNext()) {
                    sb.append(", ");
                }
            }
        } else {
            sb.append("empty");
        }

        sb.append(". Response list: ");
        if (!responses.isEmpty()) {
            for (SIPResponse response : responses) {
                sb.append(response);
                if (responses.iterator().hasNext()) {
                    sb.append(", ");
                }
            }
        } else {
            sb.append("empty");
        }

        return sb.toString();
    }
    
    public void merge(SIPSession session) {
        requests.addAll(session.getRequests());
        responses.addAll(session.getResponses());
        
        // TODO [mhack] Think better in the session merge scenarios 
        if (session.getEndTime() > endTime) {
            endTime            = session.getEndTime();
            establishedTime    = session.getEstablishedTime();
            setupTime          = session.getSetupTime();
            disconnectionStart = session.getDisconnectionStart();
            state              = session.getState();
        }
        
        if (session.getStartTime() < startTime) {
            startTime          = session.getStartTime();
            firstResponseTime  = session.getFirstResponseTime();
        }
    }
}
