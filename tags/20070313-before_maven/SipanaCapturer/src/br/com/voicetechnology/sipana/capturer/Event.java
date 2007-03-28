package br.com.voicetechnology.sipana.capturer;

import java.util.Date;

public class Event {
	private Object data;
	private Date date;
	private String srcIpAddr;
	private String dstIpAddr;
	
	public Event() {
	}
	
	public Event(Object data, Date date) {
		this.data = data;
		this.date = date;
	}

	public Object getData() {
		return data;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setData(Object data) {
		this.data = data;
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
