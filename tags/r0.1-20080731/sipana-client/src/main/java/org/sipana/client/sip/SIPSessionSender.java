package org.sipana.client.sip;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.sipana.client.config.ConfigManager;
import org.sipana.protocol.sip.SIPSession;

public class SIPSessionSender implements ExceptionListener {
    public static final int STATE_STOPED  = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_FAILED =  2;
    
    private Logger logger;
    private ConnectionFactory factory;
    private Destination destination;
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private ConfigManager configManager;
    private boolean isDelayed = false;
    private DelayedSender delayedSender;
    private SenderReconnector reconnector;
    private ConcurrentLinkedQueue<SIPSession> sessions;
    private AtomicInteger state;
    private AtomicInteger discardedSessions;
    private int bufferSize;
    private String destinationName;
    
    
    public SIPSessionSender() throws Exception {
        state = new AtomicInteger();
        sessions = new ConcurrentLinkedQueue<SIPSession>();
        discardedSessions = new AtomicInteger(0);

        logger = Logger.getLogger(SIPSessionSender.class);
        configManager = ConfigManager.getInstance();
        bufferSize = configManager.getBufferSize();
        isDelayed  = configManager.isSenderModeDelayed();
        destinationName = configManager.getSenderDestination();

        setState(SIPSessionSender.STATE_STOPED);
    }

    public void send(SIPSession session) throws Exception {
        if (isDelayed) {
            if ((sessions.size() < bufferSize)) {
                sessions.add(session);
            } else {
                discardedSessions.incrementAndGet();
                sessions.poll();
                sessions.add(session);
            }
        } else {
            sendNow(session);
        }
    }
    
    private void sendNow(SIPSession sipSession) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Sending session: ").append(sipSession));
        }
        
        //Message message = createTextMessage(sipSession);
        Message message = createObjectMessage(sipSession);
        producer.send(message);
        
        logger.debug("Session sent");
    }

    private TextMessage createTextMessage(SIPSession sipSession) throws Exception {
        TextMessage message = session.createTextMessage();
        message.setText(sipSession.toString());
        return message;
    }
    
    private ObjectMessage createObjectMessage (SIPSession sipSession) throws Exception {
        ObjectMessage message = session.createObjectMessage();
        message.setObject(sipSession);
        return message;
    }
    

    private void setState(int newState) {
        state.set(newState);
    }
    
    private int getState() {
        return state.get();
    }
    
    public boolean start() {
        try {
            logger.debug("Starting SIPSession sender");
            
            if (getState() == SIPSessionSender.STATE_RUNNING) {
                logger.debug("Sender already running");
                return false;
            }
            
            logger.debug("Creating JNDI Initial Context");
            InitialContext initContext = new InitialContext();
            
            logger.debug("Looking up for Connection Factory");
            factory = (ConnectionFactory) initContext.lookup("ConnectionFactory");
            logger.debug("Connection Factory created: " + factory.getClass().getCanonicalName());
            
            logger.debug("Looking up for Destination \"" + destinationName + "\"");
            destination = (Destination) initContext.lookup("queue/org.sipana.sip");
            logger.debug("Destination found: " + destination);
            
            logger.debug("Closing JNDI Initial Context");
            initContext.close();
            
            logger.debug("Creating connection, session and message producer");
            connection = factory.createConnection();
            connection.setExceptionListener(this);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(destination);
            
            setState(SIPSessionSender.STATE_RUNNING);
            
            if (isDelayed) {
                startDelayedSender();
            }
            
            logger.info("SIPSession sender sucessful started");
            return true;
            
        } catch (Exception e) {
            logger.error("Fail starting SIPSession sender", e);
            setState(SIPSessionSender.STATE_FAILED);
            
            if (reconnector == null || !(reconnector.isRunning())) {
                startSenderReconnector();
            }
            
            return false;
        }
    }

    public void stop() {
        logger.debug("Stoping SIPSession sender");
		try {
            // Stop Delayed sender thread if it's running
            if (delayedSender != null) {
                delayedSender.stop();
            }
            
            // Close connection if it's running
            if (getState() == SIPSessionSender.STATE_RUNNING) {
                logger.debug("Closing sender connection");
                connection.close();
            }
            
		} catch (JMSException e) {
			logger.error("Fail closing connection", e);
		}
	}
    
    private void startDelayedSender() {
        logger.debug("Starting Delayed sender");
        long delay_period = configManager.getDelayedSenderInterval();
        delayedSender = new DelayedSender(delay_period);
        Thread thread = new Thread(delayedSender);
        thread.setName("DelayedSender");
        thread.start();
        logger.debug("Delayed sender started");
    }
    
    private void startSenderReconnector() {
        logger.info("Starting Sender reconnector");
        long retryInterval = configManager.getSenderReconnectionInterval();
        reconnector = new SenderReconnector(retryInterval);
        Thread thread = new Thread(reconnector);
        thread.setName("SenderReconnector");
        thread.start();
        logger.info("Sender reconnector started");
    }
    

    public void onException(JMSException e) {
        logger.error("Sender connection fail", e);
        
        if (getState() == SIPSessionSender.STATE_RUNNING) {
            setState(SIPSessionSender.STATE_FAILED);
            
            if (delayedSender != null && delayedSender.isRunning()) {
                delayedSender.stop();
                delayedSender = null;
            }
            
            startSenderReconnector();
        }
    }

    public int getDiscardedSessions() {
        return discardedSessions.get();
    }
    
    private class DelayedSender implements Runnable {
        private AtomicLong sendInterval;
        private AtomicBoolean running;
        
        public DelayedSender(long interval) {
            sendInterval = new AtomicLong();
            running = new AtomicBoolean();
            setSendInterval(interval);
        }

        public void run() {
            running.set(true);
            
            while (running.get()) {
                try {
                    if (logger.isDebugEnabled()) {
                        StringBuilder sb = new StringBuilder("Waiting ").append(sendInterval);
                        sb.append(" seconds for the next send");
                        logger.debug(sb);
                    }
                    
                    synchronized (this) {
                        wait(sendInterval.get() * 1000);
                    }
                    
                    while (sessions.size() > 0 && getState() == SIPSessionSender.STATE_RUNNING) {
                        SIPSession session = sessions.poll();
                        sendNow(session);
                        sessions.remove(session);
                    }
                
                } catch (Throwable t) {
                    logger.error("Fail sending messages", t);
                }
            }
            
            logger.debug("Delayed sender stoped");
        }
        
        public void setSendInterval(long interval) {
            sendInterval.set(interval);
        }
        
        public boolean isRunning() {
            return running.get();
        }
        
        public void stop() {
            logger.debug("Stopping Delayed sender");
            running.set(false);
            synchronized (this) {
                notifyAll();
            }
        }
    }
    
    private class SenderReconnector implements Runnable {
        private AtomicLong retryInterval;
        private AtomicBoolean running;
        
        public SenderReconnector(long interval) {
            retryInterval = new AtomicLong();
            running = new AtomicBoolean();
            setRetryInterval(interval);
        }

        public void run() {
            running.set(true);
            
            while (getState() == SIPSessionSender.STATE_FAILED && running.get()) {
                
                try {
                    logger.info("Waiting " + retryInterval + " seconds to retry");
                    
                    synchronized (this) {
                        wait(retryInterval.get() * 1000);
                    }
                    
                    if (running.get()) {
                        logger.info("Trying to restart sender connection");
                        if (start()) {
                            running.set(false); // Stop reconnector
                        }
                    }
                    
                } catch (Throwable t) {
                    logger.error("Fail trying to reconnect", t);
                }
            }
            
            logger.info("Sender reconnector stoped");
        }
        
        public void setRetryInterval(long interval) {
            retryInterval.set(interval);
        }
        
        public boolean isRunning() {
            return running.get();
        }
        
        public void stop() {
            logger.debug("Stopping SenderReconnector");
            running.set(false);
            synchronized (this) {
                notifyAll();
            }
        }
    }
}
