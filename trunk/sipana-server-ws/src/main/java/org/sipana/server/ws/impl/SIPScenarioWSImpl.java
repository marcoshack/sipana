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

import org.apache.log4j.Logger;
import org.sipana.server.ws.SIPScenarioWS;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class SIPScenarioWSImpl implements SIPScenarioWS {

    private Logger logger = Logger.getLogger(SIPScenarioWSImpl.class);

    public String getSIPScenario(String sessionList) {
        // TODO [mhack] fake method to test resteasy

        String[] idList = null;
        StringBuilder sbList = new StringBuilder();

        if (sessionList != null) {
            idList = sessionList.split(",");
            sbList = new StringBuilder("{ ");

            for (String id : idList) {
                sbList.append(id).append(" ");
            }

            sbList.append(" }");

        } else {
            sbList.append("sessionId undefined");
        }

        return sbList.toString();
    }
}