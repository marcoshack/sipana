<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  version="2.0">

<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<table>

<c:forEach var="sipSession" items="${sipSessions}">
	<tr>
		<td><a href="/sipana/sipsession.show.logic?id=${sipSession.id}">${sipSession.method}</a></td>
		<td>${sipSession.callId}</td>
	</tr>
</c:forEach>

</table>
</body>
</html>

</jsp:root>