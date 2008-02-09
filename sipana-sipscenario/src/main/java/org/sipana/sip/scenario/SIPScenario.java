package org.sipana.sip.scenario;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPResponse;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class SIPScenario {
	private static final int IMAGE_BORDER = 20;
	private static final int HOST_INIT_W  = 80;
	private static final int HOST_INIT_H  = 20;
	private static final int HOST_STEP_W  = 150;
	private static final int HOST_LINE_OFFSET_W = 54;
	private static final int HOST_LINE_OFFSET_H = 8;
	private static final int MSG_STEP_H = 20;
	
	// avoid unnecessary instantiation
	private SIPScenario() {}
	
	/**
	 * Create SIP Scenario diagram and save it as a JPEG image file.
	 * 
	 * @param List of messages to create SIP scenario diagram
	 * @param OutputStream to write the encoded JPEG image
	 * @author mhack
	 */
	public static void createSIPScenario(List<SIPMessage> messages, OutputStream outputStream)
		throws Exception 
	{
		HashMap<String, Integer> hostList = createHostListWithWeightPosition(messages);
		
		int imgWidth = (HOST_STEP_W * hostList.size()) + HOST_INIT_W;
		int imgHeight = (MSG_STEP_H * messages.size()) + (IMAGE_BORDER * 3); 
		
		BufferedImage buffImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graph = buffImage.createGraphics();
		graph.setBackground(Color.WHITE);
		graph.clearRect(1, 1, imgWidth-2, imgHeight-2);
		
		drawHosts(graph, imgHeight, hostList);
		
		drawMessages(graph, messages, hostList);
		
		StringBuilder sbFilename = new StringBuilder("sip_callflow-");
		sbFilename.append(UUID.randomUUID()).append(".jpg");
		
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
		encoder.encode(buffImage);
	}

	private static void drawMessages(Graphics2D graph,
			List<SIPMessage> messages, HashMap<String, Integer> hostList) 
	{
		// stroke for message's continuous line
		float dash[] = null;
		BasicStroke bs = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, 
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		graph.setStroke(bs);
		
		int h = HOST_INIT_H * 2;
		Long startTime = null;
		
		for (SIPMessage message : messages) {
			String srcAddr = message.getSrcAddress();
			String dstAddr = message.getDstAddress();
			int srcWPos = hostList.get(srcAddr);
			int dstWPos = hostList.get(dstAddr);
			
			// set start time with first message's time
			if (startTime == null) {
				startTime = message.getTime();
			}
			long relativeTime = message.getTime() - startTime;
			
			// relative time stamp
			graph.drawString(String.valueOf(relativeTime), IMAGE_BORDER, h+5);
			
			// line
			graph.drawLine(srcWPos, h, dstWPos, h);
			
			// arrow
			Polygon arrow = new Polygon();
			arrow.addPoint(dstWPos, h);
			int wInc = (dstWPos > srcWPos) ? -8 : 8;
			arrow.addPoint(dstWPos + wInc, h - 3);
			arrow.addPoint(dstWPos + wInc, h + 3);
			graph.drawPolygon(arrow);
			
			// text
			String text = null;
			if (message instanceof SIPRequest) {
				text = ((SIPRequest)message).getMethod();
			} else {
				text = ((SIPResponse)message).getStatusCode() + " " + ((SIPResponse)message).getReasonPhrase();  
			}
			
			int stepCenter = HOST_STEP_W / 2;
			int textWCenter = dstWPos + ((dstWPos > srcWPos) ? -stepCenter : stepCenter );
			int textWPos = textWCenter - HOST_STEP_W / 4;
			graph.drawString(text, textWPos, h - 2);
			
			h += MSG_STEP_H;
		}
	}

	private static void drawHosts(Graphics2D graph, int height,
			HashMap<String, Integer> hostList) 
	{
		graph.setColor(Color.BLACK);
		graph.drawString("Time", IMAGE_BORDER, HOST_INIT_H);
		
		// stroke for host's dashed line
		float dash[] = { 3.0f };
		BasicStroke bs = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, 
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		graph.setStroke(bs);
		
		for (String hostAddr : hostList.keySet()) {
			int lineWeight = hostList.get(hostAddr);
			graph.drawString(hostAddr, (lineWeight - HOST_LINE_OFFSET_W), HOST_INIT_H);
			graph.drawLine(lineWeight, HOST_INIT_H + HOST_LINE_OFFSET_H, lineWeight, height - HOST_INIT_H);
		}
	}

	private static HashMap<String, Integer> createHostListWithWeightPosition(
			List<SIPMessage> messages) 
	{
		HashMap<String, Integer> hostList = new HashMap<String, Integer>();
		int lineWeigh = HOST_INIT_W + HOST_LINE_OFFSET_W;
		
		for (SIPMessage message : messages) {
			String srcAddr = message.getSrcAddress();
			String dstAddr = message.getDstAddress();
			
			if (hostList.get(srcAddr) == null) {
				hostList.put(srcAddr, lineWeigh);
				lineWeigh += HOST_STEP_W;
			}
			
			if (hostList.get(dstAddr) == null) {
				hostList.put(dstAddr, lineWeigh);
				lineWeigh += HOST_STEP_W;
			}
		}

		return hostList;
	}
}
