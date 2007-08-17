package org.sipana.server.sip;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.sipana.core.sip.SIPSessionInfo;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue"),
        @ActivationConfigProperty(propertyName="destination", propertyValue="org.sipana.sip")
})
public class SIPSessionMDB implements MessageListener {
    private Logger logger;
    
    public SIPSessionMDB() {
        logger = Logger.getLogger(SIPSessionMDB.class);
        logger.info("Starting Sipana SIPSessionInfo MDB");
    }

    public void onMessage(Message message) {
    	logger.debug("Processing new message");
    	
        try {
        	Object object = ((ObjectMessage)message).getObject();
        	
        	if (object instanceof SIPSessionInfo) {
                SIPSessionInfo session = (SIPSessionInfo) object;
        		logger.debug(session);
        	} else {
        		logger.error("Unknow object message: " + object);
        	}
        	
        	logger.debug("Message processed");
        	
        } catch (Throwable t) {
            logger.error("Fail processing message: " + t.getMessage(), t);
        }
    }
    
}
