<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>GS305_Menupage</title>
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
 
</style>

</head>
<body bgcolor="#E0D8D8">
<f:view>
	<div align="center">
	<hr />
		<h1 style="color: black">Menu Page</h1>
	<hr />
	</div>
	<div style="color: black" align="center">
			    <h:form>
		
				<br />
				<a href="fileImport.jsp">Import data</a>
				<br />
				<br />
				<a href="databaseOperations.jsp">Menu</a>
				<br />
				<br />
				<a href="export.jsp">Export</a>
				<br />
				<br />
				<h:commandButton value="Logout" action="#{main.processLogout}"
					styleClass="div1" /> 
			</h:form>
	</div>
</f:view>
</body>
</html>