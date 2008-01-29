<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="org.sipana.protocol.sip.impl.SIPSessionImpl"%>

<jsp:include page="/jsp/header.jsp"></jsp:include>

<div id="debug">
query string = "<%= request.getAttribute("query_string") %>"
<div/>

<h3>SIP session info</h3>

<div id="avg_session_info">
SIP session count: <%= request.getAttribute("sip_session_count") %> <br>
AvgSessionRequestDelay: <%= request.getAttribute("avg_request_delay") %> ms <br>
AvgDisconnectDelay: <%= request.getAttribute("avg_disconnect_delay") %> ms <br>
<div/>

<h3>SIP session list</h3>
<div id="session_list">
<% 
SIPSessionImpl[] sip_sessions = (SIPSessionImpl[])request.getAttribute("sip_sessions");

for (int i = 0 ; i < sip_sessions.length ; i++) {
%>
    <a href="/sipana/sip_session/?id=<%= sip_sessions[i].getId() %>">
	<%= sip_sessions[i].getMethod() + " " + sip_sessions[i].getCallId() %> 
	</a><br>
<%
} 

%>
</div>

<jsp:include page="/jsp/footer.jsp"></jsp:include>
