<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
<head>

	<title>Search SipSessions</title>

	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/sipana/sipsession/resources/style/style.css"/>

</head>
<body onload="selectDate()">

	<f:view>
		
		<h:form>
			<fieldset>
				<legend> :: Date for search</legend>
				
				<h:outputText styleClass="alert" value="*Start and end date format - dd/MM/yyyy hh:mm."/><br/>
				
				<h:outputText value="Start Date: "/>
         		<h:inputText id="start" styleClass="date" value="#{sipSession.startTime}">
         			<f:converter  converterId="millisConverter"/>
         		</h:inputText>
         		<img id="trigger_start" src="/sipana/sipsession/resources/images/date.gif"/>
         		
         		<h:outputText value=" End Date: "/>
         		<h:inputText id="end" styleClass="date" value="#{sipSession.endTime}">
         			<f:converter  converterId="millisConverter"/>
         		</h:inputText>
         		<img id="trigger_end" src="/sipana/sipsession/resources/images/date.gif"/><br/>       		
         		
         		<h:message styleClass="alert" for="start" style="position: absolute; margin-left:60pt;"/>
         		<h:message styleClass="alert" for="end" style="position: absolute; margin-left:330pt;"/><br/>
         		
         		<h:commandButton styleClass="date" action="#{sipSession.list}" value="Search"/>
         		<h:commandButton styleClass="date" action="#{sipSession.reset}" value="Reset"/>
         		
         	</fieldset>
		</h:form>
		
		<h:form>
			<fieldset class="session">
				<legend> :: SipSessions</legend>
				
				<h:selectManyCheckbox layout="pageDirection" value="#{sipScenario.callId}">
					<f:selectItems value="#{sipSession.sipSessionList}"/>
				</h:selectManyCheckbox><br/>
				
				<h:commandButton styleClass="date" action="#{sipScenario.show}" value="Draw the Graph"/>
				
			</fieldset>
		</h:form>
		
	</f:view>
	
</body>

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