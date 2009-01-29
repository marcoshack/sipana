package br.com.voicetechnology.sipana.sip;

public class SipRequest extends SipMessage {
	public static final String CANCEL = "CANCEL";
	public static final String ACK = "ACK";
	
	private String method;
	
	public String getMethod() {
		return method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
}
