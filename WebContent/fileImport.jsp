<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>GS305_File_Import</title>
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
		<div id="container2" align="center">
		<h:form enctype="multipart/form-data">
			<h:panelGrid columns="2"
				style="background-color: lavender;
				border-bottom-style: solid;
				border-top-style: solid;
				border-left-style: solid;
				border-right-style: solid">

				<h:outputLabel value="Upload file:*" />
				<t:inputFileUpload id="fileUpload" label="File to upload"
					storage="default" value="#{fileUpload.uploadedFile}"
					required="true" size="60" requiredMessage="Please select a file" />
					<h:outputLabel value="File label:*" />
				<h:inputText id="fileLabel" value="#{fileUpload.fileLabel}"
					size="60" required="true" requiredMessage="Please enter File Label" />
				<h:outputLabel value="" />
				<h:commandButton id="Import"
					action="#{fileUpload.processFileImport}" value="Submit" />

			</h:panelGrid>
			<br />
			<div align="center">
				<br />
				<h:outputLabel value="* Mandatory Field"  />
				<br />
				<h:message for="fileUpload" style="color:red;"
					errorClass="errorMessage" rendered="#{fileUpload.messageRendered}" />
				<br />
				<h:message for="fileLabel" style="color:red;"
					errorClass="errorMessage" />
				<br />
			</div>
		</h:form>
	</div>
	<br />
	
	</f:view>
</body>
</html>