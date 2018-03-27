<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>

<%@page import="java.util.Iterator"%>
<%@page import="vo.Articolo"%>
<%@page import="java.util.Collection"%>
<%@page import="vo.Fornitore"%>

<tiles:importAttribute name="list" scope="request" ignore="true"/>
<tiles:importAttribute name="fornitore" scope="request" ignore="true"/>

<div>
	<table>
		<tr>
			<th>ID Articolo</th><th>Descrizione</th><th></th>
		</tr>
		<%
		Iterator itr = ((Collection)request.getAttribute("list")).iterator();
		while (itr.hasNext()) {
			Articolo art = (Articolo)itr.next();
			Fornitore fornitore = art.getFornitore();
		
			
			%>
			<tr class="normalRow" id="tr_<%=art.getId()%>">
				
				<td align="center" class="tableCell">
					<%=art.getId() %>
				</td>
				<td class="tableCell">
					<%=art.getDescCompleta() %>
				</td>
				<td>
				<s:a id="bttInserisciArticolo" theme="ajax" cssClass="big-plus" onclick="return inserisciArticolo_daStats('<%=art.getId() %>','<%=art.getCodiceArticolo() %>','<%=art.getDescrizione() %>')">+</s:a>
				</td>
			</tr>
			<%
		} %>
	</table>
</div>

