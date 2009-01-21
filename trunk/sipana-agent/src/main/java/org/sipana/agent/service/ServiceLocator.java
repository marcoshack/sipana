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
package org.sipana.agent.service;

import org.sipana.agent.capture.CaptureManager;
import org.sipana.agent.capture.impl.CaptureManagerImpl;
import org.sipana.agent.config.ConfigManager;
import org.sipana.agent.sender.MessageSender;
import org.sipana.agent.sip.SIPHandler;
import org.sipana.protocol.sip.SIPFactory;

public class ServiceLocator {
    private static ServiceLocator instance;
    
    private ServiceLocator() {
    }
    
    public static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        
        return instance;
    }
    
    public MessageSender getSIPSessionSender() throws Exception {
        return new MessageSender();
    }
    
    public SIPHandler getSIPHandler(MessageSender sender) {
        return new SIPHandler(sender);
    }
    
    public CaptureManager getCaptureManager() {
    	return CaptureManagerImpl.getInstance();
    }

    public ConfigManager getConfigManager() throws Exception {
        return ConfigManager.getInstance();
    }
    
    public SIPFactory getSIPFactory() throws Exception {
        return SIPFactory.getInstance();
    }
}
