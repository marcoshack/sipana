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
package org.sipana.protocol.sip;

import gov.nist.javax.sip.parser.ParseExceptionListener;
import gov.nist.javax.sip.parser.StringMsgParser;

import java.text.ParseException;

import javax.sip.address.SipURI;

import org.apache.log4j.Logger;

public class SIPFactory implements ParseExceptionListener {
    private static SIPFactory instance;
    private StringMsgParser parser = new StringMsgParser();
    private Logger logger;

    private SIPFactory() {
        parser = new StringMsgParser(this);
        logger = Logger.getLogger(SIPFactory.class);
    }

    public SIPMessage createMessage(String data) throws ParseException {
        if (logger.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder("Creating SIP message from string data:\n");
            sb.append(data);
            logger.trace(sb);
        }

        gov.nist.javax.sip.message.SIPMessage sipMessage = parser.parseSIPMessage(data);
        return createMessage(sipMessage);
    }

    public SIPMessage createMessage(
            gov.nist.javax.sip.message.SIPMessage sipMessage) {
        SIPMessage message = null;

        if (sipMessage != null) {
            if (sipMessage instanceof gov.nist.javax.sip.message.SIPRequest) {
                message = new SIPRequest();
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
                message = new SIPResponse();
                gov.nist.javax.sip.message.SIPResponse sipResponse = (gov.nist.javax.sip.message.SIPResponse) sipMessage;
                SIPResponse response = (SIPResponse) message;

                response.setStatusCode(sipResponse.getStatusCode());
                response.setReasonPhrase(sipResponse.getReasonPhrase());
                response.setRelatedRequestMethod(sipResponse.getCSeq().getMethod());
            }

            if (message != null) {
                message.setCallID(sipMessage.getCallId().getCallId());

                // From user@domain
                SipURI fromURI = (SipURI) sipMessage.getFrom().getAddress().getURI();
                message.setFromUser(getUser(fromURI));

                // From display
                String fromDisplay = sipMessage.getFrom().getAddress().getDisplayName();
                message.setFromDisplay(fromDisplay);

                // To user@domain
                SipURI toURI = (SipURI) sipMessage.getTo().getAddress().getURI();
                message.setToUser(getUser(toURI));

                // To display
                String toDisplay = sipMessage.getTo().getAddress().getDisplayName();
                message.setToDisplay(toDisplay);

            } else {
                logger.warn("Message info is null");
            }
        } else {
            logger.warn("SIP message is null");
        }

        return message;
    }

    private String getUser(SipURI uri) {
        String user = uri.getUser();
        String host = uri.getHost();
        StringBuilder sbUser = new StringBuilder();
        
        if (user != null) {
            sbUser.append(user).append("@");
        }

        sbUser.append(host);
        return sbUser.toString();
    }

    public static synchronized SIPFactory getInstance() {
        if (instance == null) {
            instance = new SIPFactory();
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
                StringBuilder sb = new StringBuilder("Parse exception occured but I could't get the Call-ID of the message. Header text: \"");
                sb.append(headerText);
                sb.append("\"");
                logger.warn(sb, ex);
            }
        } catch (Throwable t) {
            logger.error("Fail handling NIST JAIN-SIP parse exception", t);
        }
    }

    public SIPSession createSession(SIPRequest request) {
        return new SIPSession(request);
    }
}
