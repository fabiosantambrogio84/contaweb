<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link href="css/select2.min.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<link href="css/template.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="js/jquery-ui/jquery-ui-1.10.3.custom.min.css" type="text/css" />
<link rel="stylesheet" href="/ContaWeb/struts/xhtml/styles.css" type="text/css" />
<script type="text/javascript">
	// Dojo configuration
	djConfig = {
		baseRelativePath : "/ContaWeb/struts/dojo",
		isDebug : false,
		bindEncoding : "UTF-8",
		debugAtAllCosts : false
	// not needed, but allows the Venkman debugger to work with the includes
	};
</script>
<script type="text/javascript" src="/ContaWeb/struts/dojo/dojo.js"></script>
<script type="text/javascript" src="/ContaWeb/struts/simple/dojoRequire.js"></script>
<script type="text/javascript" src="/ContaWeb/struts/ajax/dojoRequire.js"></script>
<script type="text/javascript" src="/ContaWeb/struts/CommonFunctions.js"></script>
<script type="text/javascript" language="javascript" src="js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" language="javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.12.1/jquery-ui.min.js" ></script>
<script type="text/javascript" src="js/jquery-ui-1.12.1/jquery-ui.js" ></script>
<script type="text/javascript" src="js/scripts.js"></script>
<link rel="stylesheet" href="js/jquery-ui-1.12.1/jquery-ui.css" type="text/css" media="all" />
<title><tiles:getAsString name="title" /></title>
<title>Material Design Bootstrap</title>
<!-- Font Awesome -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<!-- Bootstrap core CSS -->
<!--<link href="materialDesignBootstrap/css/bootstrap.min.css" rel="stylesheet">-->
<!-- Material Design Bootstrap -->
<link href="materialDesignBootstrap/css/mdb.min.css" rel="stylesheet">
<!-- Your custom styles (optional) -->
<link href="materialDesignBootstrap/css/style.css" rel="stylesheet">

</head>
<body onload="onBodyLoad()">
	<!-- SCRIPTS -->
	<!-- JQuery -->
	<!--<script type="text/javascript" src="mdb/js/jquery-3.3.1.min.js"></script>-->
	<!-- Bootstrap tooltips -->
	<script type="text/javascript" src="materialDesignBootstrap/js/popper.min.js"></script>
	<!-- Bootstrap core JavaScript -->
	<!--<script type="text/javascript" src="materialDesignBootstrap/js/bootstrap.min.js"></script>-->
	<!-- MDB core JavaScript -->
	<script type="text/javascript" src="materialDesignBootstrap/js/mdb.min.js"></script>
	<script type="text/javascript" src="materialDesignBootstrap/js/extras.js"></script>
	
	
	<tiles:insertAttribute name="header" ignore="true" />
	<div class="container-fluid">
		<center>
			<tiles:insertAttribute name="content" />
		</center>
	</div>
	<tiles:insertAttribute name="footer" ignore="true" />		
	<script type="text/javascript" language="javascript" src="js/jquery-ui/jquery-ui-1.10.3.custom.js"></script>
	<script type="text/javascript" language="javascript" src="js/jquery.tablesorter.js"></script>
	<script type="text/javascript" language="javascript" src="js/jquery.tablesorter.widgets.js"></script>
	<script type="text/javascript" src="js/select2.min.js"></script>
	<script type="text/javascript" src="js/form2js.js"></script>
	<script type="text/javascript">
		$(function() {
			$('ul.nav li.dropdown').hover(
				function() {
					$(this).find('.dropdown-menu').stop(true, true).delay(
							10).fadeIn(500);
				},
				function() {
					$(this).find('.dropdown-menu').stop(true, true).delay(
							10).fadeOut(500);
				});			
		})
	</script>

	<!-- Modal tabindex="-1"-->
	<div class="modal fade" id="modal" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog w-big" role="document">
			<div class="modal-content"></div>
		</div>
	</div>

</body>

</html>
