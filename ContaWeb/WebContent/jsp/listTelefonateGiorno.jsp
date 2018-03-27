<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>

<%@page import="java.util.Iterator"%>
<%@page import="vo.Cliente"%>
<%@page import="java.util.Collection"%>
<%@page import="vo.Telefonata"%>

<tiles:useAttribute name="title" scope="request"/>
<tiles:useAttribute name="showTodayDate" scope="request" ignore="true"/>
<tiles:importAttribute name="list" scope="request" ignore="true"/>

<% if ((request.getAttribute("typeList").equals("giorno")) ||
		(Collection)request.getAttribute("list") != null && 
		((Collection)request.getAttribute("list")).size() > 0) { %>

<div id="lista_container">

<% if (request.getAttribute("typeList").equals("giorno")) { %>

<div id="nuovoElemento">

	<s:form  id="frmSearch" cssStyle="float: left"  method="get" theme="xhtml" action="${pagDownAction}">
		<s:hidden name="pagina" value="1"></s:hidden>
		<s:select id="idClienteSelect" labelposition="left" label="Cliente" onchange="$('#frmSearch').submit();"
			list="listClienti" emptyOption="true" listKey="id" listValue="rs" name="filterCliente" cssClass="testo"/>
	</s:form>
	<script type="text/javascript">
		$(function()  {		
			$("select[name='filterCliente']").select2({
				allowClear: true,
				placeholder: 'Seleziona un cliente'
			});
		});
	</script>
	<s:url id="newAction" action="telefonataEdit_input" includeParams="none">
		<s:param name="action">insert</s:param>
		<s:param name="typeList"><s:property value="typeList"/></s:param>
	</s:url>

	<s:a accesskey="N" href="%{newAction}" cssClass="link">
		Nuova telefonata
	</s:a>
</div>
<% } %>

<div id="titoloLista"><tiles:getAsString name="title"/>
<s:if test="${showTodayDate} == true">
- <%= new SimpleDateFormat("EEEEE",Locale.ITALIAN).format(new Date())%> 
</s:if>
<label id="messageBox" style="margin-left: 20px"></label>
</div>

<table class="tableLista" width="650px">
		<tr>
			<th width="10px"></th>
			<th>Cliente</th>
			<th>Autista</th>
			<th width="20px">Ora</th>
			<th width="20px">Prog.</th>
			<th width="80px">Telefono</th>
			<th width="150px">Note</th>
			<th width="150px"></th>
		</tr>
	<%
		int prog = 0;
		String currentAutistaRS = "";
		String backgroundColor = "lightgray";
		Iterator itr = ((Collection)request.getAttribute("list")).iterator();
		while (itr.hasNext()) {
			prog++;
			Telefonata tel = (Telefonata)itr.next();
			Cliente cliente = tel.getCliente();
			if (cliente == null) {
				cliente = new Cliente();
				cliente.setRs("non specificato");
			}
			String autistaRS = "";
			if(tel.getAutista() != null)
				autistaRS = tel.getAutista().getNome();
			if(currentAutistaRS != autistaRS) {
				prog = 1;
				backgroundColor = backgroundColor.equals("lightgray") ? "white" : "lightgray";
				currentAutistaRS = autistaRS;
			}
			
			String cssClass = "";
			if (tel.getEseguita()) cssClass = "eseguita";
			
	%>
		<tr class="normalRow" id="tr_<%=tel.getId()%>" style="background-color: <%=backgroundColor %>">
		
			<td class="tableCell"><input onchange="storeTelefonataEseguita(this)" <% if (tel.getEseguita()) { %> checked="true" <% } %> value="<%=tel.getId() %>" type="checkbox"/></td>
		
			<td class="tableCell <%=cssClass%>"><%=cliente.getRs() %>
			<% if (tel.getPuntoConsegna() != null) { %>
				<br/><small><%=tel.getPuntoConsegna().getLocalita() %></small>
			<% } %>
			</td>
			
			<td class="tableCell <%=cssClass%>">
				<s:select onchange="changeAutistaTelefonata(this)" list="%{listAutisti}" listKey="id" listValue="nome"					
 					cssClass="testo" emptyOption="true">
 					<s:param name="value"><%= tel.getIdAutista() %></s:param>
 					<s:param name="name">tel_<%= tel.getId() %></s:param>
				</s:select>	
			</td> 
			
			<td class="tableCell <%=cssClass%>">
				<%=tel.getOrario() %>
			</td>
			
			<td class="tableCell <%=cssClass%>">
				<%=String.format("%02d", prog) %>
			</td>
			
			<% if ((tel.getNumeroTelefono1() != null) && (!tel.getNumeroTelefono1().equalsIgnoreCase(""))) { %>
			<td class="tableCell <%=cssClass%>">
				<ul class="listaTelefoni">
					<li class="rigaListaTelefoni"><a class="linkSkype" href="skype:00<%=tel.getTelefono1Completo() %>?call">
					<%=tel.getNumeroTelefono1() %></a></li>
					
					<% if ((tel.getNumeroTelefono2() != null) && (!tel.getNumeroTelefono2().equalsIgnoreCase(""))) { %>
					<li class="rigaListaTelefoni"><a class="linkSkype" href="skype:00<%=tel.getTelefono2Completo() %>?call">
					<%=tel.getNumeroTelefono2() %></a></li>
					<% } %>
					
					<% if ((tel.getNumeroTelefono3() != null) && (!tel.getNumeroTelefono3().equalsIgnoreCase(""))) { %>
					<li class="rigaListaTelefoni"><a class="linkSkype" href="skype:00<%=tel.getTelefono3Completo() %>?call">
					<%=tel.getNumeroTelefono3() %></a></li>
					<% } %>
				</ul>
			</td>
			<% } else { %>
			<td class="tableCell"></td>
			<% } %>
			
			<td class="tableCell <%=cssClass%>"><%=tel.getNote() %></td>
			
			<% if (cliente.getId() != null) { %>
			<s:url id="ordineURL" action="ordiniEdit_input">
				<s:param name="idCliente"><%=cliente.getId()%></s:param>
				<% if (tel.getIdPuntoConsegna() != null) {%>
					<s:param name="idPuntoConsegna"><%=tel.getIdPuntoConsegna()%></s:param>
				<% } %>
				<% if (tel.getIdAutista() != null) {%>
					<s:param name="idAutista"><%=tel.getIdAutista()%></s:param>
				<% } %>
				<s:param name="action">insert</s:param>
				<s:param name="typeList"><s:property value="typeList"/></s:param>
			</s:url>
			<% } %>
			
			<s:url id="cancella" action="telefonataEdit">
				<s:param name="id"><%=tel.getId()%></s:param>
				<s:param name="action">delete</s:param>
			</s:url>
			
			
			
			<s:url id="modifica" action="telefonataEdit_input">
				<s:param name="id"><%=tel.getId()%></s:param>
				<s:param name="action">edit</s:param>
				<s:param name="typeList"><s:property value="typeList"/></s:param>
			</s:url>

			<td class="tableCell">
				<% if (cliente.getId() != null) { %>
					<a href="${ordineURL}" class="link">N. ordine</a>
				<% } %>
				
				<a href="${modifica}" class="link">Mod.</a>
				
				<a dojoType="struts:BindAnchor" class="link" href="#" showError="true" 
				onclick="return confermaCancellazione(<%=tel.getId() %>,'/ContaWeb/telefonataEdit.do?action=delete')">
                Canc.</a>
			</td>
		</tr>
		<% } %>
</table>
</div>
<% } %>