<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="fattureEdit" validate="true" method="POST" theme="css_xhtml">
	<s:hidden name="action" value="%{action}"/>
	<s:textfield label="Data" onchange="javascript: aggiornaListaDDTs();" required="true" labelposition="left" name="fattura.data" size="12" cssClass="testo"/>
	<s:select labelposition="left" emptyOption="true" onchange="javascript: aggiornaListaDDTs();" label="Cliente" list="listClienti" required="true" listKey="id" listValue="rs" value="fattura.idCliente" name="fattura.idCliente" cssClass="testo"/>
	<s:textfield label="Causale" required="true" labelposition="left" name="fattura.causale" value="vendita" size="12" cssClass="testo"/>
	<s:textfield label="Sconto fine fattura" onblur="javascript: dojo.event.topic.publish('/calcolaTotaleFattura');" labelposition="left" name="fattura.sconto" size="5" cssClass="testo" required="true"/>
    <s:checkbox labelposition="left" label="Pagato:" name="fattura.pagato"/>   
	<s:textfield label="Note fattura" labelposition="left" name="fattura.noteFattura" size="40" cssClass="testo"/>    
	
	<s:url id="urlDDT" action="aggiornaDDTs">
	</s:url>
	
	<s:url id="urlTotaleFattura" action="calcolaTotaleFattura">
	</s:url>

	<div id="divListaDDTs">
		<s:div id="reportDivId" theme="ajax" href="%{urlDDT}" errorText="Errore nel prelevare la lista DDT" loadingText="Caricamento lista DDTs..." listenTopics="/aggiornaDDTs" notifyTopics="/calcolaTotaleFattura"/>
	</div>
	
	<div id="divTotaleFattura">
		<label id="totaleFatturaTitle" class="label">Totale fattura:</label>
		<s:div id="divTotaleFattura" theme="ajax" href="%{urlTotaleFattura}" errorText="Errore" listenTopics="/calcolaTotaleFattura" loadingText="Calcolo del totale..."></s:div>
	</div>
	
    <s:submit value="Salva" cssClass="button"/>
</s:form>
