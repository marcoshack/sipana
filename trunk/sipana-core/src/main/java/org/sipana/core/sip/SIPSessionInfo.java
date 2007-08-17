
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

package org.sipana.core.sip;

import java.io.Serializable;


public interface SIPSessionInfo extends Serializable {
    public static final int COMPLETED = 0;
    public static final int TIMEOUT = 1;
    public static final int ERROR = 2;
    public static final int ACTIVE = 3;
    
    public long getStartTime();
    public long getEndTime();
    public long getDuration();
    public String getRequestMethod();
    public void addResponseInfo(SIPResponseInfo responseInfo);
    public void addRequestInfo(SIPRequestInfo responseInfo);
    public String getId();
    public int getState();
    public void setState(int state);
    public String getMethod();
    
    // SIP Performance Metrics as defined on draft document
    // http://tools.ietf.org/html/draft-malas-performance-metrics-06
    public long getDisconnectDelay();
    public long getRequestDelay();
    public long getEstablishedTime();

    public void setEndTime(long time);
    public void setDisconnectionStartTime(long time);
    public void setEstablishedTime(long time);
    
    /**
     * Set time of the first provisional response received indicating an 
     * audible or visual status of the initial session setup request.
     * @param time
     */
    public void setFirstResponseTime(long time);
}
