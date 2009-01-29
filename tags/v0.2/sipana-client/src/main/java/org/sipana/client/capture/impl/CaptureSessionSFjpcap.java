package org.sipana.client.capture.impl;

import net.sourceforge.jpcap.capture.PacketCapture;
import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.jpcap.net.TCPPacket;
import net.sourceforge.jpcap.net.UDPPacket;

import org.sipana.client.capture.AbstractCaptureSession;
import org.sipana.client.capture.CaptureException;
import org.sipana.client.capture.CaptureListener;
import org.sipana.client.capture.CaptureSession;
import org.sipana.client.capture.Packet;

/**
 * Capture Session implementation using the SourceForge jpcap project [1]. This
 * project seems to be no longer maintained, use the class {@code
 * CaptureSessionKFujii} instead.
 * 
 * [1] http://jpcap.sourceforge.net/
 * 
 * @author mhack
 */
@Deprecated
public class CaptureSessionSFjpcap extends AbstractCaptureSession implements
        CaptureSession, PacketListener, Runnable {

    public static int DEFAULT_SNAPLEN = 1500;
    public static int DEFAULT_TIMEOUT = -1;
    public static int DEFAULT_COUNT = -1; // Infinite
    
    private PacketCapture capturer;

    public CaptureSessionSFjpcap(String filter, String device,
            CaptureListener listener) {
        super(filter, device, listener);
        capturer = new PacketCapture();
    }

    @Override
    protected void startCapture() throws CaptureException {
        try {
            capturer.open(getDevice(), CaptureSessionSFjpcap.DEFAULT_SNAPLEN, isPromiscuous(), CaptureSessionSFjpcap.DEFAULT_TIMEOUT);

            capturer.setFilter(getFilter(), true);
            capturer.addPacketListener(this);
            capturer.capture(CaptureSessionSFjpcap.DEFAULT_COUNT);

        } catch (Exception e) {
            throw new CaptureException("Fail starting capture session", e);
        }
    }

    @Override
    protected void stopCapture() throws CaptureException {
        try {
            capturer.endCapture();

        } catch (Exception e) {
            throw new CaptureException("Fail stopping capture session", e);
        }
    }

    public void packetArrived(net.sourceforge.jpcap.net.Packet jpcapPacket) {
        try {
            Packet packet = new Packet();

            if (jpcapPacket instanceof UDPPacket) {
                UDPPacket udpPacket = (UDPPacket) jpcapPacket;

                packet.setDate(udpPacket.getTimeval().getDate());
                packet.setData(udpPacket.getData().toString());
                packet.setSrcIPAddr(udpPacket.getSourceAddress());
                packet.setSrcIPPort(udpPacket.getSourcePort());
                packet.setDstIPAddr(udpPacket.getDestinationAddress());
                packet.setDstIPPort(udpPacket.getDestinationPort());

            } else if (jpcapPacket instanceof TCPPacket) {
                TCPPacket tcpPacket = (TCPPacket) jpcapPacket;
                
                packet.setDate(tcpPacket.getTimeval().getDate());
                packet.setData(tcpPacket.getData().toString());
                packet.setSrcIPAddr(tcpPacket.getSourceAddress());
                packet.setSrcIPPort(tcpPacket.getSourcePort());
                packet.setDstIPAddr(tcpPacket.getDestinationAddress());
                packet.setDstIPPort(tcpPacket.getDestinationPort());
                
            } else {
                throw new CaptureException("Unknown packet type:\n" + jpcapPacket);
            }

            getListener().onPacket(packet);

        } catch (Exception e) {
            logger.error("Fail processing packet", e);
        }
    }
}
