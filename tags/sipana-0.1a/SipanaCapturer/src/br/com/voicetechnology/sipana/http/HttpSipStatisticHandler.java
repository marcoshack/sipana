package br.com.voicetechnology.sipana.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.AbstractHandler;
import br.com.voicetechnology.sipana.SipanaCapturer;
import br.com.voicetechnology.sipana.statistic.StatisticSip;
import br.com.voicetechnology.sipana.statistic.StatisticSipRequest;
import br.com.voicetechnology.sipana.statistic.StatisticSipResponse;

public class HttpSipStatisticHandler extends AbstractHandler {
	private static final int DEFAULT_HTML_REFRESH = 300;
	private StatisticSip statistics;
	private int html_refresh;
	
	public HttpSipStatisticHandler(StatisticSip statistics) {
		this.statistics = statistics;
		
		String str_refresh = SipanaCapturer.getProperties().getProperty("sipana.http.refresh");
		
		if (str_refresh != null) {
			html_refresh = Integer.parseInt(str_refresh);
		} else {
			html_refresh = DEFAULT_HTML_REFRESH;
		}
	}

	public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
		Request base_request = (request instanceof Request) ? (Request)request:HttpConnection.getCurrentConnection().getRequest();
		base_request.setHandled(true);
		
		String output = (String) request.getParameter("output");
		
		if (output != null) {
			output = output.toLowerCase();
			
			if (output.equals("html")) {
				response.setContentType("text/html");
				response.setStatus(HttpServletResponse.SC_OK);
		        response.getWriter().println(getHtmlStatistics());
		        return;
			} else if (output.equals("xml")) {
				response.setContentType("text/xml");
				response.setStatus(HttpServletResponse.SC_OK);
		        response.getWriter().println(getXmlStatistics());
		        return;
			}
		}
		
