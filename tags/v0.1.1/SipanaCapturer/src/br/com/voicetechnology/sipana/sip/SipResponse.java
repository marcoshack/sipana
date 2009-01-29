package br.com.voicetechnology.sipana.sip;

public class SipResponse extends SipMessage {
	private int status_code;
	private String reason_phrase;

	public int getStatusCode() {
		return status_code;
	}
	
	public String getReasonPhrase() {
		return reason_phrase;
	}
	
	public void setStatusCode(int code) {
		status_code = code;
	}
	
	public void setReasonPhrase(String phrase) {
		reason_phrase = phrase;
	}
}
