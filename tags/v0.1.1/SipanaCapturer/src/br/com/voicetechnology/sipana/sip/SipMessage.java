package br.com.voicetechnology.sipana.sip;

import java.util.Date;

public class SipMessage {
	public static final int REQUEST  = 1;
	public static final int RESPONSE = 2;
	private String callId;
	private Date date;
	private String srcIpAddr;
	private String dstIpAddr;
	
	public SipMessage() {
	}
	
	public String getCallId() {
		return callId;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setCallId(String call_id) {
		this.callId = call_id;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public String getDstIpAddr() {
		return dstIpAddr;
	}

	public void setDstIpAddr(String dstIpAddr) {
		this.dstIpAddr = dstIpAddr;
	}

	public String getSrcIpAddr() {
		return srcIpAddr;
	}

	public void setSrcIpAddr(String srcIpAddr) {
		this.srcIpAddr = srcIpAddr;
	}
}
