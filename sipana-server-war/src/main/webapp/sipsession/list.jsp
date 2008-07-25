<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
<head>
	<title>Sipana</title>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/sipana/sipsession/resources/style/style.css"/>
</head>
<body onload="selectDate()">

	<f:view>
		
		<h:form style="width: 100%">
			<fieldset class="date">
				<legend><h:outputText value="#{sipsession_msgs.ttl_search}" /></legend>
				
				<h:outputText styleClass="alert" value="#{sipsession_msgs.fld_date_format}"/><br/>
				
				<!-- START TIME -->
				<h:outputText value="#{sipsession_msgs.fld_start_date}"/>
         		<h:inputText id="start" value="#{sipSession.startTime}">
         			<f:converter  converterId="millisConverter"/>
         		</h:inputText>
         		<img id="trigger_start" src="/sipana/sipsession/resources/images/date.gif"/>

				<!-- END TIME -->         		
         		<h:outputText value="#{sipsession_msgs.fld_end_date}"/>
         		<h:inputText id="end" value="#{sipSession.endTime}">
         			<f:converter  converterId="millisConverter"/>
         		</h:inputText>
         		<img id="trigger_end" src="/sipana/sipsession/resources/images/date.gif"/>
         		<br>
         		
         		<!-- METHOD -->
         		<h:outputText value="#{sipsession_msgs.fld_method}"/>
         		<h:inputText id="method" value="#{sipSession.method}"/>
         		<br>
         		
         		<!-- FROM USER -->
         		<h:outputText value="#{sipsession_msgs.fld_from_user}"/>
         		<h:inputText id="from" value="#{sipSession.fromUser}"/>
         		
         		<!-- TO USER -->
         		<h:outputText value="#{sipsession_msgs.fld_to_user}"/>
         		<h:inputText id="to" value="#{sipSession.toUser}"/>
         		<br>
         		
         		<!-- CALL-ID -->
         		<h:outputText value="#{sipsession_msgs.fld_callid}" />
         		<h:inputText id="callid" value="#{sipSession.callId}" size="150"/>
         		<br>

         		<h:message styleClass="alert alert1" for="start"/>
         		<h:message styleClass="alert alert2" for="end"/><br/>
         		
         		<h:commandButton action="#{sipSession.list}" value="#{sipsession_msgs.btn_search}"/>
         		<h:commandButton action="#{sipSession.reset}" value="#{sipsession_msgs.btn_reset}"/>
         		
         	</fieldset>
		</h:form>
		
		<h:form style="width: 100%">
			<fieldset>
			
				<legend><h:outputText value="#{sipsession_msgs.ttl_result}"/></legend>
				
				<h:outputFormat value="#{sipsession_msgs.msg_search_result}">
					<f:param value="#{sipSession.listSize}" />
				</h:outputFormat>
				
				<h:selectManyCheckbox styleClass="list" layout="pageDirection" value="#{sipScenario.callId}">
					<f:selectItems value="#{sipSession.sipSessionList}"/>
				</h:selectManyCheckbox>
				<br/>
				
				<h:commandButton styleClass="btn" action="#{sipScenario.show}" value="#{sipsession_msgs.btn_draw_graph}" /><br>
				<h:commandButton styleClass="btn" action="#{sipSession.details}" value="#{sipsession_msgs.btn_details}" />
				
			</fieldset>
		</h:form>
		
	</f:view>
	
</body>

	<!-- JavaScript -->
	<script type="text/javascript" src="/sipana/sipsession/resources/script/script.js"/></script>

	<!-- Pop-up Calendar -->
	<style type="text/css"> 
		@import url(/sipana/sipsession/resources/calendar/calendar-brown.css);
	</style>
	<script type="text/javascript" src="/sipana/sipsession/resources/calendar/calendar.js"></script>
	<script type="text/javascript" src="/sipana/sipsession/resources/calendar/lang/calendar-en.js"></script>
	<script type="text/javascript" src="/sipana/sipsession/resources/calendar/calendar-setup.js"></script>

</html>