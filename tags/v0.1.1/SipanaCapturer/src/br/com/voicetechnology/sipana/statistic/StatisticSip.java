package br.com.voicetechnology.sipana.statistic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;

import br.com.voicetechnology.sipana.SipanaCapturer;
import br.com.voicetechnology.sipana.sip.SipTransaction;

public class StatisticSip {
	private static volatile StatisticSip unique_instance;
	private LinkedHashMap<String, StatisticSipRequest> in_requests;
	private LinkedHashMap<String, StatisticSipRequest> out_requests;
	private long request_counter;
	private long timeout_counter;
	private long pending_transaction_counter;
	private Logger logger;
	private String targetIp;
	
	private StatisticSip() {
		in_requests = new LinkedHashMap<String, StatisticSipRequest>();
		out_requests = new LinkedHashMap<String, StatisticSipRequest>();
		request_counter = pending_transaction_counter = 0;
		logger = Logger.getLogger(StatisticSip.class);
		targetIp = SipanaCapturer.getProperties().getProperty("sipana.capturer.targetIp");
	}
	
	public static StatisticSip getInstance() {
		if (unique_instance == null) {
			synchronized (StatisticSip.class) {
				if (unique_instance == null) {
					unique_instance = new StatisticSip();
					return unique_instance;
				}
			}
		}
		
		return unique_instance;
	}
	
	public void addTransaction(SipTransaction transaction) {
		
		LinkedHashMap<String, StatisticSipRequest> requests = getRequests(transaction);
		
		synchronized (requests) {
			String method = transaction.getMethod();
			StatisticSipRequest request = requests.get(method);
			
			if (request == null) {
				request = new StatisticSipRequest(transaction);
				requests.put(method, request);
				logger.debug("Transaction " + method + " added");
			}
			
			request.addTransaction(transaction);
			request_counter++;
		}
	}

	private LinkedHashMap<String, StatisticSipRequest> getRequests(SipTransaction transaction) {
		LinkedHashMap<String, StatisticSipRequest> requests;
		
		if (transaction.getRequestDstIpAddr().equals(targetIp)) {
			requests = in_requests;
		} else {
			requests = out_requests;
		}
		
		return requests;
	}

	public ArrayList<String> getIncomingRequestMethods() {
		ArrayList<String> methods = new ArrayList<String>();
		
		synchronized (in_requests) {
			for (String method : in_requests.keySet()) {
				methods.add(method);
			}
		}
		
		return methods;
	}

	public ArrayList<String> getOutgoingRequestMethods() {
		ArrayList<String> methods = new ArrayList<String>();
			
		synchronized (out_requests) {
			for (String method : out_requests.keySet()) {
				methods.add(method);
			}
		}

		return methods;
	}
	
	public StatisticSipRequest getIncomingRequestStatistic(String method) {
		return in_requests.get(method);
	}
	
	public StatisticSipRequest getOutgoingRequestStatistic(String method) {
		return out_requests.get(method);
	}

	public long getRequestCounter() {
		return request_counter;
	}
	
	public void incPendingTransactionCounter() {
		pending_transaction_counter++;
	}
	
	public void decPendingTransactionCounter() {
		pending_transaction_counter--;
	}
	
	public long getPendingTransactionCounter() {
		return pending_transaction_counter;
	}
	
	public void addTimeout(SipTransaction transaction) {
		LinkedHashMap<String, StatisticSipRequest> requests = getRequests(transaction);
		requests.get(transaction.getMethod()).incTimeoutCounter();
		timeout_counter++;
	}
	
	public long getTimeoutCounter() {
		return timeout_counter;
	}

	public String getTargetIpAddr() {
		return targetIp;
	}
}

