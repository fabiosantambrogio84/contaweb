<%@ taglib prefix="s" uri="/struts-tags" %>

<h3>Edita listini associati al cliente: <s:property value="cliente.rs"/></h3>
<div class="formContainer">
	<s:form action="listiniAssociatiList" validate="true" method="POST" theme="css_xhtml">
		<s:hidden name="action" value="edit"/>
		<s:hidden name="cliente.id" value="%{cliente.id}"/>
	
			<s:iterator value="listListiniAssociati">
				<s:select labelposition="left" label="%{fornitore.descrizione}" list="listListini" listKey="id" listValue="descrizione" value="idListino" name="listiniSelezionati(%{idFornitore}).idListino"/>
			</s:iterator>
	
	    <s:submit value="Aggiorna" cssClass="button"/>
	</s:form>
</div>