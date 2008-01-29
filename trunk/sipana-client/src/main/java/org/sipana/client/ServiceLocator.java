package org.sipana.client;

import org.apache.log4j.Logger;
import org.sipana.client.capture.CaptureManager;
import org.sipana.client.capture.CaptureSession;
import org.sipana.client.capture.impl.CaptureManagerImpl;
import org.sipana.client.capture.impl.CaptureSessionImpl;
import org.sipana.client.config.ConfigManager;
import org.sipana.client.sip.SIPHandler;
import org.sipana.client.sip.SIPSessionSender;
import org.sipana.protocol.sip.SIPFactory;
import org.sipana.protocol.sip.impl.SIPFactoryImpl;

public class ServiceLocator {
    private static ServiceLocator instance;
    private static Logger logger = Logger.getLogger(ServiceLocator.class);
    
    private ServiceLocator() {
    }
    
    public static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        
        return instance;
    }
    
    public SIPSessionSender getSIPSessionSender() throws Exception {
        return new SIPSessionSender();
    }
    
    public SIPHandler getSIPHandler(SIPSessionSender sender) {
        return new SIPHandler(sender);
    }
    
    public CaptureManager getCaptureManager() {
    	return CaptureManagerImpl.getInstance();
    }

    public ConfigManager getConfigManager() throws Exception {
        return ConfigManager.getInstance();
    }
    
    public SIPFactory getSIPFactory() throws Exception {
        return SIPFactoryImpl.getInstance();
    }
}