		// If a unknown request output, redirect to HTML
		response.sendRedirect(request.getRequestURI() + "?output=html");
	}
	
	// TODO: Implementar decentemente getHtmlStatistics()
	public String getHtmlStatistics() {
		StringBuffer res_html = new StringBuffer();
		res_html.append("<html>\n<head>\n  <title>Sipana</title>\n");
		res_html.append("  <META HTTP-EQUIV=\"Refresh\" CONTENT=\"" + html_refresh + "\">\n");
		res_html.append("  <META HTTP-EQUIV=\"Cache-Control\" content=\"no-cache\">\n");
		res_html.append("  <META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">\n");
		res_html.append("</head>\n<body>\n");
		res_html.append("<h1>Sipana</h1>\n");
		res_html.append("<h3>Configuration</h3><ul>\n");
		res_html.append("  <li>Target IP: " + statistics.getTargetIpAddr() + "</li>\n</ul>");
		res_html.append("<h3>Transactions Summary</h3><ul>\n");
		res_html.append("  <li>Completed: " + statistics.getRequestCounter() + "</li>\n");
		res_html.append("  <li>Pending: " + statistics.getPendingTransactionCounter() + "</li>\n");
		res_html.append("  <li>Timeout: " + statistics.getTimeoutCounter() + "</li>\n</ul>\n");


		// Incoming (begin)
		res_html.append("<h3>Incoming</h3>\n");
		res_html.append("<table border=1 width=600>\n");
		res_html.append("<tr>\n<th>\n<th><b>Response</b>\n<th><b>Min</b>\n<th><b>Avg</b>\n<th><b>Max</b>\n<th>#\n");

		for (String method : statistics.getIncomingRequestMethods()) {
			StatisticSipRequest request = statistics.getIncomingRequestStatistic(method);
			int response_codes_size = request.getResponseCodes().size();
			
			res_html.append("<tr>\n<th rowspan=" + response_codes_size + ">" + method + " (" + request.getCounter() + "/" + request.getTimeoutCounter() + ")\n");
			int line = 1;
	
			for (int code : request.getResponseCodes()) {
				StatisticSipResponse response = request.getResponseStatistic(code);
				
				if (line++ > 1) {
					res_html.append("<tr>\n");
				}
				
				res_html.append("<td><center>" + code + "</center>\n<td>" + response.getMinTime() + "\n");
				res_html.append("<td>" + response.getSampleAvgTime() + "\n");
				res_html.append("<td>" + response.getMaxTime() + "\n");
				res_html.append("<td>" + response.getCounter() + "\n");
			}
		}
		res_html.append("</table>\n");
		// Incoming (end)
		
		// Outgoing (begin)
		res_html.append("<h3>Outgoing</h3>\n");
		res_html.append("<table border=1 width=600>\n");
		res_html.append("<tr>\n<th>\n<th><b>Response</b>\n<th><b>Min</b>\n<th><b>Avg</b>\n<th><b>Max</b>\n<th>#\n");
		
		for (String method : statistics.getOutgoingRequestMethods()) {
			StatisticSipRequest request = statistics.getOutgoingRequestStatistic(method);
			int response_codes_size = request.getResponseCodes().size();

			res_html.append("<tr>\n<th rowspan=" + response_codes_size + ">" + method + " (" + request.getCounter() + "/" + request.getTimeoutCounter() + ")\n");
			int line = 1;
			
			for (int code : request.getResponseCodes()) {
				StatisticSipResponse response = request.getResponseStatistic(code);
				
				if (line++ > 1) {
					res_html.append("<tr>\n");
				}
				
				res_html.append("<td><center>" + code + "</center>\n<td>" + response.getMinTime() + "\n");
				res_html.append("<td>" + response.getSampleAvgTime() + "\n");
				res_html.append("<td>" + response.getMaxTime() + "\n");
				res_html.append("<td>" + response.getCounter() + "\n");
			}
		}
		res_html.append("</table>\n");
		// Outgoing (end)

		res_html.append("</body>\n</html>");
		return res_html.toString();
	}

	// TODO: Implementar decentemente getXmlStatistics()
	public String getXmlStatistics() {
		StringBuffer res_xml = new StringBuffer();
		
		res_xml.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n\n");
		res_xml.append("<sipana>\n");
		res_xml.append(" <targetip>" + statistics.getTargetIpAddr() + "</targetip>");
		res_xml.append(" <completed>" + statistics.getRequestCounter() + "</completed>\n");
		res_xml.append(" <pending>" + statistics.getPendingTransactionCounter() + "</pending>\n");
		res_xml.append(" <timeout>" + statistics.getTimeoutCounter() + "</timeout>\n\n");

		// Incoming (begin)
		res_xml.append(" <incoming>\n");
		
		for (String method : statistics.getIncomingRequestMethods()) {
			StatisticSipRequest request = statistics.getIncomingRequestStatistic(method);

			res_xml.append("  <request>\n");
			res_xml.append("   <method>" + method + "</method>\n");
			res_xml.append("   <total>" + request.getCounter() + "</total>\n");
			res_xml.append("   <timeout>" + request.getTimeoutCounter() + "</timeout>\n");

			// responses
			for (int code : request.getResponseCodes()) {
				StatisticSipResponse response = request.getResponseStatistic(code);

				res_xml.append("    <response>\n");
				res_xml.append("     <status-code>" + response.getStatusCode() + "</status-code>\n");
				res_xml.append("     <min>" + response.getMinTime() + "</min>\n");
				res_xml.append("     <avg>" + response.getSampleAvgTime() + "</avg>\n");
				res_xml.append("     <max>" + response.getMaxTime() + "</max>\n");
				res_xml.append("    </response>\n");
			}
			
			res_xml.append("  </request>\n");
		}
		res_xml.append(" </incoming>\n");
		// Incomng (end)

		// Outgoing (begin)
		res_xml.append(" <outgoing>\n");
		
		for (String method : statistics.getOutgoingRequestMethods()) {
			StatisticSipRequest request = statistics.getOutgoingRequestStatistic(method);

			res_xml.append("  <request>\n");
			res_xml.append("   <method>" + method + "</method>\n");
			res_xml.append("   <total>" + request.getCounter() + "</total>\n");
			res_xml.append("   <timeout>" + request.getTimeoutCounter() + "</timeout>\n");

			// responses
			for (int code : request.getResponseCodes()) {
				StatisticSipResponse response = request.getResponseStatistic(code);

				res_xml.append("    <response>\n");
				res_xml.append("     <status-code>" + response.getStatusCode() + "</status-code>\n");
				res_xml.append("     <min>" + response.getMinTime() + "</min>\n");
				res_xml.append("     <avg>" + response.getSampleAvgTime() + "</avg>\n");
				res_xml.append("     <max>" + response.getMaxTime() + "</max>\n");
				res_xml.append("    </response>\n");
			}
			
			res_xml.append("  </request>\n");
		}
		res_xml.append(" </outgoing>\n");
		// Outgoing (end)

		res_xml.append("</sipana>\n");
		return res_xml.toString();
	}
}
