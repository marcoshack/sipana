package org.apache.activemq.test;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.util.IndentPrinter;
import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ProducerTool implements BundleActivator {

    private Destination destination;
    private int messageCount = 10;
    private long sleepTime = 0L;
    private boolean verbose = true;
    private int messageSize = 255;
    private long timeToLive;
    private String user = ActiveMQConnection.DEFAULT_USER;
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private String subject = "TOOL.DEFAULT";
    private boolean topic = false;
    private boolean transacted = false;
    private boolean persistent = false;
    private Logger logger;
    

    public void start(BundleContext arg0) throws Exception {
        logger = Logger.getLogger(ProducerTool.class);
        
        setUrl("tcp://localhost:61616");
        setTopic(false);
        setSubject("TEST.FOO");
        setMessageCount(10);
        setMessageSize(1000);
        setUser("consumer1");
        setTimeToLive(0);
        setSleepTime(0);
        setTransacted(false);
        setVerbose(true);
        
        run();
    }

    public void stop(BundleContext arg0) throws Exception {
        // TODO Auto-generated method stub
    }

    public void run() {
        Connection connection=null;
        try {
            logger.debug("Connecting to URL: " + url);
            logger.debug("Publishing a Message with size " + messageSize+ " to " + (topic ? "topic" : "queue") + ": " + subject);
            logger.debug("Using " + (persistent ? "persistent" : "non-persistent") + " messages");
            logger.debug("Sleeping between publish " + sleepTime + " ms");
            if (timeToLive != 0) {
                logger.debug("Messages time to live " + timeToLive + " ms");
            }
            
            // Create the connection.
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);           
            connection = connectionFactory.createConnection();
            connection.start();
            
            // Create the session
            Session session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
            if (topic) {
                destination = session.createTopic(subject);
            } else {
                destination = session.createQueue(subject);
            }
            
            // Create the producer.
            MessageProducer producer = session.createProducer(destination);
            if (persistent) {
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            } else {
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            }           
            if (timeToLive != 0)
                producer.setTimeToLive(timeToLive);
                        
            // Start sending messages
            sendLoop(session, producer);

            logger.debug("Done.");
            
            // Use the ActiveMQConnection interface to dump the connection stats.
            ActiveMQConnection c = (ActiveMQConnection) connection;
            c.getConnectionStats().dump(new IndentPrinter());
                        
        } catch (Exception e) {
            logger.debug("Caught: " + e);
            e.printStackTrace();
        } finally {
            try { 
                connection.close();
            } catch (Throwable ignore) {
            }
        }
    }

    protected void sendLoop(Session session, MessageProducer producer)
            throws Exception {

        for (int i = 0; i < messageCount || messageCount == 0; i++) {

            TextMessage message = session
                    .createTextMessage(createMessageText(i));

            if (verbose) {
                String msg = message.getText();
                if (msg.length() > 50) {
                    msg = msg.substring(0, 50) + "...";
                }
                logger.debug("Sending message: " + msg);
            }

            producer.send(message);
            if (transacted) {
                session.commit();
            }

            Thread.sleep(sleepTime);

        }

    }

    private String createMessageText(int index) {
        StringBuffer buffer = new StringBuffer(messageSize);
        buffer.append("Message: " + index + " sent at: " + new Date());
        if (buffer.length() > messageSize) {
            return buffer.substring(0, messageSize);
        }
        for (int i = buffer.length(); i < messageSize; i++) {
            buffer.append(' ');
        }
        return buffer.toString();
    }


    public void setPersistent(boolean durable) {
        this.persistent = durable;
    }
    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }
    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }
    public void setPassword(String pwd) {
        this.password = pwd;
    }
    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }
    public void setTopic(boolean topic) {
        this.topic = topic;
    }
    public void setQueue(boolean queue) {
        this.topic = !queue;
    }   
    public void setTransacted(boolean transacted) {
        this.transacted = transacted;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

}
