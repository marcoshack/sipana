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

import org.sipana.agent.service.ServiceLocator;
import org.apache.log4j.Logger;
import org.sipana.agent.capture.CaptureManager;
import org.sipana.agent.capture.CaptureSession;
import org.sipana.agent.config.ConfigException;
import org.sipana.agent.config.ConfigManager;
import org.sipana.agent.sender.Sender;
import org.sipana.agent.sip.SIPHandler;

public class SipanaAgent implements Runnable {

    public static final int STATUS_UNKNOWN_ERROR = -1;
    public static final int STATUS_IDLE = 0;
    public static final int STATUS_CONFIG_ERROR = 1;

    private ServiceLocator serviceLocator;
    private Integer status;
    private ConfigManager configManager;
    private Sender sender;
    private SIPHandler sipHandler;
    private CaptureManager captureManager;

    private Logger logger = Logger.getLogger(SipanaAgent.class);

    public void start() throws Exception {
        serviceLocator = ServiceLocator.getInstance();
        configManager = serviceLocator.getConfigManager();
        sender = serviceLocator.getSender();
        sipHandler = serviceLocator.getSIPHandler(sender);
        captureManager = serviceLocator.getCaptureManager();

        String filter = configManager.getCaptureFilter();
        String device = configManager.getCaptureInterface();
        CaptureSession capSession = captureManager.createCaptureSession(filter, device, sipHandler);
        capSession.start();

        logger.debug("Waiting capture session to start...");
        Thread.sleep(3000);

        if (capSession.getState() == CaptureSession.STATE_RUNINNG) {
            logger.info("Capture session sucessful started");
            sender.start();

        } else {
            logger.error("Couldn't start capture session");
        }
    }

    public void stop() throws Exception {
        logger.info("Stopping Sipana Agent");
        
        logger.info("Stopping Capture Sessions");
        for (CaptureSession capSession : captureManager.getCaptureSessionList()) {
            capSession.stop();
            captureManager.removeCaptureSession(capSession);
        }
        logger.info("Capture Sessions stopped");

        logger.info("Stopping Message Sender");
        sender.stop();
        logger.info("Message Sender stopped");

        logger.info("Sipana Agent stopped");
    }

    public Integer getStatus() {
        return status;
    }

    private void setStatus(Integer status) {
        this.status = status;
    }

    public void run() {
        try {
            Thread.currentThread().setName("SipanaAgent");
            start();

        } catch (ConfigException e) {
            logger.error("Fail configuring client", e);
            setStatus(STATUS_CONFIG_ERROR);

        } catch (Exception e) {
            logger.error("Fail running SipanaClient", e);
            setStatus(SipanaAgent.STATUS_UNKNOWN_ERROR);
        }
    }
}
