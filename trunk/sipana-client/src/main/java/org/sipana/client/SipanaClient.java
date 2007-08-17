package org.sipana.client;

import org.apache.log4j.Logger;
import org.sipana.client.capture.CaptureSession;
import org.sipana.client.capture.impl.CaptureSessionImpl;
import org.sipana.client.sip.SIPHandler;
import org.sipana.client.sip.SIPSessionInfoSender;

public class SipanaClient {
	private static Logger logger = Logger.getLogger(SipanaClient.class);
	
	public static void main(String[] args) {
		SIPSessionInfoSender sender = null;
		SIPHandler handler = null;
		CaptureSession captureSession = null;
		
		try {
			sender = new SIPSessionInfoSender();
			handler = new SIPHandler(sender);
			
			captureSession = new CaptureSessionImpl();
			captureSession.setDevice("lo");
			captureSession.setFilter("udp or icmp");
			captureSession.setPromiscuous(false);
			captureSession.setListener(handler);
			captureSession.start();
			
		} catch (Exception e) {
			logger.error("Fail running SipanaClient", e);
		} finally {
			if (sender != null) {
				sender.stop();
			}
		}
	}
}
