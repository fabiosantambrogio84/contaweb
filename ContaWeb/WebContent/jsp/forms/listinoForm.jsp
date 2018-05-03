<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="listinoEdit" validate="true" method="POST" theme="css_xhtml">
	<s:hidden name="action" value="%{action}"/>
	<s:hidden name="listino.id" value="%{listino.id}"/>
    <s:textfield label="Nome" labelposition="left" name="listino.descrizione" required="true" size="40" cssClass="testo"/>
    <s:select labelposition="left" label="Prezzi uguali a" list="listListini" emptyOption="true" listKey="id" listValue="descrizione" name="listino.idListinoRef" cssClass="testo"/>
    <s:submit value="Salva" cssClass="button"/>
</s:form>
