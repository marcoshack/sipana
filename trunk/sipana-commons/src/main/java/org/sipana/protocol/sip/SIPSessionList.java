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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
@XmlRootElement(name = "sipsessionlist")
@XmlSeeAlso(SIPSession.class)
@XmlAccessorType(XmlAccessType.NONE)
public class SIPSessionList extends ArrayList<SIPSession> {
    public SIPSessionList() {
        super();
    }

    public SIPSessionList(List<SIPSession> sessionList) {
        super(sessionList);
    }

    @XmlElementRef
    public List<SIPSession> getSIPSessions() {
        return this;
    }
}
