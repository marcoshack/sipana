package br.com.voicetechnology.sipana.statistic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import br.com.voicetechnology.sipana.sip.SipResponse;
import br.com.voicetechnology.sipana.sip.SipTransaction;

public class StatisticSipRequest {
	private int counter;
	private int timeout_counter;
	private String method;
	private LinkedHashMap<Integer, StatisticSipResponse> responses;
	
	public StatisticSipRequest(SipTransaction transaction) {
		responses = new LinkedHashMap<Integer, StatisticSipResponse>();
		counter = 0;
		setMethod(transaction.getMethod());
		addTransaction(transaction);
	}
	
	public void addTransaction(SipTransaction transaction) {
		long start_time = transaction.getStartDate().getTime();
		
		for (SipResponse response: transaction.getResponses()) {
			int code = response.getStatusCode();
			
			StatisticSipResponse response_item = responses.get(code);
			
			if (response_item == null) {
				response_item = new StatisticSipResponse(code);
				responses.put(code, response_item);
			}
		
			long response_time = response.getDate().getTime();
			long delta = response_time - start_time;
			response_item.addResponseTime(delta);
		}
		
		// TODO: Contador parece contar 1 a mais
		counter++;	
	}
	
	private void setMethod(String method) {
		this.method = method;
	}
	
	public String getMethod() {
		return method;
	}
	
	public ArrayList<Integer> getResponseCodes() {
		// TODO: Verificar se exite uma forma mais simples de passar essa lista
		// O problema é que alteracoes no keySet() refletem no objeto real, 
		// e num ambiente assincrono acontecem problemas de concorrencia.
		ArrayList<Integer> codes = new ArrayList<Integer>();
		synchronized (responses) {
			for (int code : responses.keySet()) {
				codes.add(code);
			}
		}
		return codes;
	}
	
	public StatisticSipResponse getResponseStatistic(int status_code) {
		return responses.get(status_code);
	}
	
	public int getCounter() {
		return counter;
	}
	
	public void incTimeoutCounter() {
		timeout_counter++;
	}
	
	public int getTimeoutCounter() {
		return timeout_counter;
	}
}
	
