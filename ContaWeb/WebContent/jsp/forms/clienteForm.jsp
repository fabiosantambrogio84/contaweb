<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="clientiEdit" validate="true" method="POST" theme="css_xhtml">
	<s:hidden name="action" value="edit"/>
	<s:hidden name="cliente.id" value="%{id}"/>
    
    <s:textfield labelposition="left" label="Ragione sociale" name="cliente.rs" required="true" size="40" cssClass="testo"/>
    <s:textfield labelposition="left" label="Ragione sociale 2" name="cliente.rs2" size="40" cssClass="testo"/>
    <s:checkbox id="clienteDittaIndividuale" labelposition="left" label="Ditta individuale" name="cliente.dittaIndividuale" onchange="enableClienti()"/> 
    <s:textfield id="clienteNome" labelposition="left" label="Nome" name="cliente.nome" size="40" cssClass="testo"/>
    <s:textfield id="clienteCognome" labelposition="left" label="Cognome" name="cliente.cognome" size="40" cssClass="testo"/>
    <s:textfield labelposition="left" label="Indirizzo" name="cliente.indirizzo" required="true" size="40" cssClass="testo"/>
    <s:textfield labelposition="left" label="Località" name="cliente.localita" required="true" size="40" cssClass="testo"/>
	<s:textfield labelposition="left" label="Cap" name="cliente.cap" required="true" size="10" cssClass="testo"/>
    <s:textfield labelposition="left" label="Prov" name="cliente.prov" required="true" size="5" cssClass="testo"/>

    <s:textfield labelposition="left" label="Partita iva" name="cliente.piva" size="20" cssClass="testo"/>

	<div class="wwgrp">
	<span class="wwlbl"><label class="label">Telefono:</label></span>
	00
	<s:textfield name="cliente.prefissoTelefono" size="2" cssClass="testo" theme="simple"/>
	-
	<s:textfield name="cliente.numeroTelefono" size="20" cssClass="testo" theme="simple"/>
	</div>

	<s:textfield labelposition="left" label="Email" name="cliente.email" size="20" cssClass="testo"/>
	<s:textfield labelposition="left" label="Email PEC" name="cliente.emailPec" size="20" cssClass="testo"/>
    <s:textfield labelposition="left" label="Codice fiscale" required="true" name="cliente.codiceFiscale" size="20" cssClass="testo"/>  
    
    <s:textfield labelposition="left" label="Descrizione banca" name="cliente.bancaDescrizione" size="30" cssClass="testo"/>
    <s:textfield labelposition="left" label="ABI" name="cliente.bancaABI" size="10" cssClass="testo"/>   
    <s:textfield labelposition="left" label="CAB fiscale" name="cliente.bancaCAB" size="10" cssClass="testo"/>
    <s:textfield labelposition="left" label="Conto corrente" name="cliente.bancaCC" size="10" cssClass="testo"/>
    
    <s:textfield labelposition="left" label="Fido" name="cliente.fido" value="%{getText('format.prezzo',{cliente.fido})}" size="7" cssClass="testo"/>
    <s:select labelposition="left" label="Pagamento" list="listPagamenti" listKey="id" listValue="descrizione" value="cliente.idPagamento" name="cliente.idPagamento" cssClass="testo"/>
    
    <s:checkbox labelposition="left" label="Nascondi prezzi:" name="cliente.nascondiPrezzi"/>   
    <s:checkbox labelposition="left" label="Blocca DDT:" name="cliente.bloccaDDT"/>
    <s:select labelposition="left" label="Estrazione CONAD" name="cliente.formatoConad" list="#{'0':'Nessuno', '1':'Conad Coop', '2':'Conad Terr'}" cssClass="testo"/>
    <s:textarea labelposition="left" label="Note" name="cliente.note" cols="30" rows="4" cssClass="testo"/>
	<s:select labelposition="left" label="Autista" value="cliente.idAutista" list="listAutisti" listKey="id" listValue="nome" name="cliente.idAutista" cssClass="testo"/>
	<s:select labelposition="left" label="Agente" value="cliente.idAgente" list="listAgenti" listKey="id" listValue="nome" name="cliente.idAgente" cssClass="testo"/>
	
	<s:checkbox id="clienteRaggruppaRiba" labelposition="left" label="Raggruppa RiBa" name="cliente.raggruppaRiba" onchange="enableClienti()"/> 
    <s:textfield id="clienteNomeRaggruppamentoRiBa" labelposition="left" label="Nome raggruppamento RiBa" name="cliente.nomeRaggruppamentoRiba" size="40" cssClass="testo"/>
	
	<s:textfield labelposition="left" label="Codice univoco SDI" name="cliente.codiceUnivocoSdi" id="clienteCodiceUnivocoSdi" size="10" cssClass="testo"/>
	
    <s:submit value="Aggiorna" cssClass="button"/>
    <s:if test="%{id}">
	    <s:submit name="action:puntiConsegnaList_input" value="Gestione punti di consegna" cssClass="button"/>
	    <s:submit name="action:listiniAssociatiList_input" value="Gestione listini associati" cssClass="button"/>
    </s:if>
    
</s:form>