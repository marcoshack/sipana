package br.com.voicetechnology.sipana.capturer.pcap;

import java.util.Properties;
import org.apache.log4j.Logger;
import br.com.voicetechnology.sipana.SipanaCapturer;
import br.com.voicetechnology.sipana.capturer.Capturer;
import br.com.voicetechnology.sipana.capturer.EventHandler;
import net.sourceforge.jpcap.capture.PacketCapture;

public class CapturerPcap extends Thread implements Capturer {
    private static Logger logger = Logger.getLogger(CapturerPcap.class);

    private static final int INFINITE = -1;

    private static final String PROTOCOL_UDP = "udp";

    private static final String DEFAULT_PROTOCOL = PROTOCOL_UDP;

    private static final String DEFAULT_UDP_PORT = "5060";

    private String filter;

    private PacketCapture pcap;

    private EventHandler handler;

    private String target;

    private String device;

    private String protocol;

    private String port;

    public CapturerPcap(EventHandler event) throws Exception {
        this.handler = event;
        pcap = new PacketCapture();

        // Get system properties
        Properties properties = SipanaCapturer.getProperties();
        device = properties.getProperty("sipana.capturer.pcap.device");
        protocol = properties.getProperty("sipana.capturer.pcap.protocol");
        port = properties.getProperty("sipana.capturer.pcap." + protocol
                + ".port");
        target = properties.getProperty("sipana.capturer.targetIp");

        if (device == null) {
            logger
                    .warn("Interface to capture not specified. Trying to find one");
            device = pcap.findDevice();
        }
        if (protocol == null) {
            logger.warn("Protocol to capture not specified. Using default "
                    + DEFAULT_PROTOCOL);
            protocol = DEFAULT_PROTOCOL;
        }
        if (port == null) {
            if (protocol.equals(PROTOCOL_UDP)) {
                port = DEFAULT_UDP_PORT;
            }

            logger.warn("Port to capture not specified. Using default " + port);
        }

        // Create BPF filter
        filter = protocol + " and port " + port + " and host " + target;

        logger.info("Capturer configured on device \"" + device
                + "\" with the filter \"" + filter + "\"");
    }

    public void run() {
        this.setName("Capturer");

        try {
            // Open device in promiscuous mode
            pcap.open(device, 512, true, 0);
            // Add a BPF Filter (see tcpdump documentation)
            pcap.setFilter(filter, true);
            // Register a Listener for Raw Packets
            pcap.addPacketListener(new CapturerPcapListener(handler));
            // Capture Data Forever
            logger.info("Starting Pcap capturer");
            pcap.capture(INFINITE);
        } catch (Exception e) {
            // TODO: Erros aqui devem encerrar o Sipana.
            logger.error("Error capturing packages");
            e.printStackTrace();
        }
    }
}
