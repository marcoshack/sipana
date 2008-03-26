<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search</title>
</head>
<body>
	<f:view>
		
		<h4>Start and end date format - <strong>dd/mm/yyyy</strong></h4>
		<h:form>
			<h:outputText value="Start Date: "/>
         	<h:inputText value="#{sipSession.startTime}">
         		<f:convertDateTime pattern="dd/MM/yyyy"/>
         		<f:converter  converterId="millisConverter"/>
         	</h:inputText>
         	<h:outputText value=" End Date: "/>
         	<h:inputText value="#{sipSession.endTime}">
         		<f:converter  converterId="millisConverter"/>
         	</h:inputText>
         	<h:commandButton action="#{sipSession.list}" value="Search"/>
		</h:form>
		
		<h:form>
			<h:selectManyCheckbox layout="pageDirection" value="#{sipScenario.callId}">
				<f:selectItems value="#{sipSession.sipSessionList}"/>
			</h:selectManyCheckbox>
			<h:commandButton action="#{sipScenario.show}" value="Draw the Graph"/>
		</h:form>
	</f:view>
</body>
</html>