
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

import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.jpcap.osgi.CaptureServiceProvider;
import net.sourceforge.jpcap.osgi.CaptureSession;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.sipana.sip.SipanaSipProvider;

public class SipanaSipActivator implements BundleActivator {
    private static BundleContext bc = null;
    private SipanaSipProviderImpl sipProvider;
    private CaptureServiceProvider captureService;
    private CaptureSession captureSession;
    private SipanaSipSessionManager sessionManager;
    private Logger logger;

    public void start(BundleContext bc) throws Exception {
        SipanaSipActivator.bc = bc;
        logger = Logger.getLogger(SipanaSipActivator.class);
        
        // Sipana SIP Session Manager
        long sessionManagerInterval = Long.parseLong(SipanaSipActivator.bc.getProperty("org.sipana.sip.sessionmanager.interval"));
        sessionManager = new SipanaSipSessionManager();
        sessionManager.setInterval(sessionManagerInterval);
        sessionManager.setServerUser(SipanaSipActivator.bc.getProperty("org.sipana.server.user"));
        sessionManager.setServerPassword(SipanaSipActivator.bc.getProperty("org.sipana.server.password"));
        sessionManager.setServerURL(SipanaSipActivator.bc.getProperty("org.sipana.server.url"));
        sessionManager.setServerQueue(SipanaSipActivator.bc.getProperty("org.sipana.sip.sessionmanager.queue"));
        sessionManager.start();
        
        // Sipana SIP Provider
        sipProvider = new SipanaSipProviderImpl();
        sipProvider.setSessionListener(sessionManager);
        
        // SIP packet capturer
        // CaptureService
        ServiceReference refCaptureService = bc.getServiceReference(CaptureServiceProvider.class.getName());
        if (refCaptureService != null) {
            captureService = (CaptureServiceProvider) bc.getService(refCaptureService);
            
            if (captureService != null) {
                captureSession = captureService.createCaptureSession();
                captureSession.setDevice("lo");
                captureSession.setFilter("udp or icmp");
                captureSession.setPromiscuous(true);
                captureSession.setListener((PacketListener) sipProvider);
                captureSession.start();
            } else {
                throw new Exception("Can't get CaptureService service");
            }
            
        } else {
            throw new Exception("Can't get CaptureServiceProvider service reference");
        }
        
        logger.info("Registering SipanaSipProvider");
        bc.registerService(SipanaSipProvider.class.getName(), 
                sipProvider, new Hashtable());
    }

    public void stop(BundleContext bc) throws Exception {
        captureSession.stop();
        captureService.destroyCaptureSession(captureSession);
        sessionManager.stopManager();
        SipanaSipActivator.bc = null;
    }
}
