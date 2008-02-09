package org.sipana.protocol.sip.impl;

import gov.nist.javax.sip.parser.ParseExceptionListener;
import gov.nist.javax.sip.parser.StringMsgParser;

import java.text.ParseException;

import javax.sip.address.SipURI;

import org.apache.log4j.Logger;
import org.sipana.protocol.sip.SIPFactory;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPResponse;
import org.sipana.protocol.sip.SIPSession;

public class SIPFactoryImpl implements SIPFactory, ParseExceptionListener {
    private static SIPFactory instance;
    private StringMsgParser parser = new StringMsgParser();
    private Logger logger;

    private SIPFactoryImpl() {
        parser = new StringMsgParser(this);
        logger = Logger.getLogger(SIPFactory.class);
    }

    public SIPMessage createMessage(byte[] rawMessage) throws ParseException {
        gov.nist.javax.sip.message.SIPMessage sipMessage = parser.parseSIPMessage(rawMessage);
        return createMessage(sipMessage);
    }

    public SIPMessage createMessage(
            gov.nist.javax.sip.message.SIPMessage sipMessage) {
        SIPMessage message = null;

        if (sipMessage instanceof gov.nist.javax.sip.message.SIPRequest) {
            message = new SIPRequestImpl();
            gov.nist.javax.sip.message.SIPRequest sipRequest = (gov.nist.javax.sip.message.SIPRequest) sipMessage;
            SIPRequest request = (SIPRequest) message;

            request.setMethod(sipRequest.getMethod());
            request.setMaxForwards(sipRequest.getMaxForwards().getMaxForwards());

            // Request address host:port
            SipURI requestURI = (SipURI) sipRequest.getRequestLine().getUri();
            StringBuilder sbAddr = new StringBuilder(requestURI.getHost());
            sbAddr.append(":").append(requestURI.getPort());
            request.setRequestAddr(sbAddr.toString());

        } else if (sipMessage instanceof gov.nist.javax.sip.message.SIPResponse) {
            message = new SIPResponseImpl();
            gov.nist.javax.sip.message.SIPResponse sipResponse = (gov.nist.javax.sip.message.SIPResponse) sipMessage;
            SIPResponse response = (SIPResponse) message;

            response.setStatusCode(sipResponse.getStatusCode());
            response.setReasonPhrase(sipResponse.getReasonPhrase());
            response.setRelatedRequestMethod(sipResponse.getCSeq().getMethod());
        }

        if (message != null) {
            message.setCallID(sipMessage.getCallId().getCallId());

            // From user@domain
            SipURI fromURI = (SipURI) sipMessage.getFrom()
                    .getAddress()
                    .getURI();
            message.setFromUser(getUser(fromURI));

            // To user@domain
            SipURI toURI = (SipURI) sipMessage.getTo().getAddress().getURI();
            message.setToUser(getUser(toURI));

        } else {
            logger.warn("Message info is null");
        }

        return message;
    }

    private String getUser(SipURI uri) {
        StringBuilder sbUser = new StringBuilder(uri.getUser());
        sbUser.append("@").append(uri.getHost());
        return sbUser.toString();
    }

    public static synchronized SIPFactory getInstance() {
        if (instance == null) {
            instance = new SIPFactoryImpl();
        }
        return instance;
    }

    public void handleException(ParseException ex,
            gov.nist.javax.sip.message.SIPMessage sipMessage,
            Class headerClass, String headerText, String messageText) {
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
            logger.error("Fail handling NIST JAIN-SIP exception: "
                    + t.getMessage(), t);
        }
    }

    public SIPSession createSession(SIPRequest request) {
        return new SIPSessionImpl(request);
    }
}
