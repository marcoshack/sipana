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
import org.apache.commons.lang.StringUtils;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPMessageList;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.ejb.SIPMessageManager;
import org.sipana.server.ws.xml.SIPMessageWSXML;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class SIPMessageWSImpl implements SIPMessageWSXML {

    private SIPMessageManager sipMessageManager;

    public SIPMessageWSImpl() {
        ServiceLocator sl = ServiceLocator.getInstance();
        sipMessageManager = (SIPMessageManager) sl.getService(Service.SIP_MESSAGE_MANAGER);
    }

    public SIPMessage getMessage(long messageId) {
        return sipMessageManager.find(messageId);
    }

    public SIPMessageList getMessageList(String sessionList) {
        String[] strList = StringUtils.split(sessionList, ",");
        List<Long> list = new ArrayList<Long>();

        for (String strID : strList) {
            list.add(Long.parseLong(strID));
        }

        List<SIPMessage> result = sipMessageManager.findBySessionID(list);
        return new SIPMessageList(result);
    }

    public void saveMessageList(SIPMessageList messageList) {
        sipMessageManager.save(messageList);
    }
}
