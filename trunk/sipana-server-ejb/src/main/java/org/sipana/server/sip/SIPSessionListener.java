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
import org.sipana.protocol.sip.impl.SIPSessionImpl;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName  = "destinationType", 
                                  propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName  = "destination", 
                                  propertyValue = "queue/org.sipana.sip") })
                                  
public class SIPSessionListener implements MessageListener {
    private Logger logger;

    @EJB
    private SIPSessionManager manager;

    public SIPSessionListener() {
        logger = Logger.getLogger(SIPSessionListener.class);
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

    private void processObjectMessage(ObjectMessage message) {
        logger.debug("Processing Object message");

        try {
            Object object = ((ObjectMessage) message).getObject();

            if (object instanceof SIPSessionImpl) {
                SIPSessionImpl session = (SIPSessionImpl) object;

                if (logger.isDebugEnabled()) {
                    logger.debug(session);
                }

                addSIPSession(session);

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

    private void addSIPSession(SIPSessionImpl session) {
        SIPSessionImpl oldSession = manager.getSIPSession(session.getId());

        if (oldSession != null) {
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("SIP session with Call-ID ");
                sb.append(session.getCallId());
                sb.append(" already exist, merging messages to this one");
                logger.debug(sb);
            }
            
            oldSession.getRequests().addAll(session.getRequests());
            oldSession.getResponses().addAll(session.getResponses());
            manager.saveSIPSession(oldSession);
            
        } else {
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("There are no SIP session with Call-ID ");
                sb.append(session.getCallId());
                sb.append(". Creating a new SIP session");
                logger.debug(sb);
            }

            manager.createSIPSession(session);
        }
    }
}
