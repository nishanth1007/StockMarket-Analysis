<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>GS305_DBops</title>
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
	<hr />
	<h3 align="center">Database Operations</h3>
	<hr />
		<h:form>
			<a href="operationChoices.jsp">Home</a>
			<a href="login.jsp" >Exit</a>
		</h:form>
		<hr />
		<br /> <br />
	</div>
		<div align="center">
			<h:form>
				<h:panelGrid columns="4">
					<h:commandButton type="submit" value="Table List" 
						action="#{databaseBean.listTableswithDefaultSchema}" style="width:150px;height:25px" styleClass="div1"/>
					<h:commandButton type="submit" value="Column List"
						action="#{databaseBean.listColumns}" style="width:150px;height:25px" styleClass="div1"/>
					<h:commandButton type="submit" value="Display Table"
						action="#{databaseBean.loadTableContents}"  style="width:150px;height:25px" styleClass="div1"/>
					<h:commandButton type="submit" value="Select"
						action="#{databaseBean.loadSelectedColumns}" style="width:150px;height:25px" styleClass="div1"/>
				</h:panelGrid>
				<h:panelGrid columns="4">
					<h:commandButton type="submit" value="Create Table"
					    action="#{databaseBean.createTable}" style="width:150px;height:25px" styleClass="div1"/>
					<h:commandButton type="submit" value="Drop Table"
					    action="#{databaseBean.droptable}" style="width:150px;height:25px" styleClass="div1"/>
					<h:commandButton type="submit" value="Compute Return"
						action="#{databaseBean.computeReturnMethod}" style="width:150px;height:25px" styleClass="div1"/>
					<h:commandButton type="submit" value="Get Tickers"
						action="#{databaseBean.distinctTickers}" style="width:150px;height:25px" styleClass="div1" />	
				</h:panelGrid>		
					<%-- <h:commandButton type="submit" value="Insert in Table"
					    action="#{databaseBean.insertDB}" /> --%>
					 
				<h:panelGrid columns="4">	
					<h:commandButton type="submit" value="Descriptive Stats"
						action="#{AnalysisBean.generateDescriptiveStatistics}"  style="width:150px;height:25px"
						styleClass="div1"
						disabled="#{AnalysisBean.renderReport}" />
					<h:commandButton
						value="Load Variables "
						action="#{AnalysisBean.displayColumnsforRegression}"
						styleClass="div1"  style="width:150px;height:25px"/>
					<h:commandButton type="submit" value="Correlate"
						action="#{AnalysisBean.correlateVariables}" style="width:150px;height:25px" styleClass="div1"/>
					<h:commandButton value="Regression Analysis"
						action="#{AnalysisBean.generateRegressionAnalysis}"
						styleClass="div1"
						disabled="#{AnalysisBean.renderRegressionButton}"  style="width:150px;height:25px"/>
				</h:panelGrid>
				<h:panelGrid columns="3">
					<h:commandButton type="submit" value="Histogram"
						action="#{AnalysisBean.loadSelectedHistogram}"  style="width:150px;height:25px" styleClass="div1"/>
					<h:commandButton type="submit" value="Create Scatterplot"
						action="#{AnalysisBean.createScatterplotGraph}" style="width:150px;height:25px" styleClass="div1"/>
					<h:commandButton type="submit" value="Reset All"
						action="#{databaseBean.resetButton}" style="width:150px;height:25px" styleClass="div1"/>
					<br />
				</h:panelGrid>
				<br />
				<h:outputText value="#{databaseBean.message}"
					rendered="#{databaseBean.messageRendered}" style="color:red" />
				<br />
				<%-- <h:outputText value="#{AnalysisBean.message}"
					rendered="#{AnalysisBean.renderMessage}"
					style="color:red" /> --%>
			
				<br />
				<br />
				<br />
				<h:panelGrid columns="80">
				
					<h:selectOneListbox size="10"
						styleClass="selectOneListbox_mono"
						value="#{databaseBean.tableName}"
						rendered="#{databaseBean.tableListRendered}">
						<f:selectItems value="#{databaseBean.tableList}" />
					</h:selectOneListbox>

					<h:selectManyListbox  size="10"
						styleClass="selectManyListbox"
						value="#{databaseBean.columnNamesSelected}"
						rendered="#{databaseBean.columnListRendered}">
						<f:selectItems value="#{databaseBean.columnList}" />
					</h:selectManyListbox>
					<h:selectOneListbox id="predictor"
						style="background-color: lavender"
						value="#{AnalysisBean.responseValue}"
						rendered="#{AnalysisBean.renderRegressionColumn}"
						size="5">
						<f:selectItem itemValue="0" itemLabel=" Response  or  Y variable" />
						<f:selectItems value="#{AnalysisBean.numericData}" />
					</h:selectOneListbox>
					<h:selectOneListbox id="response"
						style="background-color: lavender"
						value="#{AnalysisBean.predictorValue}"
						rendered="#{AnalysisBean.renderRegressionColumn}"
						size="5">
						<f:selectItem itemValue="0" itemLabel="Predictor or X variable" />
						<f:selectItems value="#{AnalysisBean.numericData}" />
					</h:selectOneListbox>
					

				</h:panelGrid>
				<br />
				
				
				<h:outputText value="Ticker:" />
						<h:selectOneListbox id="ticker" value="#{databaseBean.ticker}">
						
						<f:selectItem itemValue="" itemLabel="Select Ticker"/>
						<f:selectItems value="#{databaseBean.tickers}"/>
						
						</h:selectOneListbox>
						<h:commandButton type="submit" value="Filter"
								action="#{databaseBean.filterTicker}" styleClass="button" />
						<h:outputText value="Date Filter (mm/dd/yyyy):" />
						<h:outputText value="From:" />
						<h:inputText id="dateFrom" value="#{databaseBean.dateFrom}"/>
						<h:outputText value="to:" />
						<h:inputText id="dateTo" value="#{databaseBean.dateTo}"/>
						<h:commandButton value="Filter"
								action="#{databaseBean.filterDate}" styleClass="button" />
						<h:selectOneListbox id="sortDate" value="#{databaseBean.sort}">
						<f:selectItem itemValue="ASC" itemLabel="Ascending" />
						<f:selectItem itemValue="DESC" itemLabel="Descending"/>
						</h:selectOneListbox>
						<h:commandButton value="Sort"
								action="#{databaseBean.sortDate}" styleClass="button" />
						<hr /> 
				
				<hr />
				<h:outputText value="Query : "
					rendered="#{databaseBean.queryRendered}" />
				<h:outputText value="#{databaseBean.sqlQuery}"
					rendered="#{databaseBean.queryRendered}" />
				<br />
				<h:outputText value="Rows : "
					rendered="#{databaseBean.queryRendered}" />
				<h:outputText value="#{databaseBean.nr}"
					rendered="#{databaseBean.queryRendered}" />
				<br />
				<h:outputText value="Columns : "
					rendered="#{databaseBean.queryRendered}" />
				<h:outputText value="#{databaseBean.numberOfColumns}"
					rendered="#{databaseBean.queryRendered}" />
				<hr />
				
			
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
				</h:form>
				</div>
</f:view>
</body>
</html>