<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="ordiniEdit" id="ordiniForm" validate="true" method="POST" theme="css_xhtml">
	<s:hidden name="action" value="edit"/>
    <s:hidden id="idOrdine" name="ordine.id" value="%{id}"/>
    <s:hidden name="ordine.note" value=""/>
    <s:hidden name="typeList" value="%{typeList}"/>

	<s:select id="idClienteSelect" labelposition="left" label="Cliente" list="listClienti" required="true" listKey="id" listValue="rs"
	value="ordine.idCliente" name="ordine.idCliente" 
	 onchange="dojo.event.topic.publish('/refreshPuntiConsegna'); dojo.event.topic.publish('/calcolaStatistiche');"
	cssClass="testo"/>

	<s:url id="getPuntiConsegna" action="getPuntiConsegnaPerOrdini"/>
	<s:div theme="ajax" errorText="Errore nel caricamento. Riprovare" href="%{getPuntiConsegna}" formId="ordiniForm" listenTopics="/refreshPuntiConsegna"/>
	
	
	<s:textfield id="dataSpedizioneSelect" label="Consegna per" required="true" labelposition="left" name="dataSpedizione" size="12" cssClass="testo"/>
	
	<s:select labelposition="left" label="Autista" list="listAutisti" emptyOption="true"  required="true" listKey="id" listValue="nominativo"
		value="ordine.idAutista" name="ordine.idAutista"	cssClass="testo"/>
		
	<s:select labelposition="left" label="Agente" list="listAgenti" emptyOption="true"  required="false" listKey="id" listValue="nominativo"
		value="ordine.idAgente" name="ordine.idAgente"	cssClass="testo"/>
	
	<div class="formSubGroup">
		<div style="margin-top: 15px">
			<strong>Inserisci nuovo dettaglio:</strong>
			<s:url id="urlNewRequest" action="getArticoloDetails" includeParams="none"/>
			<div id="divInserisciDettaglioOrdine">
				<div style="float:left">
					<s:url id="loadCodArticoli" action="searchCodiciArticoli"/>
					<div class="ui-widget">
						Codice:
						
						<input type="text" style="width: 80px; height: 16px;" name="codiceArticolo" id="txtIdArticolo" class="testo" />
					</div>
					 <style>
						.ui-autocomplete-loading {
						background: white url("js/jquery-ui/images/ui-anim_basic_16x16.gif") right center no-repeat;
						}
					</style>
					
					<script>
					$(function() {
						autocompleteArticoli();
					});
					</script>

                    <!-- L'oggetto lblDescrizioneArticolo viene usato per passare la descrizione dal
                         DB, tramite Java, alla funzione javascript per l'aggiunta di un Articolo 
                         all'ordine -->
					<label id="lblDescrizioneArticolo"></label> 

                    <!-- L'oggetto lblPrezzoArticoloDaListino è stato aggiunto per passare il prezzo 
                         dell'articolo dal DB, tramite Java, alla funzione javascript per l'aggiunta 
                         di un Articolo all'ordine -->
					<label id="lblPrezzoArticoloDaListino"></label>
					<label id="lblDebug"></label>
				</div>
				<div style="text-align: right">
					<label id="lblPezziArticoli">Pezzi: </label>
					<input type="text" id="txtPezziArticoli" size="4" form="" onchange="return inserisciArticolo()" class="testo" onfocus="loadArticolo('txtIdArticolo','${urlNewRequest}')"/>
				</div>
				<s:submit id="bttInserisciArticolo" value="Inserisci" cssStyle="display: none" cssClass="hiddenElements" theme="ajax" onclick="return inserisciArticolo()"/>
			</div>
		</div>
		
		<h4>Articoli:</h4>
		<s:div id="divListaDettagliOrdini">
			<table cellpadding="0" cellspacing="0" id="tableListaOrdini">
				<tr>
					<th>Codice</th>
					<th>Descrizione</th>
					<th>Ordinati</th>
					<th>Da Evadere</th>
					<th>Prezzo</th>
					<th></th>
				</tr>
				
				<% int idx = 0; %>
				
				<s:iterator value="dettagliOrdine">
					<% String trClass = ""; %>
					
					<s:if test="pezziDaEvadere > 0 and pezziOrdinati == pezziDaEvadere">
						<% trClass = "bgGreen"; %>
					</s:if>	
					
					<s:if test="pezziDaEvadere > 0 and pezziDaEvadere < pezziOrdinati">
						<% trClass = "bgLightGreen"; %>
					</s:if>	
						
					<tr class="<%=trClass %>" id="riga<%=++idx %>">
						<td>
						    <input name="dettagliOrdine(${idArticolo}).pezziOrdinati" value="${pezziOrdinati}" id="dettagliOrdine(${idArticolo})_pezziOrdinati" type="hidden">
						    <s:property value="articolo.codiceArticolo"/>
						</td>
						<td><s:property value="articolo.descrizione"/></td>
						<td id="pezzo<%=idx %>">
						    <input id="hdnPezzo<%=idx %>" size="2" class="hiddenOrdineQta" onblur="return updateRigaTabellaOrdini('tableListaOrdini','<%=idx %>')" type="text">
						    <label id="lblPezzo<%=idx %>">
						        <s:property value="pezziOrdinati"/>
						    </label></td>
						<td><s:property value="pezziDaEvadere"/></td>
						<td id="prezziRow<%=idx %>"><s:property id="propPrezzi" value="prezziDaListini"/></td>
					    <!-- <td>
						    <a href="" onclick="return modificaRigaTabellaOrdini('tableListaOrdini','<%=idx %>')">M</a> 
						    <a href="" onclick="return cancellaRigaTabellaOrdini('tableListaOrdini','riga<%=idx %>')">C</a>
						</td> -->
						<td> 
						    <a href="" onclick="return cancellaRigaTabellaOrdini('tableListaOrdini','riga<%=idx %>')">C</a>
					        </td>
					</tr>
				</s:iterator>
			</table>
		</s:div>
	</div>

	<strong>Note:</strong>
	<s:textarea name="ordine.note" value="%{ordine.note}" cols="65" rows="4" cssClass="testo"/>
	

    <s:submit value="Salva" cssClass="button"/>
</s:form>

<script type="text/javascript">
	$(function()  {		
		$("select[name='ordine.idCliente']").select2();
	});
</script>
