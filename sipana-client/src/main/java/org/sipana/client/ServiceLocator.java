package org.sipana.client;

import org.sipana.client.capture.CaptureManager;
import org.sipana.client.capture.impl.CaptureManagerImpl;
import org.sipana.client.config.ConfigManager;
import org.sipana.client.sender.MessageSender;
import org.sipana.client.sip.SIPHandler;
import org.sipana.protocol.sip.SIPFactory;

public class ServiceLocator {
    private static ServiceLocator instance;
    
    private ServiceLocator() {
    }
    
    public static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        
        return instance;
    }
    
    public MessageSender getSIPSessionSender() throws Exception {
        return new MessageSender();
    }
    
    public SIPHandler getSIPHandler(MessageSender sender) {
        return new SIPHandler(sender);
    }
    
    public CaptureManager getCaptureManager() {
    	return CaptureManagerImpl.getInstance();
    }

    public ConfigManager getConfigManager() throws Exception {
        return ConfigManager.getInstance();
    }
    
    public SIPFactory getSIPFactory() throws Exception {
        return SIPFactory.getInstance();
    }
}
