package org.sipana.client.sip;

import java.text.ParseException;
import java.util.HashMap;

import javax.sip.message.Request;
import javax.sip.message.Response;

import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.jpcap.net.Packet;
import net.sourceforge.jpcap.net.UDPPacket;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.sipana.core.sip.SIPMessageFactory;
import org.sipana.core.sip.SIPMessageInfo;
import org.sipana.core.sip.SIPRequestInfo;
import org.sipana.core.sip.SIPResponseInfo;
import org.sipana.core.sip.SIPSessionInfo;
import org.sipana.core.sip.impl.SIPMessageFactoryImpl;
import org.sipana.core.sip.impl.SIPSessionInfoImpl;

public class SIPHandler implements PacketListener {
    private Logger logger;
    private HashMap<String, SIPSessionInfo> currentSessions;
    private SIPMessageFactory messageFactory;
    private SIPSessionInfoSender sessionSender;
    
    public SIPHandler(SIPSessionInfoSender sender) {
        logger = Logger.getLogger(SIPHandler.class);
        currentSessions = new HashMap<String, SIPSessionInfo>();
        messageFactory = SIPMessageFactoryImpl.getInstance();
        sessionSender = sender;
    }
    
    public void processRequest(SIPRequestInfo requestInfo) {
        String method = requestInfo.getMethod();
        
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Processing request ");
            sb.append(method);
            logger.debug(sb);
        }
        
        if (method.equals(Request.INVITE)) {
            processInvite(requestInfo);
        } else if (method.equals(Request.ACK)) {
            processAck(requestInfo);
        } else if (method.equals(Request.BYE) || method.equals(Request.CANCEL)) {
            processDisconnectRequest(requestInfo);
        } else {
            //unkownRequest++;
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Unknown request method ");
                sb.append(method);
                logger.debug(sb);
            }
        }
    }

    public void processResponse(SIPResponseInfo responseInfo) {
        String callId = responseInfo.getCallID();
        SIPSessionInfo session = getSessionInfo(callId);
        
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Processing response ");
            sb.append(responseInfo.getStatusCode());
            sb.append(" (");
            sb.append(responseInfo.getReasonPhrase());
            sb.append(") related to resquest ");
            sb.append(responseInfo.getRelatedRequestMethod());
            logger.debug(sb);
        }
        
        if (session != null) {
            int statusCode = responseInfo.getStatusCode();
            session.addResponseInfo(responseInfo);
            
            if (statusCode >= 100 && statusCode < 200) {           // Provisional 1xx
                processProvisionalResponse(session, responseInfo);
            } else if (statusCode == 200) {                        // Sussceful 2xx
                processSucessfulResponse(session, responseInfo);
            } else if (statusCode >= 400 && statusCode < 700) {    // Failure 4xx, 5xx and 6xx
                processFailureResponse(session, responseInfo);
            } else {
                //unkownResponse++;
                logger.debug("Unknown response ");
            }
        } else {
            logger.debug("Session not found for response");
        }

    }
    
    private void processInvite(SIPRequestInfo inviteInfo) {
        if (!currentSessions.containsKey(inviteInfo.getCallID())) {
            SIPSessionInfo newSession = new SIPSessionInfoImpl(inviteInfo);
            addSessionInfo(newSession);
        } else {
            String callId = inviteInfo.getCallID();
            SIPSessionInfo sessionInfo = getSessionInfo(callId);
            sessionInfo.addRequestInfo(inviteInfo);
        }
    }

    private void processAck(SIPRequestInfo ackInfo) {
        SIPSessionInfo session = getSessionInfo(ackInfo.getCallID());
        
        if (session != null) {
            session.addRequestInfo(ackInfo);
        } else {
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Session not found for request ");
                sb.append(Request.ACK);
                logger.debug(sb);
            }
        }
    }
    
    private void processDisconnectRequest(SIPRequestInfo requestInfo) {
        SIPSessionInfo session = getSessionInfo(requestInfo.getCallID());
        
        if (session != null) {
            session.addRequestInfo(requestInfo);
            session.setDisconnectionStartTime(requestInfo.getDate().getTime());
        } else {
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Session not found for request ");
                sb.append(Request.ACK);
                logger.debug(sb);
            }
        }
    }
    
    private void processProvisionalResponse(SIPSessionInfo session, SIPResponseInfo responseInfo) {
        long responseTime = responseInfo.getDate().getTime();
        session.setFirstResponseTime(responseTime);
    }
    
    private void processSucessfulResponse(SIPSessionInfo session, SIPResponseInfo responseInfo) {
        long respTime = responseInfo.getDate().getTime();
        int statusCode = responseInfo.getStatusCode();
        String method = responseInfo.getRelatedRequestMethod();
        
        switch (statusCode) {
            case Response.OK:
                if (method.equals(Request.INVITE)) {
                    session.setEstablishedTime(respTime);
                } else if (method.equals(Request.BYE)) {
                    session.setEndTime(respTime);
                    session.setState(SIPSessionInfo.COMPLETED);
                    terminateSessionInfo(session);
                }
                break;
    
            default:
                if (logger.isDebugEnabled()) {
                    StringBuilder sb = new StringBuilder("Response ");
                    sb.append(statusCode);
                    sb.append(" (");
                    sb.append(responseInfo.getReasonPhrase());
                    sb.append(") not implemented");
                    logger.debug(sb);
                }
                break;
        }
    }

    private void processFailureResponse(SIPSessionInfo session, SIPResponseInfo responseInfo) {
        long responseTime = responseInfo.getDate().getTime();
        session.setFirstResponseTime(responseTime);
    }
    
    private void addSessionInfo(SIPSessionInfo session) {
        logger.debug("Adding session to current session list");
        synchronized (currentSessions) {
            currentSessions.put(session.getId(), session);
        }
    }
    
    private void removeSessionInfo(SIPSessionInfo session) {
        logger.debug("Removing session from current session list");
        synchronized (currentSessions) {
            currentSessions.remove(session.getId());
        }
    }
    
    private SIPSessionInfo getSessionInfo(String callId) {
        synchronized (currentSessions) {
            return currentSessions.get(callId);
        }
    }
    
    private void terminateSessionInfo(SIPSessionInfo session) {
        String id = session.getId();
        
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Terminating SIP session ");
            sb.append(id);
            logger.debug(sb);
        }
        
        try {
            sessionSender.send(session);
        } catch (Exception e) {
            logger.error("Error sending SIPSessionInfo: " + e.getMessage(), e);
        }
        
        removeSessionInfo(session);
    }

    public SIPMessageFactory getMessageFactory() {
        return SIPMessageFactoryImpl.getInstance();
    }

    public int getCurrentSessionNumber() {
        return currentSessions.size();
    }

    public void packetArrived(Packet packet) {
        try {
            UDPPacket udpPacket = (UDPPacket)packet;
            SIPMessageInfo message = messageFactory.createMessage(udpPacket.getData());
            message.setSrcIP(udpPacket.getSourceAddress());
            message.setDstIP(udpPacket.getDestinationAddress());
            message.setDate(udpPacket.getTimeval().getDate());
            
            String callId = message.getCallID();
            NDC.push(new StringBuilder("Call-ID: ").append(callId).toString());
            
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Processing new message ");
                sb.append(message);
                logger.debug(sb);
            }
            
            if (message instanceof SIPRequestInfo) {
                processRequest((SIPRequestInfo) message);
            } else if (message instanceof SIPResponseInfo) {
                processResponse((SIPResponseInfo) message);
            }
            
        } catch (ParseException e) {
            logger.error("Fail processing packet: " + e.getMessage(), e);
        } finally {
            NDC.pop();
        }
    }

}
