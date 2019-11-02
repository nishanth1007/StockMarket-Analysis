<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>GS305_Loginpage</title>
<style>

.div1 {
  background-color: red;
  color: white;
  padding: 12px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-family: "Helvetica", Sans-serif, serif;
}
 
</style>
</head>

<body bgcolor="#E0D8D8">
<f:view>
	<div align="center">
		<h1 style="color: black">Login Page</h1>
	</div>
	<div align="center">
		<h:form>
			<h:panelGrid columns="2">
 				<h:outputLabel value="Username:*" for="userName"
					style="font-weight:bold" />
				<h:inputText id="userName" value="#{databaseBean.userName}" style="width:141px"
					required="true" requiredMessage="Please enter your username" />

				<h:outputLabel value="Password:*" for="password"
					style="font-weight:bold" />
				<h:inputSecret id="password" value="#{databaseBean.password}" style="width:141px"
					required="true" requiredMessage="Please enter your password" />

 				<h:outputLabel for="dbHost" value="DB Host*"
					style="font-weight:bold" />
				<h:selectOneMenu id="dbHost" value="#{databaseBean.dbHost}" style="width:145px">
					<f:selectItem itemValue="131.193.209.68" itemLabel="Server 68" />	
					<f:selectItem itemValue="131.193.209.69" itemLabel="Server 69" />
					<f:selectItem itemValue="localhost" itemLabel="localhost" />
				</h:selectOneMenu>

				<h:outputLabel for="dbmsType" value="DB Type*"
					style="font-weight:bold" />
				<h:selectOneMenu id="dbmsType" value="#{databaseBean.dbmsType}" style="width:145px">
					<f:selectItem itemValue="MySQL" itemLabel="mysql" />
					<f:selectItem itemValue="DB2" itemLabel="DB2" />
					<f:selectItem itemValue="Oracle" itemLabel="Oracle" />
				</h:selectOneMenu> 

 				<h:outputLabel for="dbSchema" value="DB Schema "
					style="font-weight:bold" />
				<h:inputText id="dbSchema" required="true" value="#{databaseBean.dbSchema}" style="width:141px"/> 

				<div align="center">
					<h:commandButton value="Login" type="submit"
						action="#{main.processLogin}" styleClass="div1"/>
				</div>
			</h:panelGrid>
			<br />
			<br />
			<h:outputLabel value="* Mandatory Fields"></h:outputLabel>
			<br />
			<br />
			<h:outputText style="color:red;" value="#{main.message}" />
			<br />
			<h:message for="userName" style="color:red;"
				errorClass="errorMessage" />
			<br />
			<h:message for="password" style="color:red;"
				errorClass="errorMessage" />
			<br />
		</h:form>
	</div>
</f:view>
</body>
</html>
