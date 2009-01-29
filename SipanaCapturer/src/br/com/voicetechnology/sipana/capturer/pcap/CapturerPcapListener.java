package br.com.voicetechnology.sipana.capturer.pcap;

import org.apache.log4j.Logger;

import br.com.voicetechnology.sipana.capturer.Event;
import br.com.voicetechnology.sipana.capturer.EventHandler;
import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.jpcap.net.Packet;
import net.sourceforge.jpcap.net.UDPPacket;

public class CapturerPcapListener implements PacketListener {
	private Logger logger;
	private EventHandler handler;
	
	public CapturerPcapListener(EventHandler handler) {
		this.handler = handler;
		logger = Logger.getLogger(CapturerPcapListener.class);
	}
	
	public void packetArrived(Packet packet) {
		logger.debug("Packet arrived");
		
		UDPPacket udp_packet = (UDPPacket) packet;
		Event event = new Event();
		event.setData(udp_packet.getUDPData());
		event.setDate(packet.getTimeval().getDate());
		event.setSrcIpAddr(udp_packet.getSourceAddress());
		event.setDstIpAddr(udp_packet.getDestinationAddress());
		
		handler.processEvent(event);
	}
}