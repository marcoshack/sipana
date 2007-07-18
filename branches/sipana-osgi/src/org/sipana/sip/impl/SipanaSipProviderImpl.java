
/**
 * This file is part of Sipana project <http://sipana.sourceforge.net>
 * 
 * Sipana is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sipana is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */ 

package org.sipana.sip.impl;

import java.util.HashMap;

import javax.sip.message.Request;
import javax.sip.message.Response;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sipana.sip.SIPMessageFactory;
import org.sipana.sip.SIPRequestInfo;
import org.sipana.sip.SIPResponseInfo;
import org.sipana.sip.SIPSessionInfo;
import org.sipana.sip.SipanaSipProvider;

public class SipanaSipProviderImpl implements SipanaSipProvider 
{
    private Log logger;
    private long unkownRequest;
    private long unkownResponse;
    private HashMap<String, SIPSessionInfo> currentSessions;
    private HashMap<String, SIPSessionInfo> terminatedSessions;
    
    public SipanaSipProviderImpl() {
        logger = LogFactory.getLog(SipanaSipProvider.class);
        currentSessions = new HashMap<String, SIPSessionInfo>();
        terminatedSessions = new HashMap<String, SIPSessionInfo>();
        unkownRequest = 0;
        unkownResponse = 0;
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
            unkownRequest++;
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
                unkownResponse++;
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
            getSessionInfo(callId).addRequestInfo(inviteInfo);
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
            session.setDisconnectionStartTime(requestInfo.getTime());
        } else {
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Session not found for request ");
                sb.append(Request.ACK);
                logger.debug(sb);
            }
        }
    }
    
    private void processProvisionalResponse(SIPSessionInfo session, SIPResponseInfo responseInfo) {
        long responseTime = responseInfo.getTime();
        session.setFirstResponseTime(responseTime);
    }
    
    private void processSucessfulResponse(SIPSessionInfo session, SIPResponseInfo responseInfo) {
        long respTime = responseInfo.getTime();
        int statusCode = responseInfo.getStatusCode();
        String method = responseInfo.getRelatedRequestMethod();
        
        switch (statusCode) {
            case Response.OK:
                if (method.equals(Request.INVITE)) {
                    session.setEstablishedTime(respTime);
                } else if (method.equals(Request.BYE)) {
                    session.setEndTime(respTime);
                    terminateSessionInfo(session);
                }
                break;
    
            default:
                if (logger.isDebugEnabled()) {
                    StringBuilder sb = new StringBuilder("Nothing to do to response ");
                    sb.append(statusCode);
                    sb.append(" (");
                    sb.append(responseInfo.getReasonPhrase());
                    sb.append(")");
                    logger.debug(sb);
                }
                break;
        }
    }

    private void processFailureResponse(SIPSessionInfo session, SIPResponseInfo responseInfo) {
        long responseTime = responseInfo.getTime();
        session.setFirstResponseTime(responseTime);
    }
    
    private void addSessionInfo(SIPSessionInfo session) {
        synchronized (currentSessions) {
            currentSessions.put(session.getId(), session);
        }
    }
    
    private SIPSessionInfo getSessionInfo(String callId) {
        synchronized (currentSessions) {
            return currentSessions.get(callId);
        }
    }
    
    private void terminateSessionInfo(SIPSessionInfo session) {
        String id = session.getId();
        
        synchronized (currentSessions) {
            currentSessions.remove(id);
        }
        
        synchronized (terminatedSessions) {
            terminatedSessions.put(id, session);
        }
    }

    public SIPMessageFactory getMessageFactory() {
        return SIPMessageFactoryImpl.getInstance();
    }

}
