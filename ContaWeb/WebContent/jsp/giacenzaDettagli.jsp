<%@ taglib prefix="s" uri="/struts-tags" %>

<h3>Dettagli giacenza</h3>

<s:iterator value="dettagli">
<div class="formContainerNoFloat">
		<div class="wwgrp">
			<span class="wwlbl">
				<label class="label">        Lotto:</label>
			</span>
			<span class="wwctrl">
				<s:hidden name="giacenza.lotto" value="%{lotto}"/>
				<s:property value="lotto"/>
			</span>
		</div>
		<div class="wwgrp">
				<span class="wwlbl">
					<label class="label">        Quantità:</label>
				</span>
				<span class="wwctrl">
					<s:text name="format.qta">
						<s:param name="value" value="%{qta}"/>
					</s:text>
					<s:property value="articolo.um"/>
				</span>
		</div>
		<div class="wwgrp">
				<span class="wwlbl">
					<label class="label">        Data Scadenza:</label>
				</span>
				<span class="wwctrl">
					<s:property value="dataScadenza" default="NON DEFINITA"/>
				</span>
		</div>
		<div class="wwgrp">
			<h4>Movimenti:</h4>
			<ul>
				<s:iterator value="movimenti">
					<% String classLi = ""; %>
					<% String text = ""; %>
					<s:if test="tipoMovim == 0">
						<% classLi = "movimentiListAggiunti";  %>
						<% text = "Inseriti"; %>
					</s:if>
						
					<s:if test="tipoMovim == 1 || tipoMovim == 3">
						<% classLi = "movimentiListAggiunti";  %>
						<% text = "Aggiunto/i"; %>
					</s:if>
					
					<s:if test="tipoMovim == 2 || tipoMovim == 4">
						<% classLi = "movimentiListRimossi";  %>
						<% text = "Rimosso/i"; %>
					</s:if>
					
					<li class="<%=classLi %>">
						<%=text %>
						
						<s:property value="descTipoMovim"/>
						<s:property value="articolo.um"/>
					  	<s:text name="format.qta">
							<s:param name="value" value="%{qta}"/>
						</s:text>
						il <s:property value="data"/>
						
						<s:if test="ddt != null">
						(DDT n. <s:property value="ddt.numeroProgressivo"/>/<s:property value="ddt.annoContabile"/>)
						</s:if>
						<s:if test="bollaAcquisto != null">
						(Bolla Acquisto n. <s:property value="bollaAcquisto.numeroBolla"/>/<s:property value="bollaAcquisto.annoContabile"/>)
						</s:if>
						<s:if test="tipoMovim == 3 || tipoMovim == 4">
						(Documento cancellato)
						</s:if>
					</li>
				</s:iterator>
			</ul>
		</div>
		
		<s:url id="editURL" action="giacenzaEdit_input">
			<s:param name="action">edit</s:param>
			<s:param name="id"><s:property value="id"/></s:param>
		</s:url>
		
		<s:url id="deleteURL" action="giacenzaEdit_input">
			<s:param name="action">delete</s:param>
			<s:param name="id"><s:property value="id"/></s:param>
		</s:url>
		
		<s:a href="%{editURL}" cssClass="link">Modifica</s:a> <s:a href="%{deleteURL}" cssClass="link">Cancella</s:a>
</div>
</s:iterator>