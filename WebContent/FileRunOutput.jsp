<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>GS305_File_Run_Output</title>
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
				<div
					style="background-attachment: scroll; overflow: auto; height: 400px; background-repeat: repeat">
					<h:dataTable value="#{databaseBean.tableList}" var="row"
						rendered="#{databaseBean.tableListRendered}" border="1">
						<h:column>    				    				
     						<h:outputText value="#{row}"/>
   						</h:column>
					</h:dataTable>
					<br />
					<h:outputText value="#{fileUpload.errorMessage}"
					rendered="#{fileUpload.messageRendered}" style="color:red" />
					<br />
					<h:dataTable value="#{databaseBean.columnList}" var="row"
						rendered="#{databaseBean.columnListRendered}" border="1">
						<h:column>    				    				
     						<h:outputText value="#{row}"/>
   						</h:column>
					</h:dataTable>
					<br/>
					
					
				<div
					style="background-attachment: scroll; overflow: auto; height: 400px; background-repeat: repeat">
					<t:dataTable value="#{fileUpload.rs1}" var="row"
						rendered="#{fileUpload.queryRendered}" border="1"
						cellspacing="0" cellpadding="1"
						columnClasses="columnClass1 border" headerClass="headerClass"
						footerClass="footerClass" rowClasses="rowClass2"
						styleClass="dataTableEx" width="700">
						<t:columns style="color:blue" var="col"
							value="#{fileUpload.loadColumnsList}"
							rendered="#{fileUpload.queryRendered}">
							<f:facet name="header">
								<t:outputText styleClass="outputHeader" value="#{col}" />
							</f:facet>
							<div align="center">
								<t:outputText styleClass="outputText" value="#{row[col]}" />
							</div>
						</t:columns>
					</t:dataTable>
					</div>
					</div>
				</div>
</f:view>
</body>
</html>