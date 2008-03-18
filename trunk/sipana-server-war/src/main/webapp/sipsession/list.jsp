<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search</title>
</head>
<body>
	<f:view>
	
		<h:form>
			<h:outputText value="De: "/>
         	<h:inputText value="#{sipSession.startTime}">
         		<f:converter  converterId="millisConverter"/>
         	</h:inputText>
         	<h:outputText value=" ate: "/>
         	<h:inputText value="#{sipSession.endTime}">
         		<f:converter  converterId="millisConverter"/>
         	</h:inputText>
         	<h:commandButton action="#{sipSession.list}" value="OK"/>
		</h:form>
		
	</f:view>
</body>
</html>