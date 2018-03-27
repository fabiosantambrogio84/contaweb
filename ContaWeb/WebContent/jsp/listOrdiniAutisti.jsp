<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<div>

<h2><tiles:getAsString name="title"/></h2>
<s:iterator value="autisti" id="autista">
	<tiles:insertTemplate template="/jsp/listOrdiniAutistiGiorno.jsp">
		<tiles:putAttribute name="list-autisti" value="${autista}"/>
	</tiles:insertTemplate>
</s:iterator>
