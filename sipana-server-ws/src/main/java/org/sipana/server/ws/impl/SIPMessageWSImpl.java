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

import java.util.List;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPMessageList;
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

    public SIPMessageList getSIPMessageList(long sessionId) {
        List<SIPMessage> messageList = sipMessageManager.getMessageListBySessionId(sessionId);
        breakSIPMessageRefCycle(messageList);
        return new SIPMessageList(messageList);
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
            breakSIPMessageRefCycle(m);
        }
    }

    private void breakSIPMessageRefCycle(SIPMessage m) {
        m.setSipSession(null);
    }
}
