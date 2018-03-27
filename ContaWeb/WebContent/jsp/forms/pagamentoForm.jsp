<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="pagamentiEdit" validate="true" method="POST" theme="css_xhtml">
	<s:hidden name="action" value="edit"/>
    <s:hidden name="pagamento.id" value="%{id}"/>
    <s:textfield labelposition="left" label="Descrizione" name="pagamento.descrizione" required="true" size="40" cssClass="testo"/>
    <s:textfield labelposition="left" label="Scadenza" name="pagamento.scadenza" required="true" size="5" cssClass="testo"/>
    <s:submit value="Aggiorna" cssClass="button"/>
</s:form>