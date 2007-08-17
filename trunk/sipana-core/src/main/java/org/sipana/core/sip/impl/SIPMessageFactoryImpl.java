package org.sipana.core.sip.impl;

import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import gov.nist.javax.sip.parser.ParseExceptionListener;
import gov.nist.javax.sip.parser.StringMsgParser;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.sipana.core.sip.SIPMessageFactory;
import org.sipana.core.sip.SIPMessageInfo;
import org.sipana.core.sip.SIPRequestInfo;
import org.sipana.core.sip.SIPResponseInfo;


public class SIPMessageFactoryImpl implements SIPMessageFactory, ParseExceptionListener {
    private static SIPMessageFactory instance;
    private StringMsgParser parser = new StringMsgParser();
    private Logger logger;
    
    private SIPMessageFactoryImpl() {
        parser = new StringMsgParser(this);
        logger = Logger.getLogger(SIPMessageFactory.class);
    }

    public SIPMessageInfo createMessage(byte[] rawMessage) throws ParseException {
        SIPMessage sipMessage = parser.parseSIPMessage(rawMessage);
        SIPMessageInfo messageInfo = null;
        
        if (sipMessage instanceof SIPRequest) {
            messageInfo = new SIPRequestInfoImpl();
            SIPRequest sipRequest = (SIPRequest)sipMessage;
            SIPRequestInfo request = (SIPRequestInfo)messageInfo;
            request.setMethod(sipRequest.getMethod());
        } else if (sipMessage instanceof SIPResponse) {
            messageInfo = new SIPResponseInfoImpl();
            SIPResponse sipResponse = (SIPResponse)sipMessage;
            SIPResponseInfo response = (SIPResponseInfo)messageInfo;
            response.setStatusCode(sipResponse.getStatusCode());
            response.setReasonPhrase(sipResponse.getReasonPhrase());
            response.setRelatedRequestMethod(sipResponse.getCSeq().getMethod());
        }

        if (messageInfo != null) {
            messageInfo.setCallID(sipMessage.getCallId().getCallId());
            messageInfo.setFromUser(sipMessage.getFrom().getAddress().toString());
            messageInfo.setToUser(sipMessage.getTo().getAddress().toString());
        } else {
            logger.warn("Message info is null");
        }

        return messageInfo;
    }

    public static synchronized SIPMessageFactory getInstance() {
        if (instance == null) {
            instance = new SIPMessageFactoryImpl();
        }
        return instance;
    }

    public void handleException(ParseException ex, SIPMessage sipMessage,
            Class headerClass, String headerText, String messageText)
    {
        try {
            if (sipMessage != null && sipMessage.getCallId() != null) {
                String call_id = sipMessage.getCallId().getCallId();
                StringBuilder sb = new StringBuilder("Error parsing header \"");
                sb.append(headerText);
                sb.append("\" in message with Call-ID: ");
                sb.append(call_id);
                logger.warn(sb, ex);
            } else {
                StringBuilder sb = new StringBuilder("Parse exception occured but I can't get the Call-ID of the message. Header text: \"");
                sb.append(headerText);
                sb.append("\"");
                logger.warn(sb, ex);
            }
        } catch (Throwable t) {
            logger.error("Fail handling NIST JAIN-SIP exception: " + t.getMessage(), t);
        }
    }
}
