package net.sourceforge.sipana.capturer;

import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.jpcap.net.Packet;

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
        service = (CaptureServiceProvider)bc.getService(ref);
        
        if (service != null) {
            capturer = service.createCaptureSession();
            capturer.setDevice("eth1");
            capturer.setFilter("udp or icmp");
            capturer.setPromiscuous(true);
            capturer.setListener(this);
            capturer.start();
        } else {
            throw new Exception("Can not get CaptureService");
        }
    }

    public void stop(BundleContext bc) throws Exception {
        capturer.stop();
        capturer = null;
        bc.ungetService((ServiceReference)service);
        service = null;
    }

    public void packetArrived(Packet packet) {
        StringBuilder sb = new StringBuilder(packet.getTimeval().toString());
        sb.append(" ");
        sb.append(packet.toColoredString(true));
        
        System.out.println(sb);
    }
}
