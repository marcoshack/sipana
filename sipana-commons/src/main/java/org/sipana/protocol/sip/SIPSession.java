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
    private SIPSessionState state;

    @XmlAttribute
    private String callId;

    @XmlAttribute
    private String requestMethod;

    @XmlAttribute
    private String fromUser;

    @XmlAttribute
    private String toUser;

    private List<SIPMessage> messages;

    public SIPSession() {
        messages = new LinkedList<SIPMessage>();
        firstResponseTime = 0; // no first response
        setupTime = 0; // no setup time
    }
    
    public SIPSession(SIPRequest request) {
        this();
        addMessage(request);
        setRequestMethod(request.getMethod());
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

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String method) {
        this.requestMethod = method;
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

    public SIPSessionState getState() {
        return state;
    }

    public void setState(SIPSessionState state) {
        this.state = state;
    }

    @XmlAttribute
    public String getStateString() {
        return SIPSessionState.getStateString(getState());
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

    public void addMessage(SIPMessage message) {
        synchronized (messages) {
            messages.add(message);
        }
    }

    public List<SIPMessage> getMessageList() {
        return messages;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SIPSession[");
        sb.append("initialRequestMethod=").append(getRequestMethod());
        sb.append(", id=").append(getId());
        sb.append(", startTime=").append(getStartTime());
        sb.append(", firstResponseTime=").append(getFirstResponseTime());
        sb.append(", establishedTime=").append(getEstablishedTime());
        sb.append(", disconnectionStart=").append(getDisconnectionStart());
        sb.append(", endTime=").append(getEndTime());
        sb.append(", state=").append(getState());

        sb.append(", messageList = {");
        if (!messages.isEmpty()) {
            for (SIPMessage m : messages) {
                sb.append(m);
                if (messages.iterator().hasNext()) {
                    sb.append(", ");
                }
            }
        } else {
            sb.append("}");
        }

        return sb.toString();
    }
}
