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
package org.sipana.server.ejb;

import java.util.List;
import javax.ejb.Local;
import org.sipana.protocol.sip.SIPMessage;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
@Local
public interface SIPMessageManager {
    public SIPMessage find(long id);
    public List<SIPMessage> findBySessionID(Long sessionId);
    public List<SIPMessage> findBySessionID(List<Long> sessionIdList);
    public List<SIPMessage> findByCallID(String callId);
    public List<SIPMessage> findByCallID(List<String> callIdList);
    public void save(SIPMessage message);
    public void save(List<SIPMessage> messageList);
}
