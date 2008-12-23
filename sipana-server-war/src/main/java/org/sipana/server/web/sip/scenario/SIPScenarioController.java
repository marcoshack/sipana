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
package org.sipana.server.web.sip.scenario;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.sipana.protocol.sip.SIPMessage;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.sip.SIPSessionManager;
import org.sipana.sip.scenario.SIPScenario;

public class SIPScenarioController {

    private List<SIPMessage> messageList;

    //list all SIP sessions
    private String[] callId = null;
    private String[] sessionId = null;

    private SIPSessionManager sipSessionManager;

    public SIPScenarioController() {
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        sipSessionManager = (SIPSessionManager) serviceLocator.getService(Service.SIP_SESSION_MANAGER);
    }

    public void show() throws Exception {
        if (sessionId != null) {

            List<Long> items = new ArrayList<Long>();

            for (String id : sessionId) {
                items.add(Long.parseLong(id));
            }

            if (items.size() > 0) {
                messageList = sipSessionManager.getMessageListBySessionId(items);
            }
            
        } else if (callId != null) {

            List<String> items = new ArrayList<String>();

            for (String id : callId) {
                items.add(id);
            }

            if (items.size() > 0) {
                messageList = sipSessionManager.getMessageListByCallID(items);
            }
        }

        if (messageList != null) {

            HttpServletResponse response = 
                (HttpServletResponse) FacesContext.getCurrentInstance()
                                            .getExternalContext().getResponse();

            OutputStream outputStream = response.getOutputStream();
            SIPScenario scenario = new SIPScenario(messageList);
            scenario.create(outputStream);

            FacesContext.getCurrentInstance().responseComplete();
        }

    }

    public void setSessionId(String[] sessionId) {
        this.sessionId = sessionId;
    }

    public String[] getSessionId() {
        return sessionId;
    }

    public void setCallId(String[] callId) {
        this.callId = callId;
    }

    public String[] getCallId() {
        return callId;
    }
}
