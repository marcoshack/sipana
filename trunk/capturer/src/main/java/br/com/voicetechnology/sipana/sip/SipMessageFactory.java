package br.com.voicetechnology.sipana.sip;

import java.text.ParseException;
import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;

public class SipMessageFactory {
    private static SipMessageParser parser = new SipMessageParser();

    public static SipMessage createSipMessage(byte[] rawMessage)
            throws ParseException {
        SipMessage message = null;
        SIPMessage sipMessage = parser.parse(rawMessage);

        // TODO: Verificar se instanceof eh muito "caro"
        if (sipMessage instanceof SIPRequest) {
            message = createSipRequest(sipMessage);
        } else if (sipMessage instanceof SIPResponse) {
            message = createSipResponse(sipMessage);
        }

        return message;
    }

    public static SipRequest createSipRequest(SIPMessage message) {
        SipRequest request = new SipRequest();
        request.setCallId(message.getCallId().getCallId());
        request.setMethod(((SIPRequest) message).getMethod());
        return request;
    }

    public static SipResponse createSipResponse(SIPMessage message) {
        SipResponse response = new SipResponse();
        response.setCallId(message.getCallId().getCallId());
        response.setStatusCode(((SIPResponse) message).getStatusCode());
        response.setReasonPhrase(((SIPResponse) message).getReasonPhrase());
        return response;
    }
}
