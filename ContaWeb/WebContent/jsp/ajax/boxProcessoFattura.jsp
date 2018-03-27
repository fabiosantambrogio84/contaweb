<%@ taglib prefix="s" uri="/struts-tags" %>

<s:url id="urlStatoProcesso" action="statoProcessoFatture"/>

<!-- MOSTRO IL BOX FATTURE -->
<div id="divProcessaFatture">
		<s:if test="textResponse != null">
			<s:property value="textResponse"/>
		</s:if>
		<s:elseif test="errors.dataLimite != null">
			Valore della data non corretto
		</s:elseif>
		<s:else>
			<s:div 
				theme="ajax" id="divValPerc"
				href="%{urlStatoProcesso}"
				startTimerListenTopics="/startRefresh" 
			  	stopTimerListenTopics="/stopRefresh"
			  	executeScripts="true"
			  	updateFreq="1000"
			  	loadingText="."
			  	errorText=""/>
			<div>CREAZIONE FATTURE:</div>
		</s:else>
</div>