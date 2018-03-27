<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@page import="java.util.Iterator"%>
<%@page import="vo.Agente"%>
<%@page import="vo.Cliente"%>
<%@page import="java.util.Collection"%>
<%@page import="vo.Ordine"%>
<%@page import="dao.Ordini"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Vector"%>
 
<%-- <tiles:useAttribute name="title" scope="request"/> --%>
<tiles:importAttribute name="list-agent" scope="request" ignore="true"/>

<div id="lista_container">

<div id="titoloLista">
<%-- <tiles:getAsString name="title"/> --%>

<label id="messageBox" style="margin-left: 20px"></label>
</div>


		
<%-- 	<% --%>
// 		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
// 		Calendar cal = Calendar.getInstance();
		 
		
// 		Iterator itr = ((Collection)request.getAttribute("list-agent")).iterator();
// 		while (itr.hasNext()) {
			
// 			String dataprecedente = "";
// 			Agente agente = (Agente)itr.next();
		
// 			Iterator itr_ord = new Ordini().getListaOrdiniperagentedata(agente.getId()).iterator();
			
// 			if(new Ordini().getListaOrdiniperagentedata(agente.getId()).size() > 0)
// 			{
<%-- 			%> --%>
<%-- 				<div class="veryBig24 bgSilver bold upper"><%=agente.getNome() %></div> --%>
				
<!-- 				<table class="tableLista" width="650px"> -->
<%-- 				<% --%>
// 				while(itr_ord.hasNext())
// 				{
					 
// 					Ordine ord = (Ordine)itr_ord.next();
					
// 					agente.setData(ord.getDataSpedizione().toString());
					
// 					Cliente cliente = ord.getCliente();
// 					if (cliente == null) {
// 						cliente = new Cliente();
// 						cliente.setRs("non specificato");
// 					}
					
// 					String dataadesso = dateFormat.format(ord.getDataSpedizione()); 
					
// 					if(!dataprecedente.equals(dataadesso))
// 					{
<%-- 						%> --%>
<!-- 						<tr class="tableCell veryBig bgBlack white"> -->
<%-- 							<td colspan="6"><%=dataadesso %> - <%=new SimpleDateFormat("EEEE",Locale.ITALIAN).format(ord.getDataSpedizione())%> --%>
<%-- 							<a style="float:right" href="printRiepilogoOrdineAgente.do?id=<%=agente.getId()%>&data=<%=ord.getDataSpedizione() %>" class="link">Stampa giornata</a> --%>
<!-- 							</td> -->
<!-- 						</tr> -->
<!-- 						<tr> -->
<!-- 							<th width="30px">Num.</th> -->
<!-- 							<th width="150px">Cliente</th> -->
<!-- 							<th width="60px">Data</th> -->
<!-- 							<th width="60px">Data Sped.</th> -->
<!-- 							<th width="150px"></th> -->
<!-- 						</tr> -->
<%-- 						<% --%>
// 					}
<%-- 					%> --%>
					
<%-- 					<tr class="normalRow" id="tr_<%=ord.getId()%>"> --%>
					
<%-- 						<td class="tableCell"><%=ord.getProgressivoCompleto()%></td> --%>
						
<%-- 						<td class="tableCell"><%=cliente.getRs()%></td> --%>
						
<%-- 						<td class="tableCell"><%=dateFormat.format(ord.getDataCreazione()) %></td> --%>
						
<%-- 						<td class="tableCell"><%=dateFormat.format(ord.getDataSpedizione()) %></td> --%>
						
<!-- 						<td class="tableCell"> -->
<%-- 						<s:url id="modifica" action="ordiniEdit_input"> --%>
<%-- 							<s:param name="action">edit</s:param> --%>
<%-- 							<s:param name="id"><%=ord.getId()%></s:param> --%>
<%-- 						</s:url> --%>
<%-- 						<a href="${modifica}" class="link">Mod.</a> --%>
<%-- 						<a href="printOrdineAgente.do?id=<%=ord.getId()%>" class="link">Stampa</a> --%>
<!-- 						</td> -->
						
<!-- 					</tr> -->
<%-- 					<%  --%>
// 					dataprecedente = dataadesso;
	
// 				}
<%-- 				%> --%>
<!-- 				</table> -->
<!-- 				<br /> -->
<!-- 				<br /> -->
<%-- 				<% --%>
// 			}
							
		} %>

</div>
