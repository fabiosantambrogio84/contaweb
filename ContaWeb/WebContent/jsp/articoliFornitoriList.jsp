<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div>

<h2><tiles:getAsString name="title"/></h2>

<s:iterator value="articoli" id="art">
		
		<tiles:insertTemplate template="/jsp/articoliFornitori.jsp">
			<tiles:putAttribute name="fornitore" value="<%= request.getParameter("fornitore") %>"/>
			<tiles:putAttribute name="list" value="${art}"/>
		</tiles:insertTemplate>

</s:iterator>

</div>
