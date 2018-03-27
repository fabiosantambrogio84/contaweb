<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="fornitoriEdit" validate="true" method="POST" theme="css_xhtml">
	<s:hidden name="action" value="edit"/>
    <s:hidden name="fornitore.id" value="%{id}"/>
    <s:textfield labelposition="left" label="Descrizione" name="fornitore.descrizione" required="true" size="40" cssClass="testo"/>
    <s:textfield labelposition="left" label="Indirizzo email" name="fornitore.emailAddress" size="40" cssClass="testo"/>
    <s:submit value="Aggiorna" cssClass="button"/>
</s:form>