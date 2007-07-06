
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
        SIPSessionInfo session = getSessionInfo(ackInfo.getCallID());
        
        if (session != null) {
            session.addRequestInfo(ackInfo);
        } else {
            StringBuilder sb = new StringBuilder("Session not found for request ");
            sb.append(Request.ACK);
            logger.debug(sb);
        }
    }
    
    private void processBye(SIPRequestInfo byeInfo) {
        SIPSessionInfo session = getSessionInfo(byeInfo.getCallID());
        
        if (session != null) {
            session.addRequestInfo(byeInfo);
        } else {
            StringBuilder sb = new StringBuilder("Session not found for request ");
            sb.append(Request.ACK);
            logger.debug(sb);
        }
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
