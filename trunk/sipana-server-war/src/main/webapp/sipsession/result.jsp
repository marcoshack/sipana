<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>List</title>
</head>
<body>
	<f:view>		
		<h:form>
			<h:selectManyCheckbox layout="pageDirection" value="#{sipSession.sessionId}">
				<f:selectItems value="#{sipSession.sipSessionList}"/>
			</h:selectManyCheckbox>
			<h:commandButton action="#{sipSession.show}" value="OK"/>
		</h:form>
	</f:view>
</body>
</html>