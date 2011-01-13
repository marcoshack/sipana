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
package org.sipana.agent.capture.impl;

import java.util.Date;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.IPPacket;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

import org.sipana.agent.capture.AbstractCaptureSession;
import org.sipana.agent.capture.CaptureException;
import org.sipana.agent.capture.CaptureListener;
import org.sipana.agent.capture.NetworkInterfaceNotFoundException;
import org.sipana.agent.capture.Packet;
import org.sipana.agent.config.ConfigManager;

/**
 * Capture Session implementation using Keita Fujii jpcap implementation [1].
 *
 * [1] http://netresearch.ics.uci.edu/kfujii/jpcap/doc/
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class CaptureSessionKFujii extends AbstractCaptureSession implements
        PacketReceiver {

    private static int DEFAULT_SNAPLEN = 1500;
    private static int DEFAULT_TIMEOUT = 0;
    private static int DEFAULT_COUNT = -1; // Infinite
    private JpcapCaptor captor;

    public CaptureSessionKFujii(String filter, String deviceName,
            CaptureListener listener) {
        super(filter, deviceName, listener);
    }

    @Override
    protected void startCapture() throws Exception {
        NetworkInterface[] interfaces = JpcapCaptor.getDeviceList();
        NetworkInterface selectedIface = null;
        StringBuilder sbIfList = new StringBuilder("Available network interfaces: ");

        for (NetworkInterface iface : interfaces) {
            sbIfList.append(iface.name).append(", ");

            if (iface.name.equals(getDevice())) {
                selectedIface = iface;
            }
        }

        if (selectedIface != null) {
            if (logger.isDebugEnabled()) {
                logger.debug(sbIfList);
                StringBuilder sb = new StringBuilder("Selected network interface: ");
                sb.append(selectedIface.name);
                logger.debug(sb);
            }

            captor = JpcapCaptor.openDevice(selectedIface, CaptureSessionKFujii.DEFAULT_SNAPLEN, isPromiscuous(), DEFAULT_TIMEOUT);
            captor.setFilter(getFilter(), true);
            captor.loopPacket(CaptureSessionKFujii.DEFAULT_COUNT, this);

        } else {
            throw new NetworkInterfaceNotFoundException("Couldn't open interface " + getDevice() + ". Please check the property \"" + ConfigManager.PROPERTY_CAPTURE_INTERFACE + "\" in the file \"" + ConfigManager.getInstance().getConfigFileName() + "\"");
        }
    }

    @Override
    protected void stopCapture() throws Exception {
        if (captor != null) {
            captor.breakLoop();
            captor.close();

        } else {
            throw new CaptureException("Captor is not running, cannot stop it.");
        }
    }

    public void receivePacket(jpcap.packet.Packet jpcapPacket) {
        try {
            Packet packet = new Packet();

            if (jpcapPacket instanceof IPPacket) {
                IPPacket ipPacket = (IPPacket) jpcapPacket;
                packet.setSrcIPAddr(ipPacket.src_ip.getHostAddress());
                packet.setDstIPAddr(ipPacket.dst_ip.getHostAddress());
                long seconds = ipPacket.sec;
                long microSec = ipPacket.usec;
                long milliSec = (seconds * 1000) + (microSec / 1000);
                packet.setDate(new Date(milliSec));

                if (jpcapPacket instanceof UDPPacket) {
                    UDPPacket udpPacket = (UDPPacket) jpcapPacket;
                    packet.setData(new String(udpPacket.data));
                    packet.setSrcIPPort(udpPacket.src_port);
                    packet.setDstIPPort(udpPacket.dst_port);

                } else if (jpcapPacket instanceof TCPPacket) {
                    TCPPacket tcpPacket = (TCPPacket) jpcapPacket;
                    packet.setData(new String(tcpPacket.data));
                    packet.setSrcIPPort(tcpPacket.src_port);
                    packet.setDstIPPort(tcpPacket.dst_port);

                } else {
                    throw new CaptureException("Unknown packet transport type:\n" + jpcapPacket);
                }
            } else {
                throw new CaptureException("Unknown packet: \n" + jpcapPacket);
            }

            getListener().onPacket(packet);

        } catch (Exception e) {
            logger.error("Fail processing packet", e);
        }
    }
}
