<%@ taglib prefix="s" uri="/struts-tags" %>

<!-- LISTA STATISTICHE -->
<div id="lista_container">
	
	<div id="riepilogoBox">
		<table>
			<tr>
				<td>Cliente:</td>
				<td><s:if test="cliente != null">
				<s:property value="cliente.rs"/></s:if>
				<s:else>TUTTI</s:else></td>
			</tr>
			<tr>
				<td>Fornitore:</td>
				<td><s:if test="fornitore != null">
				<s:property value="fornitore.descrizione"/></s:if>
				<s:else>TUTTI</s:else></td>
			</tr>
			<tr>
				<td>Articolo:</td>
				<td><s:if test="articolo != null">
				<s:property value="articolo.descrizione"/></s:if>
				<s:else>TUTTI</s:else></td>
			</tr>
			<tr>
				<td>Lotto:</td>
				<td><s:if test="lotto != ''"><s:property value ="lotto"/></s:if><s:else>TUTTI</s:else></td>
			</tr>
			<tr>
				<td>Periodo:</td>
				<td><s:if test="periodo == 0">anno corrente</s:if>
					<s:if test="periodo == 1">mese corrente</s:if>
					<s:if test="periodo == 2">dal <s:property value="dataDal"/> al <s:property value="dataAl"/></s:if></td>
			</tr>
		</table>
	</div>
	
	<h3 id="titoloLista">Statistiche dettagli:</h3>
	
	<s:if test="totale != null">

		<h4><i>Totale venduto:
			<s:text name="format.prezzo">
				<s:param name="value" value="%{totale}"/>
			</s:text> &#8364;</i></h4>
	
		<h4><i>Totale qta venduta:
			<s:text name="format.qta">
				<s:param name="value" value="%{qta}"/>
			</s:text>.</i></h4>
	</s:if>
	<s:else>
			<h4><i>Nessuna statistica di vendita</i></h4>
	</s:else>
	
	<!-- <h4><i>Media vendita per consegna:
		<s:text name="format.prezzo">
			<s:param name="value" value="%{media}"/>
		</s:text> &#8364;</i></h4> -->
	<s:if test="mostraDettagli && totale != null">
		<s:if test="groupArticoli">
		<table class="tableLista">
			<tr>
				<th width="70">Codice</th>
				<th width="200">Descrizione</th>
				<th width="50">N. righe</th>
				<th width="50">Tot. qta</th>
				<th width="100">Totale venduto</th>
				<th width="120">Totale medio x riga</th>			
			</tr>
	
			<!-- DATI TABELLA -->
			<s:iterator value="datiTabella">
			<tr>
				<td class="tableCell"><s:property value="top[0]"/></td>
				<td class="tableCell"><s:property value="top[1]"/></td>
				<td class="tableCell"><s:property value="top[2]"/></td>
				<td class="tableCell">
					<s:text name="format.qta">
						<s:param name="value" value="%{top[3]}"/>
					</s:text>
				</td>
				<td class="tableCell">
					<s:text name="format.prezzo">
						<s:param name="value" value="%{top[4]}"/>
					</s:text>
				</td>
				<td class="tableCell">
					<s:text name="format.prezzo">
						<s:param name="value" value="%{top[5]}"/>
					</s:text>
				</td>
			</tr>
			</s:iterator>
		</table>
		</s:if>
		<s:else>
			<h4><i>Sono state trovate <s:property value="count"/> righe di dettaglio.</i></h4>
		<table class="tableLista">
			<tr>
				<th width="40">DDT</th>
				<th width="70">Codice</th>
				<th width="200">Descrizione</th>
				<th width="50">Q.tà</th>
				<th width="50">Prezzo</th>
				<th width="50">Totale</th>
				<th width="200">Lotto</th>			
			</tr>
	
			<!-- DATI TABELLA -->
			<s:iterator value="datiTabella">
			<tr>
				<td class="tableCell"><s:property value="ddt.numeroProgressivo"/></td>
				<td class="tableCell"><s:property value="codiceArticolo"/></td>
				<td class="tableCell"><s:property value="descrizioneArticolo"/></td>
				<td class="tableCell">
					<s:text name="format.qta">
						<s:param name="value" value="%{qta}"/>
					</s:text>
				</td>
				<td class="tableCell">
					<s:text name="format.prezzo">
						<s:param name="value" value="%{prezzo}"/>
					</s:text>
				</td>
				<td class="tableCell">
					<s:text name="format.prezzo">
						<s:param name="value" value="%{totale}"/>
					</s:text>
				</td>
				<td class="tableCell"><s:property value="lotto"/></td>
			</tr>
			</s:iterator>
		</table>
		</s:else>
	</s:if>
</div>
<br/>

<s:url id="tornaURL" action="statisticheEdit_input"/>
<s:a href="%{tornaURL}" cssClass="link">Torna indietro</s:a>