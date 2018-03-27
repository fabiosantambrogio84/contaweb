<%@ taglib prefix="s" uri="/struts-tags" %>

<%@page import="java.util.*"%>
<%@page import="vo.*"%>

<h4 class="listaDDTsTitle">DDT da fatturare:</h4>
<s:if test="listaDDTs.size != 0">


	<% Collection listaDDTs = (Collection)request.getAttribute("listaDDTs"); %>
	<% Iterator itr = listaDDTs.iterator(); %>
	<% int i = 1; %>

	<DIV class="wwgrp" id="wwgrp_listaDDTs">
		 <DIV class="wwctrl" id="wwctrl_listaDDTs">
	<% while (itr.hasNext()) { %>
		<% DDT ddt = (DDT)itr.next(); %>
		<DIV class="listaDDTs">
			<INPUT type="checkbox" onchange="javascript: dojo.event.topic.publish('/calcolaTotaleFattura');"
					id="listaDDTs-<%=i %>" value="<%=ddt.getId() %>" name="listaDDTs"/>
			<LABEL class="checkboxLabel" for="listaDDTs-<%=i++ %>"><%=ddt.getDescrizioneBreveDDT() %></LABEL>
		</DIV>
	<% } %>
		</DIV>
	</DIV>
	
	
	<s:a href="_blank" onclick="selectionaListaDDTs(); return false;" cssClass="link">Seleziona tutti</s:a>
</s:if>
<s:else>
	<p>Nessun DDT risponde ai criteri selezionati</p>	
</s:else>