<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="ordiniFornitoriEdit" validate="true" method="POST" theme="css_xhtml" id="form1">
	<s:hidden name="action" value="edit"/>
    <s:hidden name="ordineFornitore.id" value="%{id}"/>
    <s:select labelposition="left" label="Fornitore" list="listFornitori" required="false" 
    	listKey="id" listValue="descrizione" emptyOption="0" 
    	value="ordineFornitore.idFornitore" name="ordineFornitore.idFornitore" 
    	cssClass="testo"/>

	<s:url id="calcolaLista" action="aggiornaDettagliOrdiniFornitori" />
	<div id="calcolaListaOrdiniFornitori">
			Calcola articoli da evadere dal <s:textfield label="Data dal" required="true" labelposition="left" name="dataDal" size="12" cssClass="testo" theme="simple"/>
			al <s:textfield label="al" required="true" labelposition="left" name="dataAl" size="12" cssClass="testo" theme="simple"/>
			<s:submit type="button" value="Calcola lista" theme="ajax" formId="form1" targets="divListaDettagliOrdiniFornitori" href="%{calcolaLista}" cssClass="button"/>
	</div>
	
	<strong>Dettagli:</strong>
	<s:div id="divListaDettagliOrdiniFornitori" theme="ajax" href="%{calcolaLista}" autoStart="true" errorText="Errore nel prelevare la lista dei dettagli" loadingText="Caricamento lista dettagli..."/>
	
	<strong>Inserisci nuovo dettaglio:</strong>
	<s:url id="urlNewRequest" action="getArticoloFornitoreDetails"/>
	<div id="divInserisciDettaglioOrdine">
		<div style="float:left">
			Codice:<s:textfield id="txtIdArticolo" label="Codice Articolo" labelposition="left" size="7" cssClass="testo" theme="simple" onblur="loadArticoloFornitore(this,'%{urlNewRequest}')"/>
			<input type="hidden" id="hdnIdArticolo" disabled="disabled"/>
			<label id="lblDescrizioneArticolo"></label>
			<label id="lblPrezzoArticoloDaListino"></label>
			<label id="lblDebug"></label>
		</div>
		<div style="text-align: right">
				<label id="lblPezziArticoli">Pezzi: </label>
				<input type="text" id="txtPezziArticoli" size="4" class="testo"/>
		</div>
		<s:submit id="bttInserisciArticolo" value="Inserisci" cssClass="hiddenElements" theme="ajax" onclick="return inserisciArticolo()"/>
	</div>

	<strong>Note:</strong>
	<s:textarea name="ordineFornitore.note" value="%{ordineFornitore.note}" cols="65" rows="4" cssClass="testo"/>
	<s:submit value="Salva" cssClass="button"/>
</s:form>