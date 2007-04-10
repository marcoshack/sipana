package br.com.voicetechnology.sipana;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import br.com.voicetechnology.sipana.sip.SipTransaction;

public class SipanaServerClientImp implements SipanaServerClient {
    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session; 
    private Destination destination;
    private MessageProducer producer;
    private Logger logger;

    public SipanaServerClientImp() throws JMSException {
        logger = Logger.getLogger(SipanaServerClient.class); 
        connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

        // Create a Connection
        connection = connectionFactory.createConnection();
        connection.start();

        // Create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        destination = session.createQueue("SIPANA.CAPTURER");
        
        // Create a MessageProducer from the Session to the Topic or Queue
        producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    }

    public void addTransaction(SipTransaction transaction) {
        try {
            ObjectMessage message = session.createObjectMessage(transaction);
            producer.send(message);
        } catch (JMSException ex) {
            logger.error("Failed to send transaction to server", ex);
        }
    }
}
