<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="telefonataEdit" id="telefonataForm" validate="true" method="POST" theme="css_xhtml">
	<s:hidden name="action" value="%{action}"/>
	<s:hidden name="telefonata.id" value="%{telefonata.id}"/>
	<s:hidden name="typeList" value="%{typeList}"/>

	<s:select labelposition="left" onchange="dojo.event.topic.publish('/refreshPuntiConsegna');" label="Cliente" list="listClienti"
	required="true" listKey="id" listValue="rs" name="telefonata.idCliente"
	value="%{telefonata.idCliente}" cssClass="testo"/>
	
	<s:url id="getPuntiConsegna" action="getPuntiConsegna"/>
	<s:div theme="ajax" errorText="Errore nel caricamento. Riprovare" href="%{getPuntiConsegna}" formId="telefonataForm" listenTopics="/refreshPuntiConsegna"/>

	<div class="wwgrp">
	<span class="wwlbl"><label class="label">        Telefono 1:</label></span>
	00
	<s:textfield name="telefonata.prefissoTelefono1" size="2" cssClass="testo" theme="simple"/>
	-
	<s:textfield name="telefonata.numeroTelefono1" size="20" cssClass="testo" theme="simple"/>
	</div>
	
	<div class="wwgrp">
	<span class="wwlbl"><label class="label">        Telefono 2:</label></span>
	00
	<s:textfield name="telefonata.prefissoTelefono2" size="2" cssClass="testo" theme="simple"/>
	-
	<s:textfield name="telefonata.numeroTelefono2" size="20" cssClass="testo" theme="simple"/>
	</div>
	
	<div class="wwgrp">
	<span class="wwlbl"><label class="label">        Telefono 3:</label></span>
	00
	<s:textfield name="telefonata.prefissoTelefono3" size="2" cssClass="testo" theme="simple"/>
	-
	<s:textfield name="telefonata.numeroTelefono3" size="20" cssClass="testo" theme="simple"/>
	</div>


	<s:select labelposition="left" label="Giorno da chiamare" list="giorni" name="telefonata.giorno"/>
	<s:textfield name="telefonata.orario" label="Orario da chiamare" labelposition="left"
	cssClass="testo" maxlength="2" size="2"/>
	
	<strong>Note:</strong>
	<s:textarea name="telefonata.note" value="%{telefonata.note}" cols="65" rows="4" cssClass="testo"/>
	
	
    <s:submit value="Salva" cssClass="button"/>
</s:form>
<script type="text/javascript">
	$(function()  {		
		$("select[name='telefonata.idCliente']").select2();
	});
</script>