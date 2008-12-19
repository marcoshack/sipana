package org.sipana.server.sip;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPSession;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"     ),
        @ActivationConfigProperty(propertyName = "destination"    , propertyValue = "queue/org.sipana.sip")
})

public class SIPSessionMDB implements MessageListener {
    private Logger logger;

    @EJB
    private SIPSessionManager manager;

    public SIPSessionMDB() {
        logger = Logger.getLogger(SIPSessionMDB.class);
        manager = new SIPSessionManagerBean();
    }

    public void onMessage(Message message) {
        try {
            NDC.push(message.getJMSMessageID());
            logger.debug("Processing new message");

            if (message instanceof ObjectMessage) {
                processObjectMessage((ObjectMessage) message);
            } else if (message instanceof TextMessage) {
                processTextMessage((TextMessage) message);
            }
        } catch (Exception e) {
            logger.error("Fail processing new message", e);
        } finally {
            NDC.remove();
        }
    }

    private void processObjectMessage(ObjectMessage objMessage) {
        logger.debug("Processing Object Message");
        
        try {
            Object object = ((ObjectMessage) objMessage).getObject();
            
            if (object instanceof SIPSession) {
                SIPSession session = (SIPSession) object;
                addSIPSession(session);
            
            } else if (object instanceof SIPMessage) {
                 SIPMessage message = (SIPMessage) object;
                 addSIPMessage(message);
                
            } else {
                logger.error("Unknow object message: " + object);
            }
        } catch (Throwable t) {
            logger.error("Fail processing Object message", t);
        }

    }

    private void processTextMessage(TextMessage message) {
        logger.debug("Processing Text message");

        try {
            TextMessage textMessage = (TextMessage) message;
            logger.debug(textMessage.getText());
            logger.debug("Message processed");
        } catch (Throwable t) {
            logger.error("Fail processing Text message", t);
        }
    }

    private void addSIPSession(SIPSession session) {
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Adding SIP session: ");
            sb.append(session);
            logger.debug(sb);
        }

        manager.createSIPSession(session);
    }
    
    private void addSIPMessage(SIPMessage message) {
        if (logger.isDebugEnabled()) {
            StringBuilder sbDebug = new StringBuilder("Adding standalone message: ");
            sbDebug.append(message);
            logger.debug(sbDebug);
        }
        
        SIPSession session = manager.getSIPSessionByCallID(message.getCallID());
        
        if (session != null) {
            session.addMessage(message);
            
        } else {
            logger.warn("Message SIP session not found. Message discarted");
        }
    }
}
