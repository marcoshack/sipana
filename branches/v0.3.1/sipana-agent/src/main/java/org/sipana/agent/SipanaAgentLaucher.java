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
package org.sipana.agent;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class SipanaAgentLaucher {

    public static void main(String[] args) {
        SipanaAgent agent = new SipanaAgent();
        
        SipanaAgentSignalHandler sigHandler = new SipanaAgentSignalHandler(agent);
        Runtime.getRuntime().addShutdownHook(new Thread(sigHandler));

        Thread agentThread = new Thread(agent);
        agentThread.start();
    }
}
