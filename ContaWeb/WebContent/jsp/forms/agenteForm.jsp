<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="agentiEdit" validate="true" method="POST" theme="css_xhtml">
	<s:hidden name="action" value="%{action}"/>
	<s:hidden name="agente.id" value="%{agente.id}"/>
    <s:textfield label="Nome" labelposition="left" name="agente.nome" required="true" size="40" cssClass="testo"/>
    <s:textfield label="Cognome" labelposition="left" name="agente.cognome" required="true" size="40" cssClass="testo"/>
    <s:textfield label="Telefono" labelposition="left" name="agente.telefono" required="false" size="40" cssClass="testo"/>	
    <s:textfield label="Email" labelposition="left" name="agente.email" required="false" size="40" cssClass="testo"/>
    <s:textfield label="Indirizzo" labelposition="left" name="agente.indirizzo" required="false" size="40" cssClass="testo"/>
    <s:submit value="Salva" cssClass="button"/>
</s:form>
