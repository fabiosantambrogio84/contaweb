<%@ taglib prefix="s" uri="/struts-tags" %>

<!-- LISTA STATISTICHE -->
<div id="lista_container">
<h3>Documenti di vendita</h3>

<table>
<tr>
	<td><strong>Numero progressivo</strong></td>
	<td><strong>Data DDT</strong></td>
	<td><strong>Cliente</strong></td>
</tr>
<s:iterator value="listDDT">
	<tr>
		<td><s:property value="numeroProgressivo"/>
		<s:if test="numeroProgressivo2 != ''">/<s:property value="numeroProgressivo2"/></s:if></td>
		<td><s:property value="data"/></td>
		<td><s:property value="cliente.rs"/></td>
	</tr>
</s:iterator>
</table>

<h3>Documenti di acquisto</h3>

<table>
<tr>
	<td><strong>Numero</strong></td>
	<td><strong>Data Bolla acquisto</strong></td>
	<td><strong>Fornitore</strong></td>
</tr>
<s:iterator value="listBolle">
	<tr>
		<td><s:property value="numeroBolla"/></td>
		<td><s:property value="data"/></td>
		<td><s:property value="fornitore.descrizione"/></td>
	</tr>
</s:iterator>
</table>
</div>