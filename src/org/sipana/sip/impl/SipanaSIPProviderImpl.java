package org.sipana.sip.impl;

import java.util.LinkedHashMap;

import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.jpcap.osgi.CaptureServiceProvider;
import net.sourceforge.jpcap.osgi.CaptureSession;

import org.apache.log4j.Logger;
import org.sipana.sip.SIPHandler;
import org.sipana.sip.SIPMessageFactory;
import org.sipana.sip.SipanaSIPProvider;

public class SipanaSIPProviderImpl implements SipanaSIPProvider {
    private SIPHandler handler;
    private SipanaSIPSessionManager sessionManager;
    private LinkedHashMap<Integer, CaptureSession> captureSessions;
    private CaptureServiceProvider captureProvider;
    private Logger logger;
    
    
    public SipanaSIPProviderImpl() {
        logger = Logger.getLogger(SipanaSIPProvider.class);
        sessionManager = new SipanaSIPSessionManager();
        handler = new SIPHandlerImpl(this);
        handler.setSessionListener(sessionManager);
        captureSessions = new LinkedHashMap<Integer, CaptureSession>();
    }
    
    public SIPHandler getSIPHandler() {
        return handler;
    }

    public SIPMessageFactory getMessageFactory() {
        return SIPMessageFactoryImpl.getInstance();
    }

    public SipanaSIPSessionManager getSIPSessionManager() {
        return sessionManager;
    }
    
    public void setCaptureServiceProvider(CaptureServiceProvider provider) {
        captureProvider = provider;
    }
    
    public void addCaptureSession(String iface, String filter) {
        logger.debug("Creating capture session to capture SIP packets");
        
        try {
            if (captureProvider != null) {
                CaptureSession captureSession = captureProvider.createCaptureSession();
                captureSession.setDevice("lo");
                captureSession.setFilter("udp or icmp");
                captureSession.setPromiscuous(true);
                captureSession.setListener((PacketListener) getSIPHandler());
                
                addCaptureSession(captureSession);
                captureSession.start();
                
                logger.debug("Capture session created");
            } else {
                throw new Exception("Capture Service Provider is null. Can't create capture sessions");
            }
        } catch (Exception e) {
            logger.error("Fail creating capture session: " + e.getMessage(), e);
        }
    }
    
    private void addCaptureSession(CaptureSession session) {
        int sessionId = (session.getDevice() + session.getFilter()).hashCode();
        
        synchronized (captureSessions) {
            captureSessions.put(sessionId, session);
        }
    }
}
