package br.com.voicetechnology.sipana.sip;

import java.util.Date;
import java.util.LinkedList;

public class SipTransaction {
	private LinkedList<SipResponse> responses;
	private SipRequest request;
	private boolean finished = false;
	
	public SipTransaction() {
		 responses = new LinkedList<SipResponse>();
	}
	
	public SipTransaction(SipRequest request) {
		this();
		this.request = request;
	}
	
	public void addResponse(SipResponse response) {
		synchronized (responses) {
			responses.add(response);
		}
	}

	public Date getStartDate() {
		return request.getDate();
	}
	
	public Date getEndDate() {
		if (isFinished()) {
			synchronized (responses) {
				SipMessage last_message = responses.getLast();
				
				if (last_message != null) {
					return last_message.getDate();
				}
			}
		}
		
		return null;
	}
	
	public long getDuration() {
		if (isFinished()) {
			return getEndDate().getTime() - getStartDate().getTime();
		} else {
			return 0;
		}
	}
	
	
	public String getMethod() {
		return request.getMethod();
	}

	public String getCallId() {
		return request.getCallId();
	}
	
	public LinkedList<SipResponse> getResponses() {
		return responses;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public synchronized void setFinished() {
		finished = true;
	}

	public SipResponse getFinalResponse() {
		if (isFinished()) {
			synchronized (responses) {
				return responses.getLast();
			}
		} else {
			return null;
		}
	}
	
	public String getRequestSrcIpAddr() {
		return request.getSrcIpAddr();
	}
	
	public String getRequestDstIpAddr() {
		return request.getDstIpAddr();
	}
}
