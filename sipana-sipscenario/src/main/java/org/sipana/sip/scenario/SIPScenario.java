/**
 * This file is part of Sipana project <http://sipana.org/>
 *
 * Sipana is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * Sipana is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

import org.sipana.SipanaProperties;
import org.sipana.SipanaPropertyType;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPResponse;

import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class SIPScenario {

    private static final int IMAGE_BORDER = 20;
    private static final int HOST_INIT_W = 80;
    private static final int HOST_INIT_H = 20;
    private static final int HOST_STEP_W = 150;
    private static final int HOST_LINE_OFFSET_W = 54;
    private static final int HOST_LINE_OFFSET_H = 8;
    private static final int HOST_LINE_TO_MSG_BOX_OFFSET_H = 5;
    private static final int MSG_STEP_H = 26;
    private static final List<Color> colors = createColorList();
    private static final String DEFAULT_IMAGE_FORMAT = "png";
    private String imageFormat;
    private int currColor = 0;
    private int imgWidth;
    private int imgHeight;
    private List<SIPMessage> messages;
    private Map<String, Color> sessionColors;
    private HashMap<String, Integer> hostList;

    public SIPScenario(List<SIPMessage> messages) {
        this(messages, DEFAULT_IMAGE_FORMAT);
    }

    public SIPScenario(List<SIPMessage> messages, String format) {
        hostList = createHostListWithWeightPosition(messages);
        imageFormat = format;
        sessionColors = new HashMap<String, Color>();
    }

    /**
     * Create SIP Scenario diagram and save it as a JPEG image file.
     * 
     * @param List of messages to create SIP scenario diagram
     * @param OutputStream to write the encoded JPEG image to
     * @author Marcos Hack <marcoshack@gmail.com>
     */
    public void create(OutputStream outputStream) throws IOException {
        BufferedImage buffImage;

        if (messages.size() > 0) {
            imgWidth = (HOST_STEP_W * hostList.size()) + HOST_INIT_W;
            imgHeight = (MSG_STEP_H * messages.size()) + (IMAGE_BORDER * 3);

            buffImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graph = buffImage.createGraphics();
            graph.setBackground(Color.WHITE);
            graph.clearRect(1, 1, imgWidth - 2, imgHeight - 2);
            drawMessages(graph, messages, hostList);
            drawHosts(graph, imgHeight, hostList);
            
        } else {
            // Empty SIPScenario, only a warning about that
            imgWidth = 400;
            imgHeight = 200;

            buffImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graph = buffImage.createGraphics();
            graph.setBackground(Color.WHITE);
            graph.clearRect(1, 1, imgWidth - 2, imgHeight - 2);
            graph.setColor(Color.BLACK);
            graph.drawString("Empty message list. Unable to draw SIP scenario.", 50, imgHeight/2);
        }

        ImageIO.write(buffImage, imageFormat, outputStream);
    }

    private void drawMessages(Graphics2D graph, List<SIPMessage> messages,
            HashMap<String, Integer> hostList) {
        // stroke for message's continuous line
        float dash[] = null;
        BasicStroke bs = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        graph.setStroke(bs);

        // Height to start to drawing the colored message boxes: Below the host
        // list and a little bit (HOST_LINE_TO_MSG_BOX_OFFSET_H) bellow the host
        // vertical stroke line
        int h = HOST_INIT_H * 2 + HOST_LINE_TO_MSG_BOX_OFFSET_H;

        Long startTime = null;
        int msgBoxWidth = imgWidth - (IMAGE_BORDER * 2);

        for (SIPMessage message : messages) {
            String srcAddr = new StringBuilder(message.getSrcAddress()).append(":").append(message.getSrcPort()).toString();
            String dstAddr = new StringBuilder(message.getDstAddress()).append(":").append(message.getDstPort()).toString();
            int srcWPos = hostList.get(srcAddr);
            int dstWPos = hostList.get(dstAddr);

            // draw session's background color
            Color msgBkgrdColor = getSessionColor(message.getCallID());
            graph.setColor(msgBkgrdColor);
            int yBox = h - (MSG_STEP_H / 2);
            Rectangle msgColorBox = new Rectangle(IMAGE_BORDER, yBox, msgBoxWidth, MSG_STEP_H);
            graph.fill(msgColorBox);
            graph.draw(msgColorBox);

            graph.setColor(Color.BLACK);

            // set start time with first message's time
            if (startTime == null) {
                startTime = message.getTime();
            }

            // relative time stamp
            long relativeTime = message.getTime() - startTime;
            graph.drawString(String.valueOf(relativeTime), IMAGE_BORDER, h + 5);

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
                text = ((SIPRequest) message).getMethod();
            } else {
                text = ((SIPResponse) message).getStatusCode() + " " + ((SIPResponse) message).getReasonPhrase();
            }

            int stepCenter = HOST_STEP_W / 2;
            int textWCenter = dstWPos + ((dstWPos > srcWPos) ? -stepCenter : stepCenter);
            int textWPos = textWCenter - HOST_STEP_W / 4;
            graph.drawString(text, textWPos, h - 2);

            h += MSG_STEP_H;
        }
    }

    private void drawHosts(Graphics2D graph, int height,
            HashMap<String, Integer> hostList) {
        graph.setColor(Color.BLACK);
        graph.drawString("Delta", IMAGE_BORDER, HOST_INIT_H);

        // stroke for host's dashed line
        float dash[] = {3.0f};
        BasicStroke bs = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        graph.setStroke(bs);

        for (String hostAddr : hostList.keySet()) {
            int lineWeight = hostList.get(hostAddr);
            graph.drawString(hostAddr, (lineWeight - HOST_LINE_OFFSET_W), HOST_INIT_H);
            graph.drawLine(lineWeight, HOST_INIT_H + HOST_LINE_OFFSET_H, lineWeight, height - HOST_INIT_H);
        }
    }

    private HashMap<String, Integer> createHostListWithWeightPosition(
            List<SIPMessage> messages) {
        HashMap<String, Integer> resHostList = new HashMap<String, Integer>();
        int lineWeigh = HOST_INIT_W + HOST_LINE_OFFSET_W;

        for (SIPMessage message : messages) {
            String srcAddr = new StringBuilder(message.getSrcAddress()).append(":").append(message.getSrcPort()).toString();
            String dstAddr = new StringBuilder(message.getDstAddress()).append(":").append(message.getDstPort()).toString();

            if (resHostList.get(srcAddr) == null) {
                resHostList.put(srcAddr, lineWeigh);
                lineWeigh += HOST_STEP_W;
            }

            if (resHostList.get(dstAddr) == null) {
                resHostList.put(dstAddr, lineWeigh);
                lineWeigh += HOST_STEP_W;
            }
        }

        return resHostList;
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

        List<Color> resultColors = new ArrayList<Color>();

        for (String strColor : colorList) {
            int rgbColor = Integer.parseInt(strColor, 16);
            Color color = new Color(rgbColor);
            resultColors.add(color);
        }

        return resultColors;
    }
}
