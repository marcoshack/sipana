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
package org.sipana.server.ws.impl;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.plugins.providers.multipart.MultipartOutput;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPResponse;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.dao.SIPMessageManager;
import org.sipana.server.ws.SIPMessageWS;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class SIPMessageWSImpl implements SIPMessageWS {

    private SIPMessageManager sipMessageManager;

    public SIPMessageWSImpl() {
        sipMessageManager = (SIPMessageManager) ServiceLocator.getInstance().getService(Service.SIP_MESSAGE_MANAGER);
    }

    public List<SIPMessage> getSIPMessageList(long sessionId) {
        List<SIPMessage> result = sipMessageManager.getMessageListBySessionId(sessionId);
        breakSIPMessageRefCycle(result);
        return result;
    }

    public MultipartOutput getSIPMessageListMultipart(long sessionId) {
        MultipartOutput out = new MultipartOutput();

        List<SIPMessage> messageList = getSIPMessageList(sessionId);

        for (SIPMessage m : messageList) {
            if (m instanceof SIPRequest) {
                out.addPart((SIPRequest)m, MediaType.APPLICATION_XML_TYPE);
            } else if (m instanceof SIPResponse) {
                out.addPart((SIPResponse)m, MediaType.APPLICATION_XML_TYPE);
            }
        }

        return out;
    }

    /**
     * JAXB throws an exception if there is a circular reference between
     * objects, this method breaks SIPSession/SIPMessage circular references.
     *
     * @param List<SIPMessage>
     * @author Marcos Hack <marcoshack@gmail.com>
     */
    private void breakSIPMessageRefCycle(List<SIPMessage> messageList) {
        for (SIPMessage m : messageList) {
            breakSIPSessionRefCycle(m);
        }
    }

    private void breakSIPSessionRefCycle(SIPMessage m) {
        m.setSipSession(null);
    }
}
