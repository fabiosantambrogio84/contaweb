<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<tiles:useAttribute name="azioneLista"/>
<tiles:useAttribute name="wideContainer" ignore="true"/>
<tiles:useAttribute name="extraLeftContainer" ignore="true"/>

<h3><tiles:getAsString name="titoloForm"/></h3>

<% String formClass = "formContainer"; %>
<% if (wideContainer != null) { %>
<%	formClass = formClass + "Wide"; %>
<% } %>

<div class="<%=formClass %>">
<tiles:insertAttribute name="templateForm"/>
</div>

<% if (extraLeftContainer != null) { %>
<div class="leftFormContainer">
<tiles:insertAttribute name="templateFormLeft"/>
</div>
<% } %>

<div style="clear: both">
<% if (!((String)azioneLista).equalsIgnoreCase("")) { %>
	<s:url id="urlAzioneLista" action="${azioneLista}" includeParams="none"/>
	<s:a href="${urlAzioneLista}" cssClass="link">Torna alla lista</s:a>
<% } %>
</div>