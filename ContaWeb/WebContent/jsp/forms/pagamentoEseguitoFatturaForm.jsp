<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="editFattura_pagamento" validate="true" method="POST" theme="css_xhtml">
	<s:hidden name="id" />
    <s:hidden name="pagamentoEseguito.id" />   
    <s:hidden name="pagamentoEseguito.idCliente" /> 
    <s:datetimepicker labelposition="left" label="Data" name="pagamentoEseguito.data" required="true" cssClass="testo"/>
    <s:textfield readonly="true" labelposition="left" label="Descrizione" name="pagamentoEseguito.descrizione" required="true" size="50" cssClass="testo"/>
    <s:textfield labelposition="left" label="Importo" name="pagamentoEseguito.importo" required="true" size="5" cssClass="testo"/>
    <s:select labelposition="left" label="Metodo Pagamento" list="listPagamenti" required="true" listKey="id" listValue="descrizione" value="pagamentoEseguito.idPagamento" name="pagamentoEseguito.idPagamento" cssClass="testo"/>
    <s:textarea labelposition="left" label="Note" name="pagamentoEseguito.notes" required="false" cols="40" rows="3" cssClass="testo"></s:textarea>
    <s:submit name="action" value="Salva" cssClass="button"/>
</s:form>