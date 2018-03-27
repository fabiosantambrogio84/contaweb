<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css">
	.statisticheArticoli small { font-size: 10px; }
	.statisticheArticoli ul { overflow: auto;  }
	.statisticheArticoli li { white-space: nowrap; } 
</style>

<table cellpadding="1" cellspacing="0" border="0">
<tr>
	<td class="leftColumnStatisticheBox"><strong><small>Per cliente<br>Sett.</small></strong></td>
	<td><strong><small>Per cliente<br>Mese</small></strong></td>
</tr>
<tr>
	<td  class="leftColumnStatisticheBox">
		<ul class="statisticheArticoli">
			<s:iterator value="statSettimanaCliente">
				<li title="<s:property value="descCompleta"/>">
					<s:a id="bttInserisciArticolo" theme="ajax" cssClass="big-plus" onclick="return inserisciArticolo_daStats(\"${id}\",\"${codiceArticolo}\",\"${descrizione}\")">
						<small>${codiceArticolo} - <s:property value="descrizioneOrdini"/></small>
					</s:a>
				</li>
			</s:iterator>
		</ul>
	</td>

	<td valign="top">
		<ul class="statisticheArticoli">
			<s:iterator value="statMeseCliente">
				<li title="<s:property value="descCompleta"/>"><s:a id="bttInserisciArticolo" theme="ajax" cssClass="big-plus" onclick="return inserisciArticolo_daStats(\"${id}\",\"${codiceArticolo}\",\"${descrizione}\")"><small>${codiceArticolo} - <s:property value="descrizioneOrdini"/></small></s:a></li>
			</s:iterator>
		</ul>
	</td>
</tr>

<tr>
	<td class="leftColumnStatisticheBox"><strong><small>Generale<br>Sett.</small></strong></td>
	<td><strong><small>Generale<br>Mese</small></strong></td>
</tr>

<tr>
	<td class="leftColumnStatisticheBox">
		<ul class="statisticheArticoli">
			<s:iterator value="statSettimana">
				<li title="<s:property value="descCompleta"/>"><s:a id="bttInserisciArticolo" theme="ajax" cssClass="big-plus" onclick="return inserisciArticolo_daStats(\"${id}\",\"${codiceArticolo}\",\"${descrizione}\")"><small>${codiceArticolo} - <s:property value="descrizioneOrdini"/></small></s:a></li>
			</s:iterator>
		</ul>
	</td>

	<td valign="top">
		<ul class="statisticheArticoli">
			<s:iterator value="statMese">
				<li title="<s:property value="descCompleta"/>"><s:a id="bttInserisciArticolo" theme="ajax" cssClass="big-plus" onclick="return inserisciArticolo_daStats(\"${id}\",\"${codiceArticolo}\",\"${descrizione}\")"><small>${codiceArticolo} - <s:property value="descrizioneOrdini"/></small></s:a></li>
			</s:iterator>
		</ul>
	</td>
</tr>
</table>