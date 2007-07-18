
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

package org.sipana.sip;

/**
 * Holds SIP message informations used by Sipana. Don't have all SIP message 
 * fields.
 * 
 * @author mhack
 */
public interface SIPMessageInfo {
    
    public String getSrcIP();
    public String getDstIP();
    public long getTime();
    public String getCallID();
    public String getFromUser();
    public String getToUser();
    
    public void setSrcIP(String addr);
    public void setDstIP(String addr);
    public void setTime(long time);
    public void setCallID(String id);
    public void setFromUser(String name);
    public void setToUser(String name);
}
