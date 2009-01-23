<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root
    version="2.0"
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:h="http://java.sun.com/jsf/html">

<jsp:directive.page
    contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

<jsp:output
    omit-xml-declaration="no"
    doctype-root-element="html"
    doctype-public="~//W3C//DTD XHTML 1.0 Transactional//EN"
    doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11-transitional.dtd"/>

<f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <title>SIP Session details</title>
        </head>
        <body>
            <h:form>
                <div id="sip_session_details">
                    <h:outputText value="SIP Session details"/><br/>
                </div>
                <br/>
                <div id="sip_scenario">
                    <h:graphicImage value="http://localhost:8080/sipana-ws/sipscenario/?sipSessionId=#{sipSession.selectedItemsCSV}" />
                </div>
            </h:form>
        </body>
    </html>
</f:view>

</jsp:root>