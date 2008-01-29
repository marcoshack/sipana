package org.sipana.client;

import org.apache.log4j.Logger;
import org.sipana.client.capture.CaptureManager;
import org.sipana.client.capture.CaptureSession;
import org.sipana.client.config.ConfigException;
import org.sipana.client.config.ConfigManager;
import org.sipana.client.sip.SIPHandler;
import org.sipana.client.sip.SIPSessionSender;

public class SipanaClient {
	public static final int EXIT_STATUS_UNKNOWN_ERROR = -1;
	public static final int EXIT_STATUS_CONFIG_ERROR = 1;
	
    private static ServiceLocator serviceLocator;
	private static Logger logger = Logger.getLogger(SipanaClient.class);
	
	public static void main(String[] args) {
        Thread.currentThread().setName("SipanaClient");
        
        ConfigManager configManager = null;
		SIPSessionSender sender = null;
		SIPHandler handler = null;
		CaptureManager captureManager = null;
        
		try {
            serviceLocator = ServiceLocator.getInstance();
            
            configManager  = serviceLocator.getConfigManager();
            sender         = serviceLocator.getSIPSessionSender();
            handler        = serviceLocator.getSIPHandler(sender);
            captureManager = serviceLocator.getCaptureManager();

            String filter = configManager.getCaptureFilter();
            String device = configManager.getCaptureInterface();
			CaptureSession capSession = captureManager.createCaptureSession(filter, device, handler);
			
			sender.start();
			capSession.start();
            
		} catch (ConfigException e) {
			logger.error("Fail configuring client", e);
			System.exit(SipanaClient.EXIT_STATUS_CONFIG_ERROR);
		
		} catch (Exception e) {
			logger.error("Fail running SipanaClient", e);
			System.exit(SipanaClient.EXIT_STATUS_UNKNOWN_ERROR);
		}
	}
}
