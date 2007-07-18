
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

package org.sipana.sip.test;

import java.text.ParseException;

import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.jpcap.net.Packet;
import net.sourceforge.jpcap.osgi.CaptureServiceProvider;
import net.sourceforge.jpcap.osgi.CaptureSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.sipana.sip.SIPMessageInfo;
import org.sipana.sip.SIPRequestInfo;
import org.sipana.sip.SIPResponseInfo;
import org.sipana.sip.SipanaSipProvider;

public class TestSipanaSipProvider implements BundleActivator, PacketListener {

    private CaptureServiceProvider captureService;
    private SipanaSipProvider sipanaSipService;
    private CaptureSession capturer;
    private Log logger;
    
    public void start(BundleContext bc) throws Exception {
        logger = LogFactory.getLog(TestSipanaSipProvider.class.getName());
        
        // SipanaSipProvider
        ServiceReference refSipanaSip = bc.getServiceReference(SipanaSipProvider.class.getName());
        if (refSipanaSip != null) {
            sipanaSipService = (SipanaSipProvider) bc.getService(refSipanaSip);

            if (sipanaSipService == null) {
                throw new Exception("Can't get SipanaSipProvider service");
            }
        } else {
            throw new Exception("Can't get SipanaSipProvider service reference");
        }

        // CaptureService
        ServiceReference refCaptureService = bc.getServiceReference(CaptureServiceProvider.class.getName());
        if (refCaptureService != null) {
            captureService = (CaptureServiceProvider) bc.getService(refCaptureService);
            
            if (captureService != null) {
                capturer = captureService.createCaptureSession();
                capturer.setDevice("lo");
                capturer.setFilter("udp or icmp");
                capturer.setPromiscuous(true);
                capturer.setListener(this);
                capturer.start();
            } else {
                throw new Exception("Can't get CaptureService service");
            }
            
        } else {
            throw new Exception("Can't get CaptureServiceProvider service reference");
        }

    }

    public void stop(BundleContext bc) throws Exception {
        capturer.stop();
        capturer = null;
        
        ServiceReference refCaptureService = bc.getServiceReference(CaptureServiceProvider.class.getName());
        bc.ungetService((ServiceReference)refCaptureService);
        captureService = null;

        ServiceReference refSipanaSipService = bc.getServiceReference(SipanaSipProvider.class.getName());
        bc.ungetService(refSipanaSipService);
        sipanaSipService = null;
    }

    public void packetArrived(Packet packet) {
        try {
            SIPMessageInfo message = sipanaSipService.getMessageFactory().createMessage(packet.getData());
            
            if (message instanceof SIPRequestInfo) {
                logger.info("Processing Request " + ((SIPRequestInfo)message).getMethod());
                sipanaSipService.processRequest((SIPRequestInfo) message);
            } else if (message instanceof SIPResponseInfo) {
                logger.info("Processing Response " + ((SIPResponseInfo)message).getStatusCode() + " " +((SIPResponseInfo)message).getReasonPhrase());
                sipanaSipService.processResponse((SIPResponseInfo) message);
            }
            
            logger.info("Current Session number: " + sipanaSipService.getCurrentSessionNumber());
            logger.info("Terminated Session number: " + sipanaSipService.getTerminatedSessionNumber());
            
        } catch (ParseException e) {
            logger.error("Fail processing packet", e);
        }
    }

}
