<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  version="2.0">

<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<table>
	<tr>
		<th>Method</th>
		<th>Status</th>
		<th>From</th>
		<th>To</th>
		<th>Call-ID</th>
	</tr>	
<c:forEach var="sipSession" items="${sipSessionList}">
	<tr>
		<td><a href="/sipana/sipscenario.show.logic?session_id=${sipSession.id}">${sipSession.method}</a></td>
		<td>${sipSession.status}</td>
		<td>${sipSession.fromAddr}</td>
		<td>${sipSession.toAddr}</td>
		<td>${sipSession.callId}</td>
	</tr>
</c:forEach>
</table>
</body>
</html>

</jsp:root>
