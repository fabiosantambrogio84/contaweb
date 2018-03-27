<?xml version="1.0" encoding="ISO-8859-1" ?>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link href="css/template.css" rel="stylesheet" type="text/css" />
<title>Gestione DDT</title>
</head>
<body style="padding: 0">
<applet width="650" height="620" code="applet.applet.DDTApplet" codebase="jsp/applet/" 
	archive="ContaApplets.jar,ojb.jar,commons-lang-2.1.jar">
	<s:if test="id != null">
		<param name="id" value="<%=request.getParameter("id") %>"></param>
	</s:if>
	<param name="url" value="<s:property value="appletUrl"/>"/>
</applet>

</body>
</html>

