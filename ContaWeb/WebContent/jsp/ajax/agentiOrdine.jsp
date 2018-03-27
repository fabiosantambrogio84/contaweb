<%-- <%@ taglib prefix="s" uri="/struts-tags" %> --%>

HELLO TIGER!
<%-- <s:form action="ordiniEdit" validate="true" method="POST" theme="css_xhtml"> --%>
<%-- 	<s:hidden name="action" value="edit"/> --%>
<%-- 	<s:hidden name="ordine.id" value="%{id}"/> --%>
	
   <s:select labelposition="left" label="Agente" list="listAgenti" required="true" listKey="id" listValue="nome"
	value="ordine.idAgenti" name="ordine.idAgenti"
	cssClass="testo"/>
<%-- 	<s:submit value="Aggiorna" cssClass="button"/>	 --%>
<%-- </s:form> --%> 