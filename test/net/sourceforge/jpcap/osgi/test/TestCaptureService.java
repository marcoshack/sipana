
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

package net.sourceforge.jpcap.osgi.test;

import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.jpcap.net.Packet;
import net.sourceforge.jpcap.net.UDPPacket;
import net.sourceforge.jpcap.osgi.CaptureServiceProvider;
import net.sourceforge.jpcap.osgi.CaptureSession;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class TestCaptureService implements BundleActivator, PacketListener 
{
    private CaptureServiceProvider service;
    private CaptureSession capturer;
    private Logger logger;

    public void start(BundleContext bc) throws Exception {
        logger = Logger.getLogger(TestCaptureService.class);
        
        String strService = CaptureServiceProvider.class.getName();
        ServiceReference ref = bc.getServiceReference(strService);
        
        if (ref != null) {
            String iface  = bc.getProperty("net.sourceforge.jpcap.test.interface");
            String filter = bc.getProperty("net.sourceforge.jpcap.test.filter");
            service = (CaptureServiceProvider)bc.getService(ref);
            
            if (service != null && iface != null && filter != null) {
                capturer = service.createCaptureSession();
                capturer.setDevice(iface);
                capturer.setFilter(filter);
                capturer.setPromiscuous(true);
                capturer.setListener(this);
                capturer.start();
            } else {
                throw new Exception("Can not get CaptureService");
            }
            
        } else {
            throw new Exception("Can not get CaptureServiceProvider service reference");
        }
        
    }

    public void stop(BundleContext bc) throws Exception {
        capturer.stop();
        
        String strService = CaptureServiceProvider.class.getName();
        ServiceReference ref = bc.getServiceReference(strService);
        bc.ungetService((ServiceReference)ref);

        capturer = null;
        service = null;
    }

    public void packetArrived(Packet packet) {
        
        if (packet instanceof UDPPacket) {
            UDPPacket udpPacket = (UDPPacket) packet;
            
            StringBuilder sb = new StringBuilder(udpPacket.getTimeval().toString());
            sb.append(" ");
            sb.append(udpPacket.toColoredVerboseString(true));
            
            logger.debug(sb);
        } else {
            logger.debug("Packet isn't a UDP packet. Nothing to do");
        }
    }
    
    @Override
    public String toString() {
        return "TestCaptureService";
    }
    
}
