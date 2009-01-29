
/**
 * This file is part of Sipana project <http://sipana.org/>
 * 
 * Sipana is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sipana is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */ 

package org.sipana.sip.impl;

import java.util.Hashtable;

import net.sourceforge.jpcap.osgi.CaptureServiceProvider;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.sipana.sip.SIPHandler;
import org.sipana.sip.SipanaSIPProvider;

public class SipanaSipActivator implements BundleActivator {
    private static BundleContext bc = null;
    private SipanaSIPProvider sipProvider;
    private CaptureServiceProvider captureService;
    private Logger logger;

    public void start(BundleContext bc) throws Exception {
        SipanaSipActivator.bc = bc;
        logger = Logger.getLogger(SipanaSipActivator.class);

        // Config parameters
        long sessionManagerInterval = Long.parseLong(SipanaSipActivator.bc.getProperty("org.sipana.sip.sessionmanager.interval"));
        String serverUser = SipanaSipActivator.bc.getProperty("org.sipana.server.user");
        String serverPassword = SipanaSipActivator.bc.getProperty("org.sipana.server.password");
        String serverURL = SipanaSipActivator.bc.getProperty("org.sipana.server.url");
        String serverQueueName = SipanaSipActivator.bc.getProperty("org.sipana.sip.sessionmanager.queue");
        String captureInterface = SipanaSipActivator.bc.getProperty("org.sipana.sip.capture.interface");
        String captureFilter = SipanaSipActivator.bc.getProperty("org.sipana.sip.capture.filter");
        
        // Sipana SIP Provider
        sipProvider = new SipanaSIPProviderImpl();
        
        // Sipana SIP Session Manager
        SipanaSIPSessionManager sessionManager = sipProvider.getSIPSessionManager();
        sessionManager.setInterval(sessionManagerInterval);
        sessionManager.setServerUser(serverUser);
        sessionManager.setServerPassword(serverPassword);
        sessionManager.setServerURL(serverURL);
        sessionManager.setServerQueue(serverQueueName);
        sessionManager.start();
        
        // SIP Handler
        SIPHandler sipHandler = sipProvider.getSIPHandler();
        sipHandler.setSessionListener(sessionManager);
        
        // CaptureServiceProvider
        ServiceReference refCaptureService = bc.getServiceReference(CaptureServiceProvider.class.getName());
        if (refCaptureService != null) {
            captureService = (CaptureServiceProvider) bc.getService(refCaptureService);
            
            if (captureService != null) {
                sipProvider.setCaptureServiceProvider(captureService);
                sipProvider.addCaptureSession(captureInterface, captureFilter);
            } else {
                throw new Exception("Can't get CaptureService service");
            }
            
        } else {
            throw new Exception("Can't get CaptureServiceProvider service reference. Sipana SIP Provider cannot start");
        }

        // Register service
        logger.info("Registering SipanaSIPProvider");
        bc.registerService(SipanaSIPProvider.class.getName(), sipProvider, new Hashtable());
    }

    public void stop(BundleContext bc) throws Exception {
        captureService.destroyCaptureSessions();
        sipProvider.getSIPSessionManager().stopManager();
        SipanaSipActivator.bc = null;
    }
}
