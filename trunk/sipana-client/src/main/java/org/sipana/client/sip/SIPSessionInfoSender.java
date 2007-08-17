package org.sipana.client.sip;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.sipana.core.sip.SIPSessionInfo;

public class SIPSessionInfoSender {
    private Logger logger;
    private ConnectionFactory factory;
    private Destination destination;
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    
    public SIPSessionInfoSender() throws Exception {
        logger = Logger.getLogger(SIPSessionInfoSender.class);

        logger.debug("Looking up for MQ provider's ConnectionFactory and Destination");
        InitialContext initContext = new InitialContext();
        factory = (ConnectionFactory) initContext.lookup("connectionFactory");
        logger.debug("Factory found: " + factory.getClass().getCanonicalName());
        destination = (Destination) initContext.lookup("SipanaSipQueue");
        logger.debug("Destination found: " + destination);
        initContext.close();
        
        logger.debug("Creating connection, session and message producer");
        connection = factory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(destination);
        
        logger.debug("Sender sucessful initialized");
    }
    
    public void send(SIPSessionInfo sipSession) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Sending session: ").append(sipSession));
        }

        Message message = createMessage(sipSession);
        producer.send(message);
        
        logger.debug("Session sent");
    }
    
    private Message createMessage(SIPSessionInfo sipSession) throws Exception {
        ObjectMessage objMessage = session.createObjectMessage();
        objMessage.setObject(sipSession);
        return objMessage;
    }

	public void stop() {
		try {
			logger.debug("Closing sender MQ connection");
			connection.close();
		} catch (JMSException e) {
			logger.error("Fail closing MQ connection", e);
		}
	}
}
