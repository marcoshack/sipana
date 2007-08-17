package br.com.voicetechnology.sipana.sip;

import java.text.ParseException;
import org.apache.log4j.Logger;
import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.parser.ParseExceptionListener;
import gov.nist.javax.sip.parser.StringMsgParser;

public class SipMessageParser implements ParseExceptionListener {
	private Logger logger;

	public SipMessageParser() {
		logger = Logger.getLogger(SipMessageParser.class);
	}
	
	public SIPMessage parse(byte[] data) throws ParseException {
		StringMsgParser parser = new StringMsgParser(this);
		SIPMessage message = parser.parseSIPMessage(data);
		return message;
	}
	
	public int getSipMessageType(SIPMessage message) {
		String first_line = message.getFirstLine().replace("\n", "");
		
		if (first_line.startsWith("SIP/2.0")) {
			return SipMessage.RESPONSE;
		} else {
			return SipMessage.REQUEST;
		}
		
	}

	public void handleException(ParseException ex, SIPMessage sipMessage, Class headerClass, String headerText, String messageText) throws ParseException {
		String call_id = sipMessage.getCallId().getCallId();
		logger.warn("Error parsing header \"" + headerText + "\" [" + call_id + "]");
	}
}
