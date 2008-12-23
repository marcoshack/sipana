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
package org.sipana.client;

import org.apache.log4j.Logger;
import org.sipana.client.capture.CaptureManager;
import org.sipana.client.capture.CaptureSession;
import org.sipana.client.config.ConfigException;
import org.sipana.client.config.ConfigManager;
import org.sipana.client.sender.MessageSender;
import org.sipana.client.sip.SIPHandler;

public class SipanaClient {
	public static final int EXIT_STATUS_UNKNOWN_ERROR = -1;
	public static final int EXIT_STATUS_CONFIG_ERROR = 1;
	
    private static ServiceLocator serviceLocator;
	private static Logger logger = Logger.getLogger(SipanaClient.class);
	
	public static void main(String[] args) {
        Thread.currentThread().setName("SipanaClient");
        
        ConfigManager configManager = null;
		MessageSender sender = null;
		SIPHandler handler = null;
		CaptureManager captureManager = null;
        
		try {
            serviceLocator = ServiceLocator.getInstance();
            
            configManager  = serviceLocator.getConfigManager();
            sender         = serviceLocator.getSIPSessionSender();
            handler        = serviceLocator.getSIPHandler(sender);
            captureManager = serviceLocator.getCaptureManager();

            String filter = configManager.getCaptureFilter();
            String device = configManager.getCaptureInterface();
			CaptureSession capSession = captureManager.createCaptureSession(filter, device, handler);
			capSession.start();

			logger.debug("Waiting capture session to start...");
			Thread.sleep(3000);
			
			if (capSession.getState() == CaptureSession.STATE_RUNINNG) {
                logger.info("Capture session sucessful started");
			    sender.start();
                
			} else {
			    logger.error("Couldn't start capture session");
			}
            
		} catch (ConfigException e) {
			logger.error("Fail configuring client", e);
			System.exit(SipanaClient.EXIT_STATUS_CONFIG_ERROR);
		
		} catch (Exception e) {
			logger.error("Fail running SipanaClient", e);
			System.exit(SipanaClient.EXIT_STATUS_UNKNOWN_ERROR);
		}
	}
}
