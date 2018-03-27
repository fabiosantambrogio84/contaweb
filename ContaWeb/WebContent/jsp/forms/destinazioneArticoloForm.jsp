<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="destinazioniArticoliEdit" validate="true" method="POST" theme="css_xhtml">
	<s:hidden name="action" value="%{action}"/>
	<s:hidden name="categoriaArticolo.id" value="%{categoriaArticolo.id}"/>
    <s:textfield label="Nome" labelposition="left" name="categoriaArticolo.nome" required="true" size="40" cssClass="testo"/>
    <s:textfield label="Pos." labelposition="left" name="categoriaArticolo.posizione" required="true" size="5" cssClass="testo"/>
    <s:submit value="Salva" cssClass="button"/>
</s:form>
