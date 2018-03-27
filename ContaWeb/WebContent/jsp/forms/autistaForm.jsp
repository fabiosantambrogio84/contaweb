<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="autistiEdit" validate="true" method="POST" theme="css_xhtml">
	<s:hidden name="action" value="%{action}"/>
	<s:hidden name="autista.id" value="%{autista.id}"/>
    <s:textfield label="Nome" labelposition="left" name="autista.nome" required="true" size="30" cssClass="testo"/>
    <s:textfield label="Cognome" labelposition="left" name="autista.cognome" required="false" size="30" cssClass="testo"/>
    <s:textfield label="Telefono" labelposition="left" name="autista.telefono" required="false" size="30" cssClass="testo"/>	
    <s:submit value="Salva" cssClass="button"/>
</s:form>
