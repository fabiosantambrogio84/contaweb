<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="ivaEdit" validate="true" method="POST" theme="css_xhtml">
	<s:hidden name="action" value="%{action}"/>
	<s:hidden name="iva.id" value="%{iva.id}"/>
    <s:textfield label="Aliquota" labelposition="left" name="iva.valore" required="true" size="10" cssClass="testo"/>
    <s:submit value="Salva" cssClass="button"/>
</s:form>
