<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>GS305_File_Run</title>
<style>

a:link, a:visited {
  background-color: #f44336;
  color: white;
  padding: 12px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-family: "Helvetica", Sans-serif, serif;
}

a:hover, a:active {
  background-color: red;
}

.div1 {
  background-color: red;
  color: white;
  padding: 2px 5px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-family: "Helvetica", Sans-serif, serif;
}

body {
font-family: "Helvetica", Sans-serif, serif;
}
 
</style>

</head>
<body bgcolor="#E0D8D8">
<f:view>
	<div id="container1" align="center">
		<h:form>
		<a href="operationChoices.jsp">Home</a>
		<a href="login.jsp">Exit</a>
		</h:form>
		<hr />
		<h1 style="color: black">File Import</h1>
		<hr />
		<br /> <br />
	</div>
		<div align="center">
			<h:form>
				<h:panelGrid columns="10">
				<%-- <h:commandButton type="submit" value="Run Script"
						action="#{fileUpload.runScript}" /> --%>
					<br />
				</h:panelGrid>
				<br />
				<h:outputText value="#{fileUpload.errorMessage}"
					rendered="#{fileUpload.messageRendered}" style="color:red" />
				<br />
				<br />
	
				</h:form>
				</div>
				<div id="container3" align="center">
		<h:form enctype="multipart/form-data">
			<h:panelGrid columns="2"
				style="background-color: grey;
				border-bottom-style: solid;
				border-top-style: solid;
				border-left-style: solid;
				border-right-style: solid">

				<h:outputLabel value="File Label:" />
				<h:outputText value="#{fileUpload.fileLabel}"
					rendered="#{fileUpload.messageRendered}" />
				
				<h:outputLabel value="File Name:" />
				<h:outputText value="#{fileUpload.fileName}"
					rendered="#{fileUpload.messageRendered}" />

				<h:outputLabel value="File size" />
				<h:outputText value="#{fileUpload.fileSize}"
					rendered="#{fileUpload.messageRendered}" />


				<h:outputLabel value="Count of rows:" />
				<h:outputText value="#{fileUpload.rowCount}"
					rendered="#{fileUpload.messageRendered}" />

				<h:outputLabel value="Temp File Path:" />
				<h:outputText value="#{fileUpload.filePath}"
					rendered="#{fileUpload.messageRendered}" />
			</h:panelGrid>
		</h:form>
	</div>
				
</f:view>
</body>
</html>