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
import org.sipana.server.ws.xml.SIPMessageWSXML;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class SIPMessageWSImpl implements SIPMessageWSXML {

    private SIPMessageManager sipMessageManager;

    public SIPMessageWSImpl() {
        sipMessageManager = (SIPMessageManager) ServiceLocator.getInstance().getService(Service.SIP_MESSAGE_MANAGER);
    }

    public SIPMessageList getSIPMessageList(long sessionId) {
        List<SIPMessage> result = sipMessageManager.getMessageListBySessionId(sessionId);
        return new SIPMessageList(result);
    }
}
