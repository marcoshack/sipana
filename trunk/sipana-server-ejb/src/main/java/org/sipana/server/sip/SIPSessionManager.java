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
package org.sipana.server.sip;

import java.util.List;
import javax.ejb.Local;

import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPSession;

@Local
public interface SIPSessionManager {
    public void createSIPSession(SIPSession session);
    public void saveSIPSession(SIPSession session);
    public long getSIPSessionCount();
    public SIPSession getSIPSession(long id);
    public SIPSession getSIPSessionByCallID(String callID);
    public List<SIPSession> getSIPSessions(Long startTime, Long endTime, String method, String fromUser, String toUser, String callId, List<String> ipAddrList);
    public List<SIPMessage> getMessageListBySessionId(List<Long> sessionIdList);
    public List<SIPMessage> getMessageListByCallID(List<String> callIdList);
}
