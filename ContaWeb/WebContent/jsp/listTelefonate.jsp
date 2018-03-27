<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@page import="java.util.Calendar"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<div>

<h2><tiles:getAsString name="title"/></h2>

<div id="lista_container">
	<s:form  id="frmSearch" cssStyle="float: right"  method="get" theme="xhtml" action="${pagDownAction}">
		<s:hidden name="pagina" value="1"></s:hidden>
		<s:select id="idClienteSelect" labelposition="left" label="Cliente" onchange="$('#frmSearch').submit();"
			list="listClienti" emptyOption="true" listKey="id" listValue="rs" name="filterCliente" cssClass="testo"/>
	</s:form>	
</div>

<%  Calendar cal = Calendar.getInstance(Locale.ITALIAN);
	cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);		
%>
<s:iterator value="telefonate" id="tel">

		<tiles:insertTemplate template="/jsp/listTelefonateGiorno.jsp">
			<tiles:putAttribute name="title"><%=new SimpleDateFormat("EEEE",Locale.ITALIAN).format(cal.getTime())%></tiles:putAttribute>
			<tiles:putAttribute name="list" value="${tel}"/>
		</tiles:insertTemplate>

<% cal.add(Calendar.DAY_OF_YEAR,1); %>
</s:iterator>

</div>

<script type="text/javascript">
	$(function()  {		
		$("select[name='filterCliente']").select2({
			allowClear: true,
			placeholder: 'Seleziona un cliente'
		});
	});
</script>