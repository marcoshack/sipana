<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="org.sipana.protocol.sip.impl.SIPSessionImpl"%>

<jsp:include page="/jsp/header.jsp"></jsp:include>

<div id="debug">
query string = "<%= request.getAttribute("query_string") %>"
<div/>

<h3>SIP scenario</h3>

<div id="session_info">
SIP session ID: <%= request.getAttribute("sip_session_id") %> <br>
<div/>

<div id="sip_scenario">
  <img path="<%= request.getAttribute("sip_scenario_filename") %>"></img><br>
</div>

<jsp:include page="/jsp/footer.jsp"></jsp:include>
