package org.sipana.sip.impl;

import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import gov.nist.javax.sip.parser.ParseExceptionListener;
import gov.nist.javax.sip.parser.StringMsgParser;

import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sipana.sip.SIPMessageFactory;
import org.sipana.sip.SIPMessageInfo;
import org.sipana.sip.SIPRequestInfo;
import org.sipana.sip.SIPResponseInfo;


public class SIPMessageFactoryImpl implements SIPMessageFactory, ParseExceptionListener {
    private static SIPMessageFactory instance;
    private StringMsgParser parser = new StringMsgParser();
    private Log logger;
    
    private SIPMessageFactoryImpl() {
        parser = new StringMsgParser(this);
        logger = LogFactory.getLog(SIPMessageFactory.class);
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

        messageInfo.setCallID(sipMessage.getCallId().getCallId());
        messageInfo.setFromUser(sipMessage.getFrom().getAddress().toString());
        messageInfo.setToUser(sipMessage.getTo().getAddress().toString());

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
            throws ParseException
    {
        String call_id = sipMessage.getCallId().getCallId();
        StringBuilder sb = new StringBuilder("Error parsing heander \"");
        sb.append(headerText);
        sb.append("\" in message with Call-ID: ");
        sb.append(call_id);
        logger.warn(sb, ex);
    }
}
