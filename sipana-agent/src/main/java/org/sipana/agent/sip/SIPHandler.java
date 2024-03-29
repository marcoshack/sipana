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
package org.sipana.agent.sip;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.sip.message.Request;
import javax.sip.message.Response;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.sipana.agent.capture.CaptureListener;
import org.sipana.agent.capture.Packet;
import org.sipana.agent.sender.Sender;
import org.sipana.protocol.sip.SIPSessionState;
import org.sipana.protocol.sip.SIPFactory;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPResponse;
import org.sipana.protocol.sip.SIPSession;

public class SIPHandler implements CaptureListener {
    private Logger logger;
    private ConcurrentMap<String, SIPSession> sessionList;
    private SIPFactory messageFactory;
    private Sender messageSender;
    
    public SIPHandler(Sender sender) {
        logger = Logger.getLogger(SIPHandler.class);
        sessionList = new ConcurrentHashMap<String, SIPSession>();
        messageFactory = SIPFactory.getInstance();
        messageSender = sender;
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
            session.addMessage(response);
            
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
            logger.debug("Response session not found. Sending standalone message to the server");
            
            try {
                messageSender.send(response);
            } catch (Exception e) {
                logger.error("Error sending standalone message", e);
            }
        }

    }
    
    private void processInvite(SIPRequest invite) {
        String callId = invite.getCallID();
        
        if (!sessionList.containsKey(callId)) {
            SIPSession newSession = messageFactory.createSession(invite);
            addSession(newSession);
        } else {
            getSession(callId).addMessage(invite);
        }
    }

    private void processAck(SIPRequest ack) {
        SIPSession session = getSession(ack.getCallID());
        
        if (session != null) {
            session.addMessage(ack);
            
            if (session.getState() == SIPSessionState.FAILED) {
                terminateSession(session);
            }
            
        } else {
            logger.debug("Session not found for request ACK");
        }
    }
    
    private void processDisconnectRequest(SIPRequest request) {
        SIPSession session = getSession(request.getCallID());
        
        if (session != null) {
            session.addMessage(request);
            session.setDisconnectionStart(request.getTime());
            session.setState(SIPSessionState.DISCONNECTING);
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
            // Only the first provisional response defines the setup time
            if (session.getState() == SIPSessionState.INITIATED) {
                session.setSetupTime(responseTime);
                session.setState(SIPSessionState.PROVISIONED);
            }
            break;
        default:
            // Do nothing
            break;
        }
    }
    
    private void processSucessfulResponse(SIPSession session, SIPResponse response) {
        int statusCode = response.getStatusCode();
        String method = response.getRelatedRequestMethod();
        
        switch (statusCode) {
            case Response.OK:
                if (method.equals(Request.INVITE)) {
                    processInviteOK(session, response);
                    
                } else if (method.equals(Request.CANCEL)) {
                    processCancelOK(session, response);
                    
                } else if (method.equals(Request.BYE)) {
                    processByeOK(session, response);
                    
                } else {
                    long respTime = response.getTime();
                    
                    // non-INVITE related request has its setup time defined with OK
                    session.setSetupTime(respTime);
                              
                    // non-INVITE request ends with OK
                    session.setEndTime(respTime);
                    session.setState(SIPSessionState.COMPLETED);
                    terminateSession(session);
                }
                
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
    
    private void processByeOK(SIPSession session, SIPResponse response) {
        long respTime = response.getTime();
        session.setEndTime(respTime);
        session.setState(SIPSessionState.COMPLETED);
        terminateSession(session);
    }

    private void processCancelOK(SIPSession session, SIPResponse response) {
        long respTime = response.getTime();
        session.setEndTime(respTime);
        session.setState(SIPSessionState.CANCELED);
        terminateSession(session);
    }

    private void processInviteOK(SIPSession session, SIPResponse response) {
        long respTime = response.getTime();
        
        // Whether the session wasn't provisioned than setup time is 
        // also defined with OK
        if (session.getState() == SIPSessionState.INITIATED) {
            session.setSetupTime(respTime);
        } 
        
        if (session.getState() == SIPSessionState.PROVISIONED ||
            session.getState() == SIPSessionState.INITIATED) 
        {
            session.setEstablishedTime(respTime);
            session.setState(SIPSessionState.ESTABLISHED);
        }
        
        // No action for Re-INVITE OK
    }

    private void processRedirectionResponse(SIPSession session, SIPResponse response) {
        // Do nothing
    }

    private void processFailureResponse(SIPSession session, SIPResponse response) {
        long responseTime = response.getTime();
        session.setEndTime(responseTime);
        session.setState(SIPSessionState.FAILED);
        
        if (session.getState() != SIPSessionState.ESTABLISHED) {
            session.setSetupTime(responseTime);
        }
        
        terminateSession(session);
    }
    
    private void addSession(SIPSession session) {
        logger.debug("Adding session to session list");
        sessionList.put(session.getCallId(), session);
    }
    
    private void removeSession(SIPSession session) {
        logger.debug("Removing session from session list");
        sessionList.remove(session.getCallId());
    }
    
    private SIPSession getSession(String callId) {
        return sessionList.get(callId);
    }
    
    private void terminateSession(SIPSession session) {
        String id = session.getCallId();
        
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Terminating SIP session ");
            sb.append(id);
            logger.debug(sb);
        }
        
        try {
            messageSender.send(session);
            
        } catch (Exception e) {
            logger.error("Error sending SIPSession: " + e.getMessage(), e);
        }
        
        removeSession(session);
    }

    public SIPFactory getMessageFactory() {
        return SIPFactory.getInstance();
    }

    public int getCurrentSessionNumber() {
        return sessionList.size();
    }

    public void onPacket(Packet packet) {
        try {
            SIPMessage message = messageFactory.createMessage(packet.getData());
            
            String callId = message.getCallID();
            NDC.push(new StringBuilder("callId=").append(callId).toString());
            
            String srcAddr = packet.getSrcIPAddr();
            int srcPort    = packet.getSrcIPPort();
            String dstAddr = packet.getDstIPAddr();
            int dstPort    = packet.getDstIPPort();
            message.setSrcAddress(srcAddr);
            message.setSrcPort(srcPort);
            message.setDstAddress(dstAddr);
            message.setDstPort(dstPort);
            message.setTime(packet.getDate().getTime());
            
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Processing new message: ");
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
