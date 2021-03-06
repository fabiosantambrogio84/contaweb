<%@ taglib uri="/WEB-INF/tiles.tld" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h3>Stampe</h3> 

<h4>Fatture:</h4>
<div class="formContainerNoFloat">
	<s:form validate="true" action="printListaFatture" method="post" onsubmit="return confirmStampe();">
		<s:textfield required="true" label="Data Dal" labelposition="left" name="dataDal" size="12" cssClass="testo"/>
		<s:textfield required="true" label="Data Al" labelposition="left" name="dataAl" size="12" cssClass="testo"/>
		<s:submit value="Stampa Fatture" cssClass="button"/>
		<s:submit name="action:printListaFattureNoPEC" value="Stampa Fatture no PEC" cssClass="button"/>
		<s:submit name="action:printListaFatturePEC" value="Spedizione Fatture PEC" cssClass="button"/>
		<s:submit name="action:printListaFattureMail" value="Spedizione Fatture MAIL" cssClass="button"/>
		<s:submit name="action:printRiepilogoFatture" value="Stampa Riepilogo Fatture" cssClass="button"/>
		<s:submit name="action:printRiepilogoFattureNP" value="Stampa Riepilogo Fatture Non Pagate" cssClass="button"/>
		<s:submit name="action:printRiepilogoFattureCC" value="Stampa Riepilogo Fatture per Commercianti" cssClass="button"/>		
	</s:form>
</div>

<h4>Esportazione file Conad</h4>
<div class="formContainerNoFloat">
	<s:form validate="true" action="exportConad">
		<s:textfield required="true" label="Data Dal" labelposition="left" name="daData" size="12" cssClass="testo"/>
		<s:textfield required="true" label="Data Al" labelposition="left" name="alData" size="12" cssClass="testo"/>
		<s:select labelposition="left" label="Cliente" list="listaClientiConad" listKey="id" listValue="rs" name="idCliente"/>
				
		<s:submit value="Esporta files Conad" cssClass="button"/>
	</s:form>
</div>
<div id="confermaStampeDialog"></div>