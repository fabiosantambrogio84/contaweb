<%@page import="java.math.BigDecimal"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import = "java.util.*" %>

<tiles:useAttribute name="titleList" ignore="true" />
<tiles:useAttribute name="editAction" ignore="true" />
<tiles:useAttribute name="editAutista" ignore="true" />
<tiles:useAttribute name="editDateShip" ignore="true" />
<tiles:useAttribute name="selectAutista" ignore="true" />
<tiles:useAttribute name="newActionCaption" ignore="true" />
<tiles:useAttribute name="onModal" ignore="true"/>
<tiles:useAttribute name="printAction" ignore="true"/>
<tiles:useAttribute name="pecAction" ignore="true"/>
<tiles:useAttribute name="mailAction" ignore="true"/>
<tiles:useAttribute name="pagDownAction"/>
<tiles:useAttribute name="pagUpAction"/>
<tiles:useAttribute name="storeAutista"  ignore="true"/>
<tiles:useAttribute name="selectAutista" ignore="true"/>
<tiles:useAttribute name="headersList"/>
<tiles:useAttribute name="columnsWidth"/>
<tiles:useAttribute name="columnsElements"/>
<tiles:useAttribute name="dettagliAction" ignore="true"/>
<tiles:useAttribute name="columnsTypes" ignore="true"/>

<tiles:useAttribute name="editNewWindow" ignore="true"/>
<tiles:useAttribute name="customBttLabel" ignore="true"/>
<tiles:useAttribute name="customBttAction" ignore="true"/>
<tiles:useAttribute name="customBttLabel2" ignore="true"/>
<tiles:useAttribute name="customBttAction2" ignore="true"/>
<tiles:useAttribute name="trova" ignore="true"/>
<tiles:useAttribute name="filtroData" ignore="true"/>
<tiles:useAttribute name="editButtons" ignore="true"/>
<tiles:useAttribute name="editButtonModifica" ignore="true"/>
<tiles:useAttribute name="customListBttLabel" ignore="true"/>
<tiles:useAttribute name="customListBttAction" ignore="true"/>
<tiles:useAttribute name="pagamentoAction" ignore="true"/>
<tiles:useAttribute name="pagatoAction" ignore="true"/>
<tiles:useAttribute name="importMondinAction" ignore="true"/>
<tiles:useAttribute name="filtroStato" ignore="true"/>

<!-- PAGINAZIONE -->
<% 
	Integer itemCount = (Integer)request.getAttribute("itemCount");
	Integer pageCount = (Integer)request.getAttribute("pageCount");
	Integer pagina = (Integer)request.getAttribute("pagina");
	BigDecimal totaleAcconto = (BigDecimal)request.getAttribute("totaleAcconto");
	BigDecimal totaleImporto = (BigDecimal)request.getAttribute("totaleImporto");
	BigDecimal totaleImponibile = (BigDecimal)request.getAttribute("totaleImponibile");
	BigDecimal totaleCosto = (BigDecimal)request.getAttribute("totaleCosto");
	BigDecimal totaleGuadagno = (BigDecimal)request.getAttribute("totaleGuadagno");
%>
<s:if test="messageSuccess != null">
	<div id="messageSuccessBox">
		<s:property value="messageSuccess"/>
	</div>
</s:if>


<div id="lista_container">	
	
	<div class="divPag" style="margin-left: 10px">
		<% if (pagina > 1) { %>
			<s:a href="javascript: gotoPage(%{pagina -1});" cssClass="link" accesskey="I">Indietro</s:a>
		<% } %>

		<% if (pagina > 0) { %>
			<input type="text" name="pagina" onchange="gotoPage($(this).val());" value="<s:property value="pagina"/>" size="1" style="text-align: center" cssClass="testo" /> di <s:text name="%{pageCount}"/>
		<% } %>
				
		<% if (pagina < pageCount) { %>
			<s:a href="javascript: gotoPage(%{pagina + 1});" cssClass="link" accesskey="I">Avanti</s:a>
		<% } %>
	</div>
	
	<div id="nuovoElemento">
		 <% String newWindowScript = ""; %>
		
		<% if (editNewWindow!=null) { %>
			<%	newWindowScript = "return apriFinestra('%{newAction}');"; %>
		<% } %>
		 
		<% if (editAction.equals("ordiniEdit")) { %>
		 	<s:form method="post" id="frmDeleteOrdini" theme="simple" action="${editAction}_deleteAll.do" onsubmit="return confirmDelete('ordiniClienti')">
				<s:select id="idDeleteOrdiniSelect" labelposition="left" label="Stato ordine"
					list="deleteOrdini" emptyOption="false" name="filterDeleteOrdini"/>
				<button class="link" value="Cancella">Cancella</button>
				<% if (newActionCaption != null) { %>
					<s:url id="newAction" action="${editAction}_input">	
						<s:param name="action">insert</s:param>
					</s:url>
					<s:a onclick="<%=newWindowScript %>" accesskey="N" href="%{newAction}" cssClass="link">
						<tiles:getAsString name="newActionCaption"/>
					</s:a>
				<% } %>
			</s:form>
		    <!--  
		    <s:a onclick="return confermaCancellazioneTuttiOrdini()" href="ordiniEdit_deleteAll.do" cssClass="link">Cancella</s:a>
		    -->	    	
		<% } %> 
		 
		<% if (editAction.equals("fattureEdit")) { %>
			<s:form validate="true" cssStyle="display: inline" action="exportCommercianti">
				<s:hidden name="daData" required="true" value="%{filterDataDa}" />
				<s:hidden name="alData" required="true" value="%{filterDataA}" />
    			<input type="submit" class="link" cssStyle="display: inline" value="Esporta Commerc.">  	
			</s:form>	
		<% } %>
		
		<% if (editAction.equals("fattureEdit")) { %>
			<s:form validate="true" cssStyle="display: inline" action="exportRiba">
				<s:hidden name="daData" required="true" value="%{filterDataDa}" />
				<s:hidden name="alData" required="true" value="%{filterDataA}" />
    			<input type="submit" class="link" cssStyle="display: inline" value="Esporta Riba">  	
			</s:form>	
		<% } %>
	
		<% if (customBttLabel != null) {%>
			<s:url id="cstBttAction" action="${customBttAction}"/>
			<s:a href="%{cstBttAction}" cssClass="link">
				<tiles:getAsString name="customBttLabel"/>
			</s:a>&nbsp;
		<% } %>
		
		<% if (customBttLabel2 != null) {%>
			<s:url id="cstBttAction2" action="${customBttAction2}"/>
			<s:a href="%{cstBttAction2}" cssClass="link">
				<tiles:getAsString name="customBttLabel2"/>
			</s:a>&nbsp;
		<% } %>

		<s:url id="newAction" action="${editAction}_input" includeParams="none">	
			<s:param name="action">insert</s:param>
		</s:url>	

		<% if (!editAction.equals("ordiniEdit")) { %>
            <%-- <% if (onModal != null && !onModal.equals("False")) { %>
				<s:a onclick="openModal('0');" accesskey="N" href="javascript:void(0)" cssClass="link">
					<tiles:getAsString name="newActionCaption"/>
				</s:a>
			<% } else { %>  --%>
				<% if (newActionCaption != null) { %>
					<s:a onclick="<%=newWindowScript %>" accesskey="N" href="%{newAction}" cssClass="link">
						<tiles:getAsString name="newActionCaption"/>
					</s:a>
				<% } %>
			 <%-- <% } %>  --%>
		<% } %>
	</div>
		
	<div id="titoloLista">
		<tiles:getAsString name="titleList"/>
		<label id="messageBox" style="margin-left: 20px"></label>
	</div>
	<br />
	<% if (trova != null) { %>
		<div class="left">
			<% if (filtroData != null) { %>
				<s:form id="frmSearch"  method="get" theme="simple" action="${pagDownAction}">
					Da: <s:datetimepicker name="filterDataDa" cssClass="testo" />
					A: <s:datetimepicker name="filterDataA" cssClass="testo" />
					<% if (!editAction.equals("editDDT") 
						&& !editAction.equals("fattureEdit")) { %>
					<% } %> 
					<% if (editAction.equals("pagamentoEseguitoEdit")) { %>
						Pagamento: <s:textfield name="filterPagamento" size="10" cssClass="testo"/>
						Cliente: <s:textfield name="filterKey" size="30" cssClass="testo"/>
					<% }  %>
					<% if (editAction.equals("editDDT") 
						|| editAction.equals("fattureEdit")) { %>
						<script type="text/javascript">
							window.onload = function()
							{
								var key = document.getElementById('filterKey');
								if(key) key.focus();
							}
						</script>						
						Prog./Cliente/Agente: <s:textfield name="filterKey" size="10" cssClass="testo" />
						Importo: <s:textfield name="filterImporto" size="10" cssClass="testo"/>
						<% if (editAction.equals("editDDT")) { %>
						    Autista: <s:textfield name="filterAutista" size="10" cssClass="testo"/>
						<% } %>
						Pagamento: <s:textfield name="filterPagamento" size="10" cssClass="testo"/>
						Articolo: <s:textfield name="filterArticolo" size="10" cssClass="testo"/>
						<s:select label="Stato" name="filterStato" headerKey="-1" headerValue="Tutte" list="#{'01':'Pagate', '02':'Non Pagate'}" value="filterStato" required="false" />
					<% }  %>
					<s:hidden name="pagina" value="1"></s:hidden>
					<s:submit name="invia" value="Cerca" ></s:submit>
				</s:form>
			<% } else { %>			
				<s:form  id="frmSearch"  method="get" theme="simple" action="${pagDownAction}">
					<s:hidden name="pagina" value="1"></s:hidden>
					<s:textfield labelposition="left" label="Trova" name="filterKey" size="30" cssClass="testo"/>	
					<%if (editAction.equals("clientiEdit")) {%>
						<s:select labelposition="left" label="Fornitore" onchange="$('#frmSearch').submit();"
							list="listFornitori" emptyOption="true" listKey="id" listValue="descrizione" name="filterFornitore" cssClass="testo"/>
						<s:select labelposition="left" label="Num.Listino" onchange="$('#frmSearch').submit();"
							list="listListini" emptyOption="true" listKey="id" listValue="descrizione" name="filterListino" cssClass="testo"/>	
							
						<script type="text/javascript">
							$(function()  {		
								$("select[name='filterFornitore']").select2({
									allowClear: true,
									placeholder: 'Seleziona un Fornitore'
								});
								$("select[name='filterListino']").select2({
									allowClear: true,
									placeholder: 'Seleziona un Listino'
								});
							});
						</script>
					<%} %>			 				
				</s:form>			
			<% } %>
		</div>
	<% } else { %>
		<s:form  id="frmSearch" cssClass="left" method="get" theme="xhtml" action="${pagDownAction}">
			<s:hidden name="pagina" value="1"></s:hidden>
			<% if (filtroStato != null) {%>
				<s:form method="post" id="frmSearchCliente" theme="simple" action="${pagDownAction}">
					<s:select id="idStatoOrdineSelect" labelposition="left" label="Stato ordine"
						list="statoOrdini" emptyOption="true" name="filterStatoOrdini"/>
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
			<% } %>
		</s:form>			
	<% } %>

	<span style="float: right">Trovati <s:text name="%{itemCount}"></s:text> elementi</span>

	<!-- TABELLA DEI DATI -->
	<table class="tableLista" id="tableListaId">
		<thead>
			<tr>
				<th class="hide">Num</th>
				<!-- LISTA DEI TITOLI DELLE COLONNE -->
				<%-- Exclude "Aut. Ord." --%>
				<% 
				ListIterator<?> itr = ((List<?>)headersList).listIterator();
				ListIterator<?> itrWidth = ((List<?>)columnsWidth).listIterator();
				while (itr.hasNext()) {
					Object value = itr.next();
					Object width = itrWidth.next();
					org.apache.tiles.Attribute valueAttr = (org.apache.tiles.Attribute)value;
					Object value2 = valueAttr.getValue(); 
					if(value2 instanceof String){
					    String valueS = (String)value2;
					    if(!valueS.equalsIgnoreCase("Aut. Ord.")){
				%>
							<th width="<%=width%>"><%=value %></th>
				<%
					    }
					}
				} 
				if(editDateShip != null){
				%>
					<th>Data Sped.</th>
				<% } 
				if(editAutista != null){
				%>
					<th>Autisti</th>
				<% } %>
				<th width="230px"></th>
			</tr>
		</thead>
		<tbody>
			<s:set name="sumTotal" value="0" />
			<!-- DATI TABELLA -->
			<s:url id="editURLnp" action="${editAction}_input"/>
			<%
				Object[] ElementTitle = ((List<?>)headersList).toArray();
			%>
			<s:iterator value="datiTabella">
				<%
				Object[] itrCol = ((List<?>)columnsElements).toArray();
				Object[] itrTypes = null;
				if (columnsTypes != null)
					itrTypes = ((List<?>)columnsTypes).toArray();
				%>
				<% String trClass="normalRow"; 
					int stato = 0;
				%>
				<s:if test="stato == 3">
					<% trClass="storedRow"; 
					stato = 3;
					%>
				</s:if>
				<s:elseif test="stato == 2">
					<% trClass="errorRow"; 
					stato = 2;
					%>
				</s:elseif>
				<s:elseif test="stato == 1">
					<% trClass="warningRow"; 
					stato = 1;
					%>
				</s:elseif>
				<s:elseif test="stato == 4">
					<% trClass="notActiveRow"; 
					stato = 4;
					%>
				</s:elseif>
				<s:elseif test="stato == 5">
					<% trClass="partialRow"; 
					stato = 5;
					%>
				</s:elseif>
				<% if (titleList.equals("Lista Ordini Clienti")) { 
					String trOrdiniClientClass = "ordiniClientiDefault";
				%>
					<s:if test="statoOrdine == 0">
						<% trOrdiniClientClass="ordiniClientiDaEvadere"; %>
					</s:if>
					<s:elseif test="statoOrdine == 2">
					    <% trOrdiniClientClass="ordiniClientiParzEvaso"; %>
					</s:elseif>
					<s:else>
					    <% trOrdiniClientClass="ordiniClientiEvaso"; %>
					</s:else>
					<%-- 
					<s:property value="statoOrdine"/>
					--%>
				 <%				
					trClass = trOrdiniClientClass; 						    		    	
				 } %>
	
				<tr id="tr_<s:property value="id"/>" class="<%=trClass %>">
					<%-- 		<% if(filtroData != null) { %> --%>
					<%-- 				<s:set name="sumTotal" value="#sumTotal + importo" /> --%>
					<%-- 		<% } %> --%>

					<td class="hide"><%=stato%></td>
					<% for(int i=0;i<itrCol.length;++i) {
						Object valueCol = itrCol[i];
						String type = "String";
						if (itrTypes != null)
							type = itrTypes[i].toString(); 
					%>
					
					<%-- Exclude "Aut. Ord." --%>
					<% if (!ElementTitle[i].toString().equalsIgnoreCase("Aut. Ord.")) { %>	
			        <td data-sort="<%=stato%>" class="tableCell" align="right">
			        <% if (ElementTitle[i].toString().equalsIgnoreCase("Importo")) { %>	
			        	<b><b>
			        <% } %>			
					<% if (type.equalsIgnoreCase("Boolean")) { %>
						<s:checkbox disabled="true" name="<%=valueCol.toString()%>"/>
					<% } %>
					<% if (type.equalsIgnoreCase("Prezzo")) { %>				
						<s:text name="format.prezzo">
						    <s:param name="value" value="<%=valueCol.toString()%>"/>
						</s:text>
					<% } %>
					<% if (type.equalsIgnoreCase("Qta")) { %>
						<s:text name="format.qta">
							<s:param name="value" value="<%=valueCol.toString()%>"/>					
						</s:text>
					<% } %>
					<% if (type.equalsIgnoreCase("String") || type.equalsIgnoreCase("Date") || type.equalsIgnoreCase("Integer")) { %>
						<s:property value="<%=valueCol.toString()%>"/>
					<% } %>
			        <% if (ElementTitle[i].toString().equalsIgnoreCase("Importo")) { %>	
			        </b></b>
			        <% } %>			
					</td>
			        <% } %>
					<% } %>
		
					<!-- MODIFICA DATA SPEDIZIONE -->
					<% if(editDateShip != null){ %>
						<td class="tableCell">
							<s:textfield cssClass="testo-small" name="dataSpedizione" id="dataSpedizione_${id}" />
							<div class="left hide msg-ok" id="msg_${id}">Salvataggio riuscito</div>
							<s:submit value="Salva" onclick="changeDateShip('${id}')" cssClass="btn-small right" type="button"/>
						</td>
					<% } %>
		
					<!-- MODIFICA AGENTE ORDINE - rimosso -->
					<!-- MODIFICA AUTISTA ORDINE -->
					<% if(editAutista != null){ %>
						<td class="tableCell">
						<%if (!editAction.equals("editDDT")) {%>
							<s:select onchange="changeAutista('${id}')" list="%{listAutisti}" listKey="id" listValue="nome" value="${idAutista}" name="ordine.idAutista_${id}" cssClass="testo" emptyOption="true" />
						<%} else { %>
							<s:select onchange="changeAutistaDDT('${id}')" list="%{listAutisti}" listKey="id" listValue="nome" value="${idAutista}" name="ddt.idAutista_${id}" cssClass="testo" emptyOption="true" />
						<%}%>
						</td>
					<% } %>
		
					<td class="tableCell" style="width: 250px;">
						<!-- AZIONE DETTAGLI -->
						<% if (dettagliAction != null) { %>
							<s:url id="dettagliURL" action="${dettagliAction}_input">
								<s:param name="action">dettagli</s:param>
								<s:param name="id">
									<s:property value="id"/>
								</s:param>
							</s:url>
							<s:a href="%{dettagliURL}" cssClass="link">Dettagli</s:a>	
						<% } %>
				
						<% if ((editButtons == null || editButtons.equals("true")) && editButtonModifica == null) { %>
							<s:url id="editURL" action="${editAction}_input">
								<s:param name="action">edit</s:param>
								<s:param name="id"><s:property value="id"/></s:param>
							</s:url>
							
							<% if (editNewWindow!=null) { %>
									<% newWindowScript = "return apriFinestra('"+ editAction + "_input.do','edit','%{id}');"; %>
								<% } %>
														
							<%if (onModal == null) { %>								
								<s:a onclick="<%=newWindowScript %>" href="%{editURL}" cssClass="link">Mod.</s:a>
							<% } else {%>
								<a onclick="openModal('<s:property value="id"/>')" href="javascript:void(0)" class="link">Mod.</a>
							<%}%>
						<% } %>

						<!-- AZIONE STAMPA -->
						<!-- FINE AZIONE STAMPA -->
						<!-- AZIONE PEC -->
						<% if (pecAction != null) { %>
							<s:if test="cliente.emailPec != null && !cliente.emailPec.equals('')">
								<s:url id="pecURL" action="${pecAction}">
									<s:param name="id"><s:property value="id"/></s:param>
								</s:url>
								<s:a href="%{pecURL}" cssClass="link">Invia pec</s:a>
							</s:if>
							<% if (mailAction != null) { %>
								<s:if test="!(cliente.emailPec != null && !cliente.emailPec.equals('')) && cliente.email != null && !cliente.email.equals('')">
									<s:url id="mailURL" action="${mailAction}">
										<s:param name="id"><s:property value="id"/></s:param>
									</s:url>
									<s:a href="%{mailURL}" cssClass="link">Invia mail</s:a>
								</s:if>
							<% } %>
						<% } %>					
						<!-- FINE AZIONE PEC -->
					
						<% if (editButtons == null || editButtons.equals("true")) { %>
							<% if (editAction.equals("clientiEdit")) { %>
								<s:url id="deleteURL" action="${editAction}_input" includeParams="none">
									<s:param name="action">delete</s:param>
								</s:url>
							<% } else {%>
							<s:url id="deleteURL" action="${editAction}" includeParams="none">
								<s:param name="action">delete</s:param>
							</s:url>	
							<% } %>
								
							<% if (editAction.equals("editDDT")) { %>
								<s:a theme="ajax" onclick="return confermaCancellazioneDDT('%{id}','%{deleteURL}')" href="#" cssClass="link">Canc.</s:a>
							<% }  else { %> 
								<s:a theme="ajax" onclick="return confermaCancellazione('%{id}','%{deleteURL}')" href="#" cssClass="link">Canc.</s:a>
							<% }  %> 
						<% } %>
					
						<% if (editAction.equals("listinoEdit")) { %>	
							<select  id="selCategoria_<s:property value="id"/>">
							 <option value="">Tutte le Categorie</option>
							   <s:iterator value="listCategorie">
							      <option value="<s:property value="id"/>"><s:property value="nome"/></option>
							   </s:iterator>
							</select>
							<select  id="selFornitore_<s:property value="id"/>">
							 <option value="">Tutti i Fornitori</option>
							   <s:iterator value="listFornitori">
							      <option value="<s:property value="id"/>"><s:property value="descrizione"/></option>
							   </s:iterator>
							</select>
							<b>Variaz.</b>
							<input type="text" id="aumento_<s:property value="%{id}" />" size="4" value="0">    
					    	<label class="radio-inline"><input type="radio" name="tipo_<s:property value="id"/>" name="tipo_<s:property value="id"/>" value="%" checked="checked" >%</label>
							<label class="radio-inline"><input type="radio" name="tipo_<s:property value="id"/>" value="0">&euro;</label>							
							<s:a id="variaListino_%{id}" onclick="return setUrlVariaListino('listinoEdit_varia.do?id=%{id}', '%{id}')"  href="#" cssClass="link">Aggiorna</s:a>
						<% } %>
					
						<!-- AZIONE STAMPA -->
						<% if (printAction != null) { %>
							<s:url id="printURL" action="${printAction}" includeParams="none">
								<s:param name="id"><s:property value="id"/></s:param>
							</s:url>						
							<a id="stampalistino_<s:property value="id"/>" onclick="return setUrlStampaListino('<s:property value="printURL" />', '<s:property value="id"/>')" href="#" target="_blank" class="link">Stampa</a> 
						<% } %>
					
						<!-- CUSTOM ACTION -->
						<% if (customListBttAction != null) { %>
							<s:url id="customURL" action="${customListBttAction}">
								<s:param name="id"><s:property value="id"/></s:param>
							</s:url>
						
							<s:a href="%{customURL}" cssClass="link"><%=customListBttLabel%></s:a>
						<% } %>
					
						<% if (pagamentoAction != null) { %>
							<s:if test="stato != 0">
								<s:url id="pagamentoURL" action="${pagamentoAction}">
									<s:param name="id"><s:property value="id"/></s:param>
								</s:url>
								<s:a href="%{pagamentoURL}" cssClass="link">Pagamento</s:a>
							</s:if>
						<% } %>
					
						<% if (pagatoAction != null) { %>
							<s:if test="!pagato">
								<s:url id="pagamentoURL" action="${pagatoAction}">
									<s:param name="id"><s:property value="id"/></s:param>
								</s:url>
								<s:a href="%{pagamentoURL}" cssClass="link">Pagato</s:a>
							</s:if>
						<% } %>
					</td>
				</tr>
			</s:iterator>
		
			<% if (filtroData != null && pagamentoAction == null && !editAction.equals("editNotaAccredito")) { %>
				<tr class="tableCell bold center veryBig">
					<td colspan="6">IMPORTI</td>
				</tr>

				<s:iterator value="pagamenti" id="pays">
					<tr>
						<td class="tableCell bgBlack" >&nbsp;</td>
						<td class="tableCell bgBlack" >&nbsp;</td>
						<td class="tableCell bgBlack white bold bordWhite" align="left">${pays.descrizione}</td>
						<td class="tableCell bgBlack white bold bordWhite" >${pays.amount}</td>
						<td class="tableCell bgBlack" >&nbsp;</td>
						<td class="tableCell bgBlack" >&nbsp;</td>
					</tr>
				</s:iterator>
			<% } %>
			<% if (editAction.equals("editDDT")) {%> 
			<tr>
				<td class="tableCell" colspan="5" align="left">Totali:</td>
				<td class="tableCell" align="right" id="idTotaleAcconto"><%=totaleAcconto%></td>
				<td class="tableCell" align="right" id="idTotaleImporto"><%=totaleImporto%></td>
				<td class="tableCell" align="right"><%=totaleImponibile%></td>
				<td class="tableCell" align="right"><%=totaleCosto%></td>
				<td class="tableCell" align="right"></td>
				<td class="tableCell" align="right"><%=totaleGuadagno%></td>
				<td class="tableCell" colspan="1" align="right"></td>
			</tr>
			<% } %>
		</tbody>
	</table>
</div>

<div id="update-confirm" title="Conferma" style="display: none">
	<p><span class="ui-icon ui-icon-help" style="float:left; margin:0 7px 20px 0;"></span>Vuoi aggiornare l'ordine?</p>
</div>

<script>


	function gotoPage(page)
	{
		var frm = $("#frmSearch");
		var hdn = $("input[name=pagina]", frm);
		hdn.val(page);
		frm.submit();
	}

</script>