package org.sipana.sip.scenario;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.sipana.SipanaProperties;
import org.sipana.SipanaPropertyType;
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
	private static final int MSG_STEP_H = 26;
	private static final List<Color> colors = createColorList();
	private int currColor = 0;
	private int imgWidth;
	private int imgHeight;
	private List<SIPMessage> messages;
	private Map<String, Color> sessionColors;
	
	// avoid unnecessary instantiation
	public SIPScenario(List<SIPMessage> messages) {
		this.messages = messages;
		sessionColors = new HashMap<String, Color>();
	}
	
	/**
	 * Create SIP Scenario diagram and save it as a JPEG image file.
	 * 
	 * @param List of messages to create SIP scenario diagram
	 * @param OutputStream to write the encoded JPEG image
	 * @author mhack
	 */
	public void create(OutputStream outputStream) throws Exception {
		HashMap<String, Integer> hostList = createHostListWithWeightPosition(messages);
		
		imgWidth = (HOST_STEP_W * hostList.size()) + HOST_INIT_W;
		imgHeight = (MSG_STEP_H * messages.size()) + (IMAGE_BORDER * 3); 
		
		BufferedImage buffImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graph = buffImage.createGraphics();
		graph.setBackground(Color.WHITE);
		graph.clearRect(1, 1, imgWidth-2, imgHeight-2);
		
		drawMessages(graph, messages, hostList);
		drawHosts(graph, imgHeight, hostList);
		
		StringBuilder sbFilename = new StringBuilder("sip_callflow-");
		sbFilename.append(UUID.randomUUID()).append(".jpg");
		
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
		encoder.encode(buffImage);
	}

	private void drawMessages(Graphics2D graph,
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

			// draw session's background color
			Color msgBkgrdColor = getSessionColor(message.getCallID());
			graph.setColor(msgBkgrdColor);
			int x1 = IMAGE_BORDER;
			int y1 = h - (MSG_STEP_H/2);
			int x2 = imgWidth - IMAGE_BORDER;
			int y2 = h + (MSG_STEP_H/2);
			Rectangle msgColorBox = new Rectangle(x1,y1,x2,y2);
			graph.fill(msgColorBox);
			graph.draw(msgColorBox);
			
			graph.setColor(Color.BLACK);
			
			// set start time with first message's time
			if (startTime == null) {
				startTime = message.getTime();
			}
			
			// relative time stamp
			long relativeTime = message.getTime() - startTime;
			graph.drawString(String.valueOf(relativeTime), IMAGE_BORDER, h+5);
			
			// arrow line
			graph.drawLine(srcWPos, h, dstWPos, h);
			
			// arrow
			int arrowWidth = SipanaProperties.getPropertyInt(SipanaPropertyType.SIPSCENARIO_ARROW_WIDTH);
			int arrowHeight = SipanaProperties.getPropertyInt(SipanaPropertyType.SIPSCENARIO_ARROW_HEIGHT);
			Polygon arrow = new Polygon();
			arrow.addPoint(dstWPos, h);
			int wInc = (dstWPos > srcWPos) ? -arrowWidth : arrowWidth;
			arrow.addPoint(dstWPos + wInc, h - arrowHeight);
			arrow.addPoint(dstWPos + wInc, h + arrowHeight);
			graph.fillPolygon(arrow);
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

	private void drawHosts(Graphics2D graph, int height,
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

	private HashMap<String, Integer> createHostListWithWeightPosition(
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
	
	private Color getSessionColor(String callId) {
		Color color;

		// Get color for session with the specified callId, otherwise get the 
		// next available color and associate it to this callId.
		if (sessionColors.containsKey(callId)) {
			color = sessionColors.get(callId);
		} else {
			// Restart current color if all available was used 
			if (currColor == colors.size()) {
				currColor = 0;
			}
			
			color = colors.get(currColor++);
			sessionColors.put(callId, color);
		}
		
		return color;
	}
	
	private static List<Color> createColorList() {
		String strColorList = SipanaProperties.getProperty(SipanaPropertyType.SIPSCENARIO_COLORS);
		String colorList[] = strColorList.split(",");
		
		List<Color> colors = new ArrayList<Color>();
		
		for (String strColor : colorList) {
			int rgbColor = Integer.parseInt(strColor, 16);
			Color color = new Color(rgbColor);
			colors.add(color);
		}
		
		return colors;
	}
}
