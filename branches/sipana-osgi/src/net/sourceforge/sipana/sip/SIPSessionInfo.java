
/**
 * This file is part of Sipana project <http://sipana.sourceforge.net>
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

package net.sourceforge.sipana.sip;

public interface SIPSessionInfo {
    
    public long getStartTime();
    public long getEndTime();
    public long getDuration();
    public String getRequestMethod();
    
    public void addResponseInfo(SIPResponseInfo responseInfo);
    public void addRequestInfo(SIPRequestInfo responseInfo);
    public void terminateSession(long time);
    public String getId();
}
