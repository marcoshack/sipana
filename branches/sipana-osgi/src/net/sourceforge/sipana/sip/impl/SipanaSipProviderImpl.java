package net.sourceforge.sipana.sip.impl;

import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.jpcap.net.Packet;
import net.sourceforge.sipana.sip.SipanaSipProvider;

import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

public class SipanaSipProviderImpl implements SipanaSipProvider,
        PacketListener, ServiceListener 
{

    public void packetArrived(Packet packet) {
        // TODO Auto-generated method stub

    }

    public void serviceChanged(ServiceEvent event) {
        // TODO Auto-generated method stub

    }

}
