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

@Component
public class SIPSessionLogic {
	
	@Out
	private List<SIPSessionImpl> sipSessions;
	
	@Out
	private List<SIPMessage> sipMessages;
	
	public void list() throws Exception {
		this.sipSessions = getSIPSessionManager().getSIPSessions(0, Calendar.getInstance().getTimeInMillis());
	}
	
	public void show(long id) throws Exception {
		this.sipMessages = getSIPSessionManager().getMessageListBySessionId(id);
	}
	
	private SIPSessionManager getSIPSessionManager() throws ServiceLocatorException {
		ServiceLocator serviceLocator = ServiceLocator.getInstance();
		return (SIPSessionManager) serviceLocator.getLocalService(Service.SIP_SESSION_MANAGER);
	}
}
