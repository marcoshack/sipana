package net.sourceforge.sipana.sip.impl;

import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.jpcap.net.Packet;
import net.sourceforge.sipana.sip.SipanaSipProvider;

public class SipanaSipProviderImpl implements SipanaSipProvider,
        PacketListener 
{
    public SipanaSipProviderImpl() {
        
    }
    
    public void packetArrived(Packet packet) {

    }
}
