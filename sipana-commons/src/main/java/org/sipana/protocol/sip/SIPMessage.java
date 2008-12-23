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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sipmessage")
public abstract class SIPMessage implements Serializable
{
    private static final long serialVersionUID = 8938899488648772992L;
    public static final Integer REQUEST  = 1;
    public static final Integer RESPONSE = 2;
    private long id;
    private String callID;
    private String srcAddr;
    private int srcPort;
    private String dstAddr;
    private int dstPort;
    private String requestAddr;
    private String fromUser;
    private String toUser;
    private String fromDisplay;
    private String toDisplay;
    private int maxForwards;
    private long msgTime;
    private SIPSession sipSession;
    
    public SIPMessage() {}
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(SIPMessage.class.getName());
        sb.append("callId=").append(getCallID());
        sb.append(", srcAddr=").append(getSrcAddress()).append(":").append(getSrcPort());
        sb.append(", dstAddr=").append(getDstAddress()).append(":").append(getDstPort());
        sb.append(", From=").append("\"").append(getFromDisplay()).append("\" <").append(getFromUser()).append(">");
        sb.append(", To=").append("\"").append(getToDisplay()).append("\" <").append(getToUser()).append(">");
        sb.append(", time=").append(getTime());
        return sb.toString();
    }

    @XmlElement
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @XmlElement
    public String getCallID() {
        return callID;
    }
    
    public void setCallID(String callID) {
        this.callID = callID;
    }

    @XmlElement
    public String getDstAddress() {
        return dstAddr;
    }
    
    public void setDstAddress(String dstIP) {
        this.dstAddr = dstIP;
    }

    @XmlElement
    public String getFromUser() {
        return fromUser;
    }
    
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    @XmlElement
    public String getSrcAddress() {
        return srcAddr;
    }
    
    public void setSrcAddress(String srcIP) {
        this.srcAddr = srcIP;
    }

    @XmlElement
    public long getTime() {
        return msgTime;
    }
    
    public void setTime(long time) {
        this.msgTime = time;
    }

    @XmlElement
    public String getToUser() {
        return toUser;
    }
    
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    @XmlElement
    public int getMaxForwards() {
        return maxForwards;
    }

    public void setMaxForwards(int maxForwards) {
        this.maxForwards = maxForwards;
    }    

    @XmlElement
    public String getRequestAddr() {
        return requestAddr;
    }

    public void setRequestAddr(String requestAddr) {
        this.requestAddr = requestAddr;
    }

    @XmlElement
	public SIPSession getSipSession() {
		return sipSession;
	}

	public void setSipSession(SIPSession sipSession) {
		this.sipSession = sipSession;
	}

    @XmlElement
    public String getFromDisplay() {
        return fromDisplay;
    }

    public void setFromDisplay(String fromDisplay) {
        this.fromDisplay = fromDisplay;
    }

    @XmlElement
    public String getToDisplay() {
        return toDisplay;
    }

    public void setToDisplay(String toDisplay) {
        this.toDisplay = toDisplay;
    }

    @XmlElement
    public int getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(int srcPort) {
        this.srcPort = srcPort;
    }

    @XmlElement
    public int getDstPort() {
        return dstPort;
    }

    public void setDstPort(int dstPort) {
        this.dstPort = dstPort;
    }
}
