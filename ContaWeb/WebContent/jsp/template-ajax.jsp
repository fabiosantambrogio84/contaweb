<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<!-- <link href="css/template.css" rel="stylesheet" type="text/css" /> -->
<!-- <link href="css/select2.min.css" rel="stylesheet" type="text/css" /> -->

<!-- <link rel="stylesheet" href="/ContaWeb/struts/xhtml/styles.css" type="text/css"/> -->



<%-- <script type="text/javascript" language="javascript" src="js/jquery-1.11.0.min.js"></script> --%>
<%-- <script type="text/javascript" language="javascript" src="js/jquery-ui/jquery-ui-1.10.3.custom.js"></script> --%>
<!-- <link rel="stylesheet" href="js/jquery-ui/jquery-ui-1.10.3.custom.min.css" type="text/css"/> -->

<!-- <link rel="stylesheet" href="/ContaWeb/js/bootstrap/css/bootstrap.min.css" type="text/css"/> -->
<!-- <link rel="stylesheet" href="/ContaWeb/css/bootstrap-theme.css" type="text/css"/> -->
<%-- <script type="text/javascript" language="javascript" src="js/bootstrap/js/bootstrap.min.js"></script> --%>

<%-- <script type="text/javascript" language="javascript" src="js/jquery.tablesorter.js"></script> --%>
<%-- <script type="text/javascript" language="javascript" src="js/jquery.tablesorter.widgets.js"></script> --%>

<%-- <script type="text/javascript" --%>
<%--         src="js/select2.min.js"></script> --%>
        
<%-- <script src="js/scripts.js" language="javascript" type="text/javascript"></script> --%>



<title><tiles:getAsString name="title"/></title>
</head>

<body>

<div class="container body-content">
	<tiles:insertAttribute name="content"/>
</div>

</body>

</html>