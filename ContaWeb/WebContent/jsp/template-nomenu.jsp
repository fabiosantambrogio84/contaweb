<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link href="css/template.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="/ContaWeb/struts/xhtml/styles.css" type="text/css"/>
<script type="text/javascript">
    // Dojo configuration
    djConfig = {
        baseRelativePath: "/ContaWeb/struts/dojo",
        isDebug: false,
        bindEncoding: "UTF-8",
        debugAtAllCosts: false // not needed, but allows the Venkman debugger to work with the includes
    };
</script>
<script type="text/javascript"
        src="/ContaWeb/struts/dojo/dojo.js"></script>
<script type="text/javascript"
        src="/ContaWeb/struts/simple/dojoRequire.js"></script>
<script type="text/javascript"
        src="/ContaWeb/struts/ajax/dojoRequire.js"></script>
<script type="text/javascript"
        src="/ContaWeb/struts/CommonFunctions.js"></script>

<script src="js/scripts.js" language="javascript" type="text/javascript"></script>
<title><tiles:getAsString name="title"/></title>
</head>

<body>

<div>
<tiles:insertAttribute name="content"/>
</div>

</body>

</html>