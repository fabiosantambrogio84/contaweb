<%@ taglib uri="/WEB-INF/tiles.tld" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!-- LISTA PUNTI CONSEGNA -->
<div id="lista_container">
	<h3 id="titoloLista">Lista punti di consegna per il cliente: <s:property value="cliente.rs"/></h3>
	<table class="tableLista">
		<tr>
			<th width="200">Nome</th>
			<th width="200">Indirizzo</th>
			<th width="50">Cap</th>
			<th width="100">Località</th>
			<th width="50">Prov</th>
			<th width="50">Conad</th>
			<th width="50">Attivato</th>
			<th width="200"></th>			
		</tr>

		<!-- DATI TABELLA -->
		<s:set name="idCliente" value="cliente.id"/>
		<s:iterator value="datiTabella">
		<tr id="tr_<s:property value="id"/>">
			<td class="tableCell"><s:property value="nome"/></td>
			<td class="tableCell"><s:property value="indirizzo"/></td>
			<td class="tableCell"><s:property value="cap"/></td>
			<td class="tableCell"><s:property value="localita"/></td>
			<td class="tableCell"><s:property value="prov"/></td>
			<td class="tableCell"><s:property value="codConad"/></td>
			<s:url id="urlAttiva" action="attivaPuntoConsegna" encode="false" includeParams="none">
				<s:param name="id"><s:property value="id"/></s:param>
			</s:url>
			<td class="tableCell"><s:checkbox name="attivato" onchange="attivaPuntiConsegna('%{urlAttiva}',this)"/></td>
			<td class="tableCell">
					<a href="#" class="link" onclick="return modificaPuntoConsegna(<s:property value="id"/>)">Modifica</a>
					<s:url id="deleteURL" action="puntiConsegnaList_input">
						<s:param name="action">deletePuntoConsegna</s:param>
						<s:param name="id"><s:property value="id"/></s:param>
						<s:param name="cliente.id"><s:property value="#idCliente"/></s:param>
					</s:url>
					<s:a href="%{deleteURL}" cssClass="link">Cancella</s:a>			
			</td>											
		</tr>
		</s:iterator>
	</table>
</div>

<h3>Gestione punto di consegna:</h3>
<div class="formContainerNoFloat">
	<s:form action="puntiConsegnaList_input" validate="true" method="POST">
		<s:hidden name="action" value="storePuntoConsegna"/>
		<s:hidden name="puntoConsegna.id" value="%{puntoConsegna.id}"/>
		<s:hidden name="cliente.id" value="%{cliente.id}"/>
		<s:textfield labelposition="left" label="Nome" name="puntoConsegna.nome" required="true" size="40" cssClass="testo"/>
		<s:textfield labelposition="left" label="Indirizzo" name="puntoConsegna.indirizzo" required="true" size="40" cssClass="testo"/>
		<s:textfield labelposition="left" label="Località" name="puntoConsegna.localita" required="true" size="30" cssClass="testo"/>
		<s:textfield labelposition="left" label="Cap" name="puntoConsegna.cap" required="true" size="7" cssClass="testo"/>
		<s:textfield labelposition="left" label="Prov" name="puntoConsegna.prov" required="true" size="3" cssClass="testo"/>
		<s:textfield labelposition="left" label="Codice Conad" name="puntoConsegna.codConad" size="10" cssClass="testo"/>
		
		<div align="right">
			<input type="button" class="button" onclick="pulisciCampiPuntoConsegna()" value="Nuovo"/>
		</div>
		<s:submit value="Salva" cssClass="button"/>
	</s:form>
</div>

<s:url id="tornaURL" action="clientiEdit_input">
	<s:param name="action">edit</s:param>
	<s:param name="id"><s:property value="#idCliente"/></s:param>						
</s:url>
<s:a href="%{tornaURL}" cssClass="link">Torna indietro</s:a>