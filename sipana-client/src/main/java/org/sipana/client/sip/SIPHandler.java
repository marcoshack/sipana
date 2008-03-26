package org.sipana.client.sip;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.sip.message.Request;
import javax.sip.message.Response;

import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.jpcap.net.Packet;
import net.sourceforge.jpcap.net.UDPPacket;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPFactory;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPResponse;
import org.sipana.protocol.sip.SIPSession;
import org.sipana.protocol.sip.SIPSessionStatus;
import org.sipana.protocol.sip.impl.SIPFactoryImpl;

public class SIPHandler implements PacketListener {
    private Logger logger;
    private ConcurrentMap<String, SIPSession> currentSessions;
    private SIPFactory messageFactory;
    private SIPSessionSender sessionSender;
    
    public SIPHandler(SIPSessionSender sender) {
        logger = Logger.getLogger(SIPHandler.class);
        currentSessions = new ConcurrentHashMap<String, SIPSession>();
        messageFactory = SIPFactoryImpl.getInstance();
        sessionSender = sender;
    }
    
    public void processRequest(SIPRequest request) {
        String method = request.getMethod();
        
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Processing request ");
            sb.append(method);
            logger.debug(sb);
        }
        
        if (method.equals(Request.INVITE)) {
            processInvite(request);
        } else if (method.equals(Request.ACK)) {
            processAck(request);
        } else if (method.equals(Request.BYE) || method.equals(Request.CANCEL)) {
            processDisconnectRequest(request);
        } else {
            SIPSession newSession = messageFactory.createSession(request);
            addSession(newSession);
        }
    }

    public void processResponse(SIPResponse response) {
        String callId = response.getCallID();
        SIPSession session = getSession(callId);
        
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Processing response ");
            sb.append(response.getStatusCode());
            sb.append(" (").append(response.getReasonPhrase());
            sb.append(") related to resquest ");
            sb.append(response.getRelatedRequestMethod());
            logger.debug(sb);
        }
        
        if (session != null) {
            session.addResponse(response);
            
            if (session.getFirstResponseTime() == 0) {
                session.setFirstResponseTime(response.getTime());
            }
            
            int statusCode = response.getStatusCode();
            
            if (statusCode >= 100 && statusCode < 200) {           // Provisional 1xx
                processProvisionalResponse(session, response);
                
            } else if (statusCode >= 200 && statusCode < 300) {    // Sussceful 2xx
                processSucessfulResponse(session, response);
                
            } else if (statusCode >= 300 && statusCode < 400) {    // Redirection 3xx
                processRedirectionResponse(session, response);
                
            } else if (statusCode >= 400 && statusCode < 700) {    // Failure 4xx, 5xx and 6xx
                processFailureResponse(session, response);
                
            } else {
                StringBuilder sb = new StringBuilder("Unknown response ").append(statusCode);
                sb.append(" (").append(response.getReasonPhrase()).append(")");
                logger.warn(sb);
            }
        } else {
            logger.debug("Session not found for response");
        }

    }
    
    private void processInvite(SIPRequest invite) {
        if (!currentSessions.containsKey(invite.getCallID())) {
            SIPSession newSession = messageFactory.createSession(invite);
            addSession(newSession);
        } else {
            String callId = invite.getCallID();
            SIPSession sessionInfo = getSession(callId);
            sessionInfo.addRequest(invite);
        }
    }

    private void processAck(SIPRequest ack) {
        SIPSession session = getSession(ack.getCallID());
        
        if (session != null) {
            session.addRequest(ack);
            
            if (session.getState() == SIPSessionStatus.FAILED) {
                terminateSession(session);
            }
            
        } else {
            logger.debug("Session not found for request ACK");
        }
    }
    
    private void processDisconnectRequest(SIPRequest request) {
        SIPSession session = getSession(request.getCallID());
        
        if (session != null) {
            session.addRequest(request);
            session.setDisconnectionStart(request.getTime());
        } else {
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Session not found for request ");
                sb.append(Request.ACK);
                logger.debug(sb);
            }
        }
    }
    
    private void processProvisionalResponse(SIPSession session, SIPResponse response) {
        long responseTime = response.getTime();
        int statusCode = response.getStatusCode();
        
        switch (statusCode) {
        case Response.RINGING: case Response.SESSION_PROGRESS:
            session.setSetupTime(responseTime);
            break;
        default:
            // Do nothing
            break;
        }
    }
    
    private void processSucessfulResponse(SIPSession session, SIPResponse response) {
        long respTime = response.getTime();
        int statusCode = response.getStatusCode();
        String method = response.getRelatedRequestMethod();
        
        switch (statusCode) {
            case Response.OK:
                if (method.equals(Request.INVITE)) {
                    session.setEstablishedTime(respTime);
                    session.setState(SIPSessionStatus.ESTABLISHED);
                    return;
                } else if (!(method.equals(Request.BYE) && method.equals(Request.CANCEL))) {
                    // non-INVITE request setup time is defined with OK
                    session.setSetupTime(respTime);
                }
                
                // non-INVITE request ends with OK
                session.setEndTime(respTime);
                session.setState(SIPSessionStatus.COMPLETED);
                terminateSession(session);
                break;
    
            default:
                if (logger.isDebugEnabled()) {
                    StringBuilder sb = new StringBuilder("Response ");
                    sb.append(statusCode).append(" (");
                    sb.append(response.getReasonPhrase());
                    sb.append(") not implemented");
                    logger.debug(sb);
                }
                break;
        }
    }
    
    private void processRedirectionResponse(SIPSession session, SIPResponse response) {
        // Do nothing
    }

    private void processFailureResponse(SIPSession session, SIPResponse response) {
        long responseTime = response.getTime();
        session.setEndTime(responseTime);
        session.setSetupTime(responseTime);
        session.setState(SIPSessionStatus.FAILED);
        terminateSession(session);
    }
    
    private void addSession(SIPSession session) {
        logger.debug("Adding session to current session list");
        currentSessions.put(session.getCallId(), session);
    }
    
    private void removeSession(SIPSession session) {
        logger.debug("Removing session from current session list");
        currentSessions.remove(session.getCallId());
    }
    
    private SIPSession getSession(String callId) {
        return currentSessions.get(callId);
    }
    
    private void terminateSession(SIPSession session) {
        String id = session.getCallId();
        
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Terminating SIP session ");
            sb.append(id);
            logger.debug(sb);
        }
        
        try {
            sessionSender.send(session);
            
        } catch (Exception e) {
            logger.error("Error sending SIPSession: " + e.getMessage(), e);
        }
        
        removeSession(session);
    }

    public SIPFactory getMessageFactory() {
        return SIPFactoryImpl.getInstance();
    }

    public int getCurrentSessionNumber() {
        return currentSessions.size();
    }

    public void packetArrived(Packet packet) {
        try {
            UDPPacket udpPacket = (UDPPacket)packet;
            SIPMessage message = messageFactory.createMessage(udpPacket.getData());
            
            String callId = message.getCallID();
            NDC.push(new StringBuilder("callId=").append(callId).toString());
            
            String srcAddr = new StringBuilder(udpPacket.getSourceAddress()).append(":").append(udpPacket.getSourcePort()).toString();
            String dstAddr = new StringBuilder(udpPacket.getDestinationAddress()).append(":").append(udpPacket.getDestinationPort()).toString();
            message.setSrcAddress(srcAddr);
            message.setDstAddress(dstAddr);
            message.setTime(udpPacket.getTimeval().getDate().getTime());
            
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Processing new message ");
                sb.append(message);
                logger.debug(sb);
            }
            
            if (message instanceof SIPRequest) {
                processRequest((SIPRequest) message);
            } else if (message instanceof SIPResponse) {
                processResponse((SIPResponse) message);
            }
            
        } catch (Throwable t) {
            logger.error("Fail processing packet", t);
        } finally {
            NDC.pop();
        }
    }

}