package org.sipana.server.web;

import java.util.Calendar;
import java.util.List;

import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.impl.SIPSessionImpl;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.service.ServiceLocatorException;
import org.sipana.server.sip.SIPSessionManager;
import org.vraptor.annotations.Component;
import org.vraptor.annotations.Out;
import org.vraptor.annotations.Parameter;

@Component
public class SIPSessionLogic {
	
	@Out
	private List<SIPSessionImpl> sipSessions = null;
	
	@Out
	private List<SIPMessage> sipMessages = null;
	
	@Parameter
	private long id = 0;
	
	@Parameter
	private String call_id = null;
	
	public void list() throws Exception {
		this.sipSessions = getSIPSessionManager().getSIPSessions(0, Calendar.getInstance().getTimeInMillis());
	}
	
	public void show() throws Exception {
		if (id != 0) {
			this.sipMessages = getSIPSessionManager().getMessageListBySessionId(id);
		} else if (call_id != null) {
			processCallIDList(call_id);
		}
	}
	
	private void processCallIDList(String list) throws ServiceLocatorException {
		String items[] = list.split(",");
		sipMessages = getSIPSessionManager().getMessageListByCallID(items);
	}

	private SIPSessionManager getSIPSessionManager() throws ServiceLocatorException {
		ServiceLocator serviceLocator = ServiceLocator.getInstance();
		return (SIPSessionManager) serviceLocator.getService(Service.SIP_SESSION_MANAGER);
	}
}
