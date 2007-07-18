
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
import net.sourceforge.jpcap.osgi.CaptureServiceProvider;
import net.sourceforge.jpcap.osgi.CaptureSession;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class TestCaptureService implements BundleActivator, PacketListener 
{
    private CaptureServiceProvider service;
    private CaptureSession capturer;

    public void start(BundleContext bc) throws Exception {
        String strService = CaptureServiceProvider.class.getName();
        ServiceReference ref = bc.getServiceReference(strService);
        
        if (ref != null) {
            service = (CaptureServiceProvider)bc.getService(ref);
            
            if (service != null) {
                capturer = service.createCaptureSession();
                capturer.setDevice("eth0");
                capturer.setFilter("udp or icmp");
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
        StringBuilder sb = new StringBuilder(packet.getTimeval().toString());
        sb.append(" ");
        sb.append(packet.toColoredString(true));
        
        System.out.println(sb);
    }
    
    @Override
    public String toString() {
        return "TestCaptureService";
    }
    
}
