<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<div>

<h2><tiles:getAsString name="title"/></h2>
<s:iterator value="agenti" id="agent">
	<tiles:insertTemplate template="/jsp/listOrdiniAgentiGiorno.jsp">
		<tiles:putAttribute name="list-agent" value="${agent}"/>
	</tiles:insertTemplate>
</s:iterator>
</div>