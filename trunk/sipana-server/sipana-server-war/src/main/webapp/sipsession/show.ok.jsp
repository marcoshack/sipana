<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  version="2.0">
<jsp:directive.page import="org.sipana.protocol.sip.SIPRequest"/>
<jsp:directive.page import="org.sipana.protocol.sip.SIPMessage"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<body>
SIP message list size: ${sipMessages.size}
</body>
</html>

</jsp:root>