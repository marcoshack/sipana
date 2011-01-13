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
import java.util.StringTokenizer;
import javax.ws.rs.core.StreamingOutput;
import org.apache.log4j.Logger;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.server.dao.SIPMessageManager;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.ws.jpeg.SIPScenarioWSJPEG;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class SIPScenarioWSImpl implements SIPScenarioWSJPEG {

    private Logger logger = Logger.getLogger(SIPScenarioWSImpl.class);

    private SIPMessageManager sipMessageManager;

    public SIPScenarioWSImpl() {
        sipMessageManager = (SIPMessageManager) ServiceLocator.getInstance().getService(Service.SIP_MESSAGE_MANAGER);
    }

    public StreamingOutput getSIPScenario(String strIdList) {
        List<Long> idList = createSIPSessionIdList(strIdList);
        List<SIPMessage> messageList = sipMessageManager.getMessageListBySessionId(idList);
        return new SIPScenarioStreamingOutput(messageList);
    }

    private List<Long> createSIPSessionIdList(String strIdList) {
        List<Long> idList = null;

        if (strIdList != null) {
            String csv = strIdList.replaceAll(" +", ",");
            StringTokenizer tokenizer = new StringTokenizer(csv, ",");

            if (tokenizer.countTokens() > 0) {
                idList = new ArrayList<Long>();
                while (tokenizer.hasMoreTokens()) {
                    String strId = tokenizer.nextToken();
                    idList.add(Long.parseLong(strId));
                }
            }
        }

        return idList;
    }
}