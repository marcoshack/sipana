/**
 * This file is part of Sipana project <http://sipana.org/>
 *
 * Sipana is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * Sipana is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sipana.agent.sender.jms;

import java.io.Serializable;
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
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.sipana.agent.config.ConfigManager;
import org.sipana.agent.sender.Sender;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPSession;

public class JMSSender implements Sender, ExceptionListener {
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
    private ConcurrentLinkedQueue<SIPMessage> messages;
    private AtomicInteger state;
    private AtomicInteger discardedSessions;
    private int bufferSize;
    private String destinationName;
    
    
    public JMSSender() throws Exception {
        state = new AtomicInteger();
        sessions = new ConcurrentLinkedQueue<SIPSession>();
        messages = new ConcurrentLinkedQueue<SIPMessage>();
        discardedSessions = new AtomicInteger(0);

        logger = Logger.getLogger(JMSSender.class);
        configManager = ConfigManager.getInstance();
        bufferSize = configManager.getBufferSize();
        isDelayed  = configManager.isSenderModeDelayed();
        destinationName = configManager.getSenderDestination();

        setState(JMSSender.STATE_STOPED);
    }

    public void send(SIPSession session) throws Exception {
        if (isDelayed) {
            logger.debug("Storing SIP session for delayed sending");
            
            if (!isBufferOverflowed()) {
                sessions.add(session);
            } else {
                logger.warn("Buffer overflowed, discarding the oldest SIP session stored");
                discardedSessions.incrementAndGet();
                sessions.poll();
                sessions.add(session);
            }
            
        } else if (isRunning()){
            sendNow(session);
            
        } else {
            StringBuilder sbWarn = new StringBuilder("Sender is not running and not using a delayed sender. Discarding SIP session: ");
            sbWarn.append(session);
            logger.warn(sbWarn);
        }
    }
    
    public void send(SIPMessage message) throws Exception {
        logger.debug("Sending standalone message immediately");
        
        if (isRunning()) {
            sendNow(message);
        } else if (isDelayed) {
            logger.warn("Sender is not running, cannot send the message now. Storing it for delayed sending");
            messages.add(message);
        } else {
            StringBuilder sbWarn = new StringBuilder("Sender is not running and not using a delayed sender. Discarding SIP message: ");
            sbWarn.append(message);
            logger.warn(sbWarn);
        }
    }
    
    private void sendNow(Serializable object) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Sending message: ").append(object));
        }
        
        Message message = createObjectMessage(object);
        producer.send(message);
        
        logger.debug("Session sent");
    }

    private ObjectMessage createObjectMessage (Serializable object) throws Exception {
        ObjectMessage message = session.createObjectMessage();
        message.setObject(object);
        return message;
    }
    

    private void setState(int newState) {
        state.set(newState);
    }
    
    private int getState() {
        return state.get();
    }
    
    public void start() {
        try {
            logger.debug("Starting message Sender");
            
            if (getState() == JMSSender.STATE_RUNNING) {
                logger.debug("Sender already running");
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
            
            setState(JMSSender.STATE_RUNNING);
            
            if (isDelayed) {
                startDelayedSender();
            }
            
            logger.info("Sender sucessful started");
            
        } catch (Exception e) {
            logger.error("Fail starting SIPSession sender", e);
            setState(JMSSender.STATE_FAILED);
            
            if (reconnector == null || !(reconnector.isRunning())) {
                startSenderReconnector();
            }
        }
    }

    public void stop() {
        logger.debug("Stoping message Sender");
		try {
            // Stop Delayed sender thread if it's running
            if (delayedSender != null) {
                delayedSender.stop();
            }
            
            // Close connection if it's running
            if (getState() == JMSSender.STATE_RUNNING) {
                logger.debug("Closing sender connection");
                connection.close();
            }
            
		} catch (JMSException e) {
			logger.error("Fail closing connection", e);
		}
	}
    
    private void startDelayedSender() {
        logger.debug("Starting Delayed Sender");
        long delay_period = configManager.getDelayedSenderInterval();
        delayedSender = new DelayedSender(delay_period);
        Thread thread = new Thread(delayedSender);
        thread.setName("DelayedSender");
        thread.start();
        logger.debug("Delayed Sender started");
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
        
        if (isRunning()) {
            setState(JMSSender.STATE_FAILED);
            
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
    
    private boolean isBufferOverflowed() {
        return ((sessions.size() + messages.size()) > bufferSize);
    }
    
    private boolean isRunning() {
        return (getState() == JMSSender.STATE_RUNNING);
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
                    
                    if (isRunning()) {
                    
                        while (sessions.size() > 0 && isRunning()) {
                            SIPSession session = sessions.peek();
                            sendNow(session);
                            sessions.remove(session);
                        }
                        
                        while (messages.size() > 0 && isRunning()) {
                            SIPSession session = sessions.peek();
                            sendNow(session);
                            sessions.remove(session);
                        }
                    } else {
                        logger.warn("Sender isn't running, cannot send message");
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

            // Notify sender to send remaining messages
            synchronized (this) {
                notifyAll();
            }

            running.set(false);
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
            
            while (getState() == JMSSender.STATE_FAILED && running.get()) {
                
                try {
                    logger.info("Waiting " + retryInterval + " seconds to retry");
                    
                    synchronized (this) {
                        wait(retryInterval.get() * 1000);
                    }
                    
                    if (running.get()) {
                        logger.info("Trying to restart sender connection");
                        start();
                        running.set(false); // Stop reconnector
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
