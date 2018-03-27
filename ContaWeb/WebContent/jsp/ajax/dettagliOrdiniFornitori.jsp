<%@ taglib prefix="s" uri="/struts-tags" %>

<table cellpadding="0" cellspacing="0" id="tableListaOrdini">
<tr>
	<th>Codice</th>
	<th>Descrizione</th>
	<th>Pezzi</th>
	<th></th>
</tr>
<s:iterator value="ordineFornitore.dettagliOrdineFornitore" status="itr">
 <tr id="riga<s:property value="#itr.count"/>">
 
 	<td><s:hidden name="dettagliOrdineFornitore('%{idArticolo}').pezziOrdinati" value="%{pezziOrdinati}" theme="simple"/><s:property value="articolo.codiceArticolo"/></td>
 	<td><s:property value="articolo.descrizione"/></td>
 	<td id="pezzo<s:property value="#itr.count"/>">
	 	<input type="text" id="hdnPezzo<s:property value="#itr.count"/>" size="2" class="hiddenOrdineQta"
	 		onblur="return updateRigaTabellaOrdini('tableListaOrdiniFornitori','<s:property value="#itr.count"/>')"/>
	 	<label id="lblPezzo<s:property value="#itr.count"/>"><s:property value="pezziOrdinati"/></label>
 	</td>
 	<td><a href="" onclick="return modificaRigaTabellaOrdini('tableListaOrdini','<s:property value="#itr.count"/>')">M</a> <a href="" onclick="return cancellaRigaTabellaOrdini('tableListaOrdini','riga<s:property value="#itr.count"/>')">C</a></td>
 	
 	<s:iterator value="richiesteOrdini">
 		<s:hidden name="dettagliOrdine('%{idDettaglioOrdine}').pezziDaOrdinare" value="%{qta}"/>
 		<s:hidden name="dettagliOrdine('%{idDettaglioOrdine}').idArticolo" value="%{dettaglioOrdine.idArticolo}"/>
 		<s:hidden name="dettagliOrdine('%{idDettaglioOrdine}').id" value="%{dettaglioOrdine.id}"/>
 	</s:iterator>
 	
 </tr>
</s:iterator>
</table>