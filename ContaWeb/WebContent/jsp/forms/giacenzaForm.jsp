<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="giacenzaEdit" validate="true" method="POST">
	<s:hidden name="action" value="edit"/>
	<s:hidden name="giacenza.id" value="%{id}"/>
	<s:if test="action == 'insert'">
		<s:select labelposition="left" label="Articolo" list="listArticoli" listKey="id" listValue="descCompleta" value="giacenza.idArticolo" name="giacenza.idArticolo" cssClass="testo"/>	
	</s:if>
	<s:else>
			<div class="wwgrp">
				<span class="wwlbl">
					<label class="label">        Articolo:</label>
				</span>
				<span class="wwctrl">
					<s:hidden name="giacenza.idArticolo" value="%{giacenza.idArticolo}"/>
					<s:property value="giacenza.articolo.descrizione"/>
				</span>
			</div>
	</s:else>
	<s:if test="action == 'insert'">
		<s:textfield label="Lotto" labelposition="left" name="giacenza.lotto" size="25" cssClass="testo" required="true"/>
	</s:if>
	<s:else>
			<div class="wwgrp">
				<span class="wwlbl">
					<label class="label">        Lotto:</label>
				</span>
				<span class="wwctrl">
					<s:hidden name="giacenza.lotto" value="%{giacenza.lotto}"/>
					<s:property value="giacenza.lotto"/>
				</span>
			</div>
	</s:else>
	
	<s:textfield label="Quantità" labelposition="left" name="giacenza.qta" size="5" cssClass="testo" required="true" value="%{getText('format.qta',{giacenza.qta})}"/>
	<s:textfield label="Data Scadenza" labelposition="left" name="giacenza.dataScadenza" size="12" cssClass="testo"/>
	<s:submit value="Aggiorna" cssClass="button"/>
</s:form>