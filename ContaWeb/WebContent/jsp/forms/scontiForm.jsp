<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="scontiEdit" validate="true" method="POST">
	<s:hidden name="action" value="edit"/>
	<s:hidden name="sconto.id" value="%{id}"/>	
	<s:select multiple="true" labelposition="left" label="Cliente" list="listClienti" listKey="id" listValue="rs" name="clienti" required="true" cssClass="testo"/>
	<s:select multiple="true" labelposition="left" label="Fornitore" list="listFornitori" listKey="id" listValue="descrizione" name="fornitori" cssClass="testo" onclick="document.getElementById('scontiEdit_articoli').disabled=true"/>
	<s:select multiple="true" labelposition="left" label="Articolo" list="listArticoli" listKey="id"  name="articoli" listValue="descCompleta" cssClass="testo" size="40" onclick="document.getElementById('scontiEdit_fornitori').disabled=true"/>	
	<s:textfield label="Sconto" labelposition="left" name="sconto.sconto" size="5" cssClass="testo" required="true" value="%{getText('format.prezzo',{sconto.sconto})}"/>
	<s:textfield label="Data Dal" labelposition="left" name="sconto.dataScontoDal" size="12" cssClass="testo"/>
	<s:textfield label="Data Al" labelposition="left" name="sconto.dataScontoAl" size="12" cssClass="testo"/>
	<s:submit value="Aggiorna" cssClass="button"/>
</s:form>