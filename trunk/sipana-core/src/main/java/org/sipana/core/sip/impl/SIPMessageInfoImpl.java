
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

package org.sipana.core.sip.impl;

import java.io.Serializable;
import java.util.Date;

import org.sipana.core.sip.SIPMessageInfo;

public abstract class SIPMessageInfoImpl implements SIPMessageInfo, Serializable {
    
    private String callID;
    private String srcIP;
    private String dstIP;
    private String fromUser;
    private String toUser;
    private Date date;
    
    public String getCallID() {
        return callID;
    }
    
    public void setCallID(String callID) {
        this.callID = callID;
    }
    
    public String getDstIP() {
        return dstIP;
    }
    
    public void setDstIP(String dstIP) {
        this.dstIP = dstIP;
    }
    
    public String getFromUser() {
        return fromUser;
    }
    
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }
    
    public String getSrcIP() {
        return srcIP;
    }
    
    public void setSrcIP(String srcIP) {
        this.srcIP = srcIP;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date= date;
    }
    
    public String getToUser() {
        return toUser;
    }
    
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("callId=");
        sb.append(getCallID());
        sb.append(", srcIP=").append(getSrcIP());
        sb.append(", dstIP=").append(getDstIP());
        sb.append(", fromUser=").append(getFromUser());
        sb.append(", toUser=").append(getToUser());
        sb.append(", time=").append(getDate().getTime());
        return sb.toString();
    }
}
