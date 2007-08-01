package org.sipana.sip.impl;

import java.util.LinkedList;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.sipana.sip.SIPSessionInfo;
import org.sipana.sip.SIPSessionInfoListener;

public class SipanaSipSessionManager extends Thread implements SIPSessionInfoListener {
    private LinkedList<SIPSessionInfo> sessions;
    private Boolean running;
    private long interval;
    private String serverURL;
    private String serverUser;
    private String serverPassword;
    private String serverQueue;
    private Connection serverConnection;
    private Session serverSession;
    private MessageProducer messageProducer;
    private Logger logger;
    
    public SipanaSipSessionManager() {
        logger = Logger.getLogger(SipanaSipSessionManager.class);
        sessions = new LinkedList<SIPSessionInfo>();
    }
    
    public void sipSessionInfoFinished(SIPSessionInfo session) {
        addSession(session);
    }
    
    public synchronized void stopManager() {
        logger.debug("Stopping Sipana SIP Session Manager");
        running = false;
        
        synchronized (sessions) {
            sessions = null;
        }
    }
    
    @Override
    public void run() {
        logger.info("Starting Sipana SIP Session Manager");
        running = true;
        
        try {
            startMQSession();
            
            while (running) {
                logger.debug("Running SIP session manager");

                if (sessions.size() > 0) {
                    LinkedList<SIPSessionInfo> currentList = null;
                    
                    logger.debug("Copying current list to send to server");
                    synchronized (sessions) {
                        currentList = (LinkedList<SIPSessionInfo>) sessions.clone();
                        sessions = new LinkedList<SIPSessionInfo>();
                    }
                    
                    for (SIPSessionInfo session : currentList) {
                        NDC.push(session.getId());
                        sendSession(session);
                        NDC.pop();
                    }
                    
                    currentList = null;
                }
                
                if (logger.isDebugEnabled()) {
                    logger.debug("Waiting " + interval + "ms to next execution");
                }
                sleep(interval);
            }
            
            logger.info("Sipana SIP Session Manager stoped");
        
        } catch (JMSException je) {
            logger.error("Error sending JMS message: " + je.getMessage(), je);
        } catch (Exception e) {
            logger.error("Error running SIP session manager: " + e.getMessage(), e);
        } finally {
            running = false;
            NDC.remove();
            stopMQSession();
        }
    }

    private void addSession(SIPSessionInfo session) {
        NDC.push(session.getId());
        logger.debug("Adding session");
        
        if (running) {
            synchronized (sessions) {
                sessions.add(session);
            }
            
            logger.debug("Session added");
        } else {
            logger.warn("SIP Session Manager not running, can't add sessions");
        }
        NDC.pop();
    }
    
    private void startMQSession() throws Exception {
        logger.debug("Starting MQ session");
        
        // Create the connection.
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(serverUser, serverPassword, serverURL);           
        serverConnection = connectionFactory.createConnection();
        serverConnection.start();
        
        serverSession = serverConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Destination destination = serverSession.createQueue(serverQueue);
        messageProducer = serverSession.createProducer(destination);
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
        
        logger.debug("MQ session started");
    }
    
    private void stopMQSession() {
        try {
            logger.debug("Stopping MQ session");
            serverConnection.close();
            logger.debug("MQ session stoped");
        } catch (Throwable t) {
            logger.error("Fail stopping MQ session: " + t.getMessage(), t);
        }
    }
    
    private void sendSession(SIPSessionInfo session) throws JMSException {
        logger.debug("Sending message to broker");

        TextMessage message = serverSession.createTextMessage();
        message.setStringProperty("method", session.getRequestMethod());
        message.setLongProperty("request_delay", session.getRequestDelay());
        messageProducer.send(message);
        serverSession.commit();
        
        logger.debug("Message sent");
    }

    public void setInterval(long interval) {
        this.interval = interval * 1000;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public void setServerUser(String serverUser) {
        this.serverUser = serverUser;
    }

    public void setServerQueue(String serverQueue) {
        this.serverQueue = serverQueue;
    }
}
