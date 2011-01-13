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
package org.sipana.server.dao;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.sipana.protocol.sip.SIPMessage;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
@Stateless
public class SIPMessageManagerBean extends AbstractManagerBean implements SIPMessageManager {

    public List<SIPMessage> getMessageListBySessionId(Long sessionId) {
        List<Long> sessionIdList = new ArrayList<Long>();
        sessionIdList.add(sessionId);
        return getMessageListBySessionId(sessionIdList);
    }

    public List<SIPMessage> getMessageListBySessionId(List<Long> sessionIdList) {
        StringBuilder sbQuery = new StringBuilder("SELECT m FROM SIPMessage m INNER JOIN m.sipSession AS s WHERE s.id IN (");
        sbQuery.append("'").append(StringUtils.join(sessionIdList, "','"));
        sbQuery.append("') ORDER BY m.time");

        Query q = createQuery(sbQuery);
        return q.getResultList();
    }

    public List<SIPMessage> getMessageListByCallID(String callId) {
        ArrayList<String> callIdList = new ArrayList<String>();
        callIdList.add(callId);
        return getMessageListByCallID(callIdList);
    }

    public List<SIPMessage> getMessageListByCallID(List<String> callIdList) {
        StringBuilder sbQuery = new StringBuilder("SELECT m FROM SIPMessage m WHERE m.callId IN (");
        sbQuery.append("'").append(StringUtils.join(callIdList, "','")).append("') ORDER BY m.time");

        Query q = createQuery(sbQuery);
        return q.getResultList();
    }
}
