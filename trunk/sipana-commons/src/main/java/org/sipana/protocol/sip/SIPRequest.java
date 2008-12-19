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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "siprequest")
public class SIPRequest extends SIPMessage
{
    private static final long serialVersionUID = 6595857043893213159L;
    private String method;

    @XmlElement
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SIP Request, method=").append(getMethod());
        sb.append(", ").append(super.toString());
        return sb.toString();
    }
}
