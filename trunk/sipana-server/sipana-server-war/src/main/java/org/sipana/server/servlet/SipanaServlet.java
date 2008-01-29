package org.sipana.server.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPResponse;
import org.sipana.protocol.sip.SIPScenario;
import org.sipana.protocol.sip.SIPSession;
import org.sipana.protocol.sip.impl.SIPSessionImpl;
import org.sipana.server.service.Service;
import org.sipana.server.service.ServiceLocator;
import org.sipana.server.service.ServiceLocatorException;
import org.sipana.server.sip.SIPPerformanceMetrics;
import org.sipana.server.sip.SIPSessionManager;

public class SipanaServlet extends HttpServlet
{
    private static final long serialVersionUID = 3542804712826905482L;
    
    private Logger logger = Logger.getLogger(SipanaServlet.class);
    private String SIP_SCENARIO_ROOT_DIR;
    private String WEBAPP_ROOT;
    private static String SIP_SCENARIO_DIR = "scenarios";
    ServiceLocator serviceLocator;
    
    public void init() throws ServletException {
        logger.debug("Initializing SipanaServlet");
        serviceLocator = ServiceLocator.getInstance();
        SIP_SCENARIO_ROOT_DIR = getServletContext().getRealPath("/") + SIP_SCENARIO_DIR;
        WEBAPP_ROOT = getServletContext().getContextPath();
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException
    {
        try {
            String query = request.getQueryString();
            String pagename = null;
    		
    		if (query != null) {
	    		request.setAttribute("query_string", query);
	        	
	        	String[] args = query.split("=");
	        	String query_param = args[0];
	        	
	        	if (query_param.equals("id")) {
	        		Long id = Long.getLong(args[1]);
	        		pagename = processSIPSession(request, id);
	        	}

    		} else {
	            pagename = processListSIPSessions(request);
    		}
            
    		// Forward
    		ServletContext context = getServletContext();
            RequestDispatcher dispatcher = context.getRequestDispatcher(pagename);
            dispatcher.forward(request, response);
    		
        } catch (ServiceLocatorException e) {
            throw new ServletException("Fail getting SIPSessionManager", e);
        }
    }
    
	private String processListSIPSessions(HttpServletRequest request) throws ServiceLocatorException {
		request.setAttribute("sip_session_count", getSIPSessionManager().getSIPSessionCount());
        
        // test
        long now = Calendar.getInstance().getTimeInMillis();
        List<SIPSessionImpl> sessionList = getSIPSessionManager().getSIPSessions(0, now);
        
        long avgRequestDelay = getSIPPerformanceMetrics().getAvgSessionRequestDelay(sessionList);
        request.setAttribute("avg_request_delay", avgRequestDelay);
        
        long avgDisconnectDelay = getSIPPerformanceMetrics().getAvgSessionDisconnectDelay(sessionList);
        request.setAttribute("avg_disconnect_delay", avgDisconnectDelay);
        
        SIPSessionImpl[] sessions = new SIPSessionImpl[sessionList.size()];
        
        for (int i = 0 ; i < sessionList.size() ; i++) {
            sessions[i] = sessionList.get(i);
        }
        
        request.setAttribute("sip_sessions", sessions);
        return "/jsp/sip_session.jsp";
	}

	private SIPSessionManager getSIPSessionManager() throws ServiceLocatorException {
		return (SIPSessionManager) serviceLocator.getLocalService(Service.SIP_SESSION_MANAGER);
	}
	
	private SIPPerformanceMetrics getSIPPerformanceMetrics() throws ServiceLocatorException { 
		return (SIPPerformanceMetrics) serviceLocator.getLocalService(Service.SIP_PERFORMANCE_METRICS);
	}

	private String processSIPSession(HttpServletRequest request, long sip_session_id) 
		throws ServiceLocatorException 
	{
		// TODO Auto-generated method stub
        // As described on JBoss EJB3 documentation [1], they have not yet 
        // (version 4.2.1.GA) updated tomcat to support @EJB annotations. 
        // So for now, we must lookup the EJB via its global JNDI name. 
        // Here we are using the class ServiceLocator to do it.
        // 
        // TODO Use @EJB annotation to inject SIPSessionManager
        // 
        // [1] http://docs.jboss.org/ejb3/app-server/tutorial/ear/ear.html
        //
        
//    	List<SIPMessage> messages = manager.getMessageListBySessionId(sip_session_id);
//    	
//    	if (messages != null) {
//    		request.setAttribute("sip_session_id", sip_session_id);
//    		String filename = SIPScenario.createSIPScenario(messages, SIP_SCENARIO_FILEPATH);
//    		request.setAttribute("sip_scenario_filename", filename);
//    	}

    	SIPSession session = getSIPSessionManager().getSIPSession(sip_session_id);
    	if (session != null) {
    		List<SIPMessage> messages = createOrderedMessageList(session);
    		request.setAttribute("sip_session_id", sip_session_id);
    		
    		String filename = SIPScenario.createSIPScenario(messages, SIP_SCENARIO_ROOT_DIR);
    		request.setAttribute("sip_scenario_filename", getSipScenarioWebFilePath(filename));
    	}
    	
    	return "/jsp/sip_scenario.jsp";
	}

	private String getSipScenarioWebFilePath(String filename) {
		StringBuilder filepath = new StringBuilder("http://127.0.0.1:8080/");
		filepath.append(WEBAPP_ROOT);
		filepath.append("/").append(SIP_SCENARIO_DIR);
		
		filepath.append("/").append(filename);
		return filepath.toString();
	}

	private List<SIPMessage> createOrderedMessageList(SIPSession session) {
		List<SIPRequest> requests = session.getRequests();
		List<SIPResponse> responses = session.getResponses();
		
		ArrayList<SIPMessage> messages = new ArrayList<SIPMessage>();
		
		int reqSize = requests.size();
		int resSize = responses.size();
		int reqIndex = 0;
		int resIndex = 0;
		
		while (reqIndex < reqSize && resIndex < resSize) {
			
			SIPRequest req = requests.get(reqIndex);
			SIPResponse res = responses.get(resIndex);
			
			if (req.getTime() <= res.getTime()) {
				messages.add(req);
				reqIndex++;
			} else {
				messages.add(res);
				resIndex++;
			}
		}
		
		while (reqIndex < reqSize) {
			messages.add(requests.get(reqIndex++));
		}
	
		while (resIndex < resSize) {
			messages.add(responses.get(resIndex++));
		}
		
		return messages;
	}
}
