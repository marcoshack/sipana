<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search</title>

</head>
<body onload="selectDate()">
	<f:view>
		
		<h:form>
			<fieldset>
				<legend> :: Date for search</legend>
				<h:outputText styleClass="alert" value="*Start and end date format - dd/mm/yyyy."/><br/>
				<h:outputText value="Start Date: "/>
         		<h:inputText styleClass="date" value="#{sipSession.startTime}">
         			<f:converter  converterId="millisConverter"/>
         		</h:inputText>
         		<img id="trigger_start" src="/sipana/sipsession/resources/images/date.gif"/>
         		<h:outputText value=" End Date: "/>
         		<h:inputText styleClass="date" value="#{sipSession.endTime}">
         			<f:converter  converterId="millisConverter"/>
         		</h:inputText>
         		<img id="trigger_end" src="/sipana/sipsession/resources/images/date.gif"/>
         		<h:commandButton styleClass="date" action="#{sipSession.list}" value="Search"/>
         	</fieldset>
		</h:form>
		
		<h:form>
			<fieldset class="session">
				<legend> :: SipSessions</legend>
				<h:selectManyCheckbox layout="pageDirection" value="#{sipScenario.callId}">
					<f:selectItems value="#{sipSession.sipSessionList}"/>
				</h:selectManyCheckbox>
				<h:commandButton styleClass="date" action="#{sipScenario.show}" value="Draw the Graph"/>
			</fieldset>
		</h:form>
	</f:view>
</body>

<!-- CSS -->
<link rel="stylesheet" type="text/css" href="/sipana/sipsession/resources/style/style.css"/>

<!-- JavaScript -->
<script type="text/javascript" src="/sipana/sipsession/resources/script/script.js"/></script>

<!-- Popup Calendar -->
<style type="text/css"> 
	@import url(/sipana/sipsession/resources/calendar/calendar-brown.css);
</style>
<script type="text/javascript" src="/sipana/sipsession/resources/calendar/calendar.js"></script>
<script type="text/javascript" src="/sipana/sipsession/resources/calendar/lang/calendar-en.js"></script>
<script type="text/javascript" src="/sipana/sipsession/resources/calendar/calendar-setup.js"></script>

</html>