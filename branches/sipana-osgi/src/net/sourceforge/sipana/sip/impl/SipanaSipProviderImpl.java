package net.sourceforge.sipana.sip.impl;

import java.util.HashMap;

import javax.sip.message.Request;

import net.sourceforge.sipana.sip.SIPRequestInfo;
import net.sourceforge.sipana.sip.SIPResponseInfo;
import net.sourceforge.sipana.sip.SIPSessionInfo;
import net.sourceforge.sipana.sip.SipanaSipProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
        unkownRequest = 0;
        unkownResponse = 0;
    }

    public void processRequest(SIPRequestInfo requestInfo) {
        String method = requestInfo.getMethod();
        
        if (method.equals(Request.INVITE)) {
            processInvite(requestInfo);
        } else if (method.equals(Request.ACK)) {
            processAck(requestInfo);
        } else if (method.equals(Request.BYE)) {
            processBye(requestInfo);
        } else if (method.equals(Request.CANCEL)) {
            processCancel(requestInfo);
        } else {
            unkownRequest++;
            StringBuilder sb = new StringBuilder("Unknown request method ");
            sb.append(method);
            logger.debug(sb);
        }
    }

    public void processResponse(SIPResponseInfo responseInfo) {
        // TODO Auto-generated method stub
        
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
        String callId = ackInfo.getCallID();
        SIPSessionInfo session = getSessionInfo(callId);
        
        if (session != null) {
            session.addRequestInfo(ackInfo);
        } else {
            StringBuilder sb = new StringBuilder("Session not found for request ");
            sb.append(Request.ACK);
            logger.debug(sb);
        }
    }
    
    private void processBye(SIPRequestInfo byeInfo) {
        
    }
    
    private void processCancel(SIPRequestInfo requestInfo) {
        
    }
    
    private void processFinalResponse(SIPResponseInfo respInfo) {
        
    }
    
    private void processProvisionalResponse(SIPResponseInfo respInfo) {
        
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
    
}
