<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>GS305_Stats</title>
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
  padding: 12px 20px;
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
		<h:panelGrid columns="3"> 
			<a href="databaseOperations.jsp">Back</a>
			<a href="operationChoices.jsp">Menu Page</a>
			<a href="login.jsp">Login Page</a>
		</h:panelGrid>
		</h:form>
		<hr />
		<h1>Statistical Analysis</h1>
		<hr />
		</div>
		<br /> <br />
		<div
					style="background-attachment: scroll; overflow: auto; background-repeat: repeat; background-color: lavender"
					align="center">
					<h:dataTable
						value="#{AnalysisBean.descriptiveStatsBeanList}"
						var="rowNumber"
						rendered="#{AnalysisBean.renderTabledata}" border="1"
						cellspacing="0" cellpadding="1" headerClass="headerWidth">
						<h:column>
							<f:facet name="header">
								<h:outputText value="Table" />
							</f:facet>
							<h:outputText value="#{databaseBean.tableName}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="No. of Observations" />
							</f:facet>
							<h:outputText value="#{AnalysisBean.rowCount}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Column Selected" />
							</f:facet>
							<h:outputText value="#{rowNumber.columnSelected}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Minimum Value" />
							</f:facet>
							<h:outputText value="#{rowNumber.minValue}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Maximum Value" />
							</f:facet>
							<h:outputText value="#{rowNumber.maxValue}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Mean" />
							</f:facet>
							<h:outputText value="#{rowNumber.mean}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Variance" />
							</f:facet>
							<h:outputText value="#{rowNumber.variance}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Standard Deviation" />
							</f:facet>
							<h:outputText value="#{rowNumber.std}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Q1" />
							</f:facet>
							<h:outputText value="#{rowNumber.q1}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Q3" />
							</f:facet>
							<h:outputText value="#{rowNumber.q3}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Range" />
							</f:facet>
							<h:outputText value="#{rowNumber.range}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="IQR" />
							</f:facet>
							<h:outputText value="#{rowNumber.iqr}" />
						</h:column>
					</h:dataTable>
				</div>
				<br /><br />
				<h:outputText value="Regression Equation: "
					rendered="#{AnalysisBean.renderRegressionResult}">
				</h:outputText>
				
				<h:outputText value="#{AnalysisBean.regressionEquation}"
					rendered="#{AnalysisBean.renderRegressionResult}">
				</h:outputText>
				<br /> <br />
				<h:outputText value="Regression Model"
					rendered="#{AnalysisBean.renderRegressionResult}"></h:outputText>
				<h:panelGrid columns="5"
					rendered="#{AnalysisBean.renderRegressionResult}"
					border="2">
					<h:outputText value="Predictor" />
					<h:outputText value="Co-efficient" />
					<h:outputText value="Standard Error Co-efficient" />
					<h:outputText value="T-Statistic" />
					<h:outputText value="P-Value" />
					<h:outputText value="Constant" />
					<h:outputText value="#{AnalysisBean.intercept}" />
					<h:outputText
						value="#{AnalysisBean.interceptStandardError}" />
					<h:outputText value="#{AnalysisBean.tStatistic }" />
					<h:outputText value="#{AnalysisBean.interceptPValue }" />
					<h:outputText value="#{AnalysisBean.predictorValue}" />
					<h:outputText value="#{AnalysisBean.slope}" />
					<h:outputText value="#{AnalysisBean.slopeStandardError}" />
					<h:outputText
						value="#{AnalysisBean.tStatisticPredictor }" />
					<h:outputText value="#{AnalysisBean.pValuePredictor }" />
				</h:panelGrid>
				<br /> <br />
				<h:panelGrid columns="2"
					rendered="#{AnalysisBean.renderRegressionResult}"
					border="2">
					<h:outputText value="Model Standard Error:" />
					<h:outputText value="#{AnalysisBean.standardErrorModel}" />
					<h:outputText value="R Square(Co-efficient of Determination)" />
					<h:outputText value="#{AnalysisBean.rSquare}" />
					<h:outputText
						value="R Square Adjusted(Co-efficient of Determination)" />
					<h:outputText value="#{AnalysisBean.rSquareAdjusted}" />
				</h:panelGrid>
				<br /> <br />
				<h:outputText value="Analysis of Variance"
					rendered="#{AnalysisBean.renderRegressionResult}" />
				<br />
				<h:panelGrid columns="6"
					rendered="#{AnalysisBean.renderRegressionResult}"
					border="2">
					<h:outputText value="Source" />
					<h:outputText value="Degrees of Freedom(DF)" />
					<h:outputText value="Sum of Squares" />
					<h:outputText value="Mean of Squares" />
					<h:outputText value="F-Statistic" />
					<h:outputText value="P-Value" />
					<h:outputText value="Regression" />
					<h:outputText value="#{AnalysisBean.predictorDF}" />
					<h:outputText
						value="#{AnalysisBean.regressionSumSquares}" />
					<h:outputText value="#{AnalysisBean.meanSquare }" />
					<h:outputText value="#{AnalysisBean.fValue }" />
					<h:outputText value="#{AnalysisBean.pValue}" />
					<h:outputText value="Residual Error" />
					<h:outputText value="#{AnalysisBean.residualErrorDF}" />
					<h:outputText value="#{AnalysisBean.sumSquaredErrors }" />
					<h:outputText value="#{AnalysisBean.meanSquareError }" />
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="Total" />
					<h:outputText value="#{AnalysisBean.totalDF}" />
				</h:panelGrid>
				<h:graphicImage value="#{AnalysisBean.chartPath}"
						height="600" width="600"
						rendered="#{AnalysisBean.renderChart}"  />
				<h:panelGrid columns="2"
					rendered="#{AnalysisBean.renderCorrelationResult}"
					border="2">
					<h:outputText value=""/>
					<h:outputText value="#{AnalysisBean.predictorValue}"/>
					<h:outputText value="#{AnalysisBean.responseValue}"/>
					<h:outputText value="#{AnalysisBean.corr}"/>
					<h:outputText value=""/>
					<h:outputText value="#{AnalysisBean.pValue}"/>
					<h:outputText value="Cell Contents" />
					<h:outputText value="	Pearson Correlation" />
					<h:outputText value=""/>
					<h:outputText value="	P value" />
				</h:panelGrid>					
</f:view>
</body>
</html>