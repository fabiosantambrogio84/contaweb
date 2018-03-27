<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="processoCompletato == true">
	<s:if test="messaggioErrore == null">
		COMPLETATO
	</s:if>
	<s:else>
		<s:property value="messaggioErrore"/>
	</s:else>
	<script type="text/javascript">
		stopProcessaFatture();
	</script>
</s:if>
<s:else>
	<!-- STATO AVANZAMENTO -->
	<s:property value="percAvanzamento"/>%
</s:else>