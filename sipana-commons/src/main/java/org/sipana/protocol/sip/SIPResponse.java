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

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sipresponse")
public class SIPResponse extends SIPMessage
{
    private static final long serialVersionUID = -7490558556327953840L;
    private int statusCode;
    private String reasonPhrase;
    private String relatedRequestMethod;

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRelatedRequestMethod() {
        return relatedRequestMethod;
    }
    
    public void setRelatedRequestMethod(String method) {
        relatedRequestMethod = method;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SIP Response, statusCode=").append(getStatusCode());
        sb.append(", reasonPhrase=").append(getReasonPhrase());
        sb.append(", relatedRequestMethod=").append(getRelatedRequestMethod());
        sb.append(", ").append(super.toString());
        return sb.toString();
    }
}
