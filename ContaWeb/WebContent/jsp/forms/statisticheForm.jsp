<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="statisticheEdit" validate="true" method="POST" theme="css_xhtml">
	<s:select labelposition="left" label="Cliente" list="listClienti" listKey="id" listValue="rs" name="idCliente" cssClass="testo"/>
	<hr/>
	<s:select labelposition="left" label="Fornitore" list="listFornitori" listKey="id" listValue="descrizione" name="idFornitore" cssClass="testo"/>
	<s:select labelposition="left" label="Articolo" list="listArticoli" listKey="id" listValue="descCompleta" name="idArticolo" cssClass="testo"/>	
	<s:textfield label="Lotto" labelposition="left" name="lotto" size="30" cssClass="testo"/>
	<hr/>
	<s:radio label="Seleziona il periodo" name="periodo" list="periodi" labelposition="left"/>
	<s:textfield label="Data dal" labelposition="left" name="dataDal" size="10" cssClass="testo"/>
	<s:textfield label="al" labelposition="left" name="dataAl" size="10" cssClass="testo"/>
	<hr/>
	<s:checkbox name="mostraDettagli" label="Mostra dettaglio:" labelposition="left"/>
	<s:checkbox name="groupArticoli" label="Raggruppa articoli dettaglio:" labelposition="left"/>
	<s:submit value="Elabora statistiche" cssClass="button"/>
	<s:submit name="action:viewChart" value="Visualizza grafico" cssClass="button"/>
</s:form>

<h3>Ricerca lotti</h3>
<s:form action="ricercaLotti" validate="true" method="POST">
<s:textfield label="lotto" labelposition="left" name="lotto" size="30" cssClass="testo"/>
<s:submit value="Ricerca" cssClass="button"/>
</s:form>