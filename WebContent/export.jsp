<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<head>
<title>GS305_File_Export</title>
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
	<h:form>
	<center>
		<hr />
		<a href="operationChoices.jsp">Home</a>
		<a href="login.jsp">Exit</a>
		<hr />
		<h1 style="color: black">File Export</h1>
		<hr />
		<br /> <br />
			<h:panelGrid columns="2">
				<h:commandButton type="submit" value="Table List"
						action="#{databaseBean.listTableswithDefaultSchema}" style="width:130px" styleClass="div1"/>
				<h:commandButton value="Export CSV File "
					action="#{export.exportCSV}" styleClass="div1"/>
			</h:panelGrid>
			<h:panelGrid columns="4">
				<h:selectOneListbox size="10"
						styleClass="selectOneListbox_mono"
						value="#{databaseBean.tableName}"
						rendered="#{export.renderTablename}">
						<f:selectItems value="#{databaseBean.tableList}" />
				</h:selectOneListbox>
			</h:panelGrid>
							<div
					style="background-attachment: scroll; overflow: auto; height: 400px; background-repeat: repeat">
					<t:dataTable value="#{databaseBean.resultSet}" var="row"
						rendered="#{databaseBean.queryRendered}" border="1"
						cellspacing="0" cellpadding="1"
						columnClasses="columnClass1 border" headerClass="headerClass"
						footerClass="footerClass" rowClasses="rowClass2"
						styleClass="dataTableEx" width="700">
						<t:columns style="color:black" var="col"
							value="#{databaseBean.columnNamesSelected}"
							rendered="#{databaseBean.queryRendered}">
							<f:facet name="header">
								<t:outputText styleClass="outputHeader" value="#{col}" />
							</f:facet>
							<div align="center">
								<t:outputText styleClass="outputText" value="#{row[col]}" />
							</div>
						</t:columns>
					</t:dataTable>
				</div>
				</center>
		</h:form>
	</f:view>
</body>
</html>