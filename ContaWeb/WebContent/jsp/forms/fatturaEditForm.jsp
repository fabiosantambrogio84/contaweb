<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="fattureEdit" validate="true" method="POST" theme="css_xhtml">
		<s:hidden name="action" value="%{action}"/>
		<s:hidden name="fattura.id" value="%{id}"/>

		<div id="wwgrp_fattureEdit_fattura_data" class="wwgrp">
			<span id="wwlbl_fattureEdit_fattura_data" class="wwlbl">
				<label for="fattureEdit_fattura_data" class="label">Numero:</label>
			</span>
			<span id="wwctrl_fattureEdit_fattura_data" class="wwctrl">
				<s:property value="fattura.numeroProgressivo"/>
				<s:textfield  name="fattura.numeroProgressivo" cssClass="testo" required="true" />   
			</span>
		</div>	
		<div id="wwgrp_fattureEdit_fattura_data" class="wwgrp">
			<span id="wwlbl_fattureEdit_fattura_data" class="wwlbl">
				<label for="fattureEdit_fattura_data" class="label">Data:</label>
			</span>
			<span id="wwctrl_fattureEdit_fattura_data" class="wwctrl">
				<s:property value="fattura.data"/>
			</span>
		</div>
		
		<div id="wwgrp_fattureEdit_fattura_cliente" class="wwgrp">
			<span id="wwlbl_fattureEdit_fattura_cliente" class="wwlbl">
				<label for="fattureEdit_fattura_cliente" class="label">Cliente:</label>
			</span>
			<span id="wwctrl_fattureEdit_fattura_cliente" class="wwctrl">
				<s:property value="fattura.cliente.rs"/>
			</span>
		</div>
		
		<div id="wwgrp_fattureEdit_fattura_causale" class="wwgrp">
			<span id="wwlbl_fattureEdit_fattura_causale" class="wwlbl">
				<label for="fattureEdit_fattura_causale" class="label">Causale:</label>
			</span>
			<span id="wwctrl_fattureEdit_fattura_causale" class="wwctrl">
				<s:property value="fattura.causale"/>
			</span>
		</div>
		
		<div id="wwgrp_fattureEdit_fattura_sconto" class="wwgrp">
			<span id="wwlbl_fattureEdit_fattura_sconto" class="wwlbl">
				<label for="fattureEdit_fattura_sconto" class="label">Sconto:</label>
			</span>
			<span id="wwctrl_fattureEdit_fattura_sconto" class="wwctrl">
				<s:text name="format.prezzo">
					<s:param name="value" value="%{fattura.sconto}"/>
				</s:text>%
			</span>
		</div>
		
		<div id="wwgrp_fattureEdit_fattura_note" class="wwgrp">
			<span id="wwlbl_fattureEdit_fattura_note" class="wwlbl">
				<label for="fattureEdit_fattura_note" class="label">Note:</label>
			</span>
			<span id="wwctrl_fattureEdit_fattura_note" class="wwctrl">
				<s:property value="fattura.noteFattura"/>&nbsp;
			</span>
		</div>
		
		<div id="wwgrp_fattureEdit_fattura_note" class="wwgrp">
			<span id="wwlbl_fattureEdit_fattura_note" class="wwlbl">
				<label for="fattureEdit_fattura_note" class="label">Lista DDT:</label>
			</span>
			<br/>
			<ul>
				<s:iterator value="fattura.dettagliFattura">
					<li><s:property value="descrizioneBreveDDT"/></li>
				</s:iterator>
			</ul>
		</div>
		<br/>
		<div id="wwgrp_fattureEdit_fattura_note" class="wwgrp">
			<span id="wwlbl_fattureEdit_fattura_note" class="wwlbl">
				<label for="fattureEdit_fattura_note" class="label">Totale:</label>
			</span>
			<span id="wwctrl_fattureEdit_fattura_note" class="wwctrl">
				<s:text name="format.prezzo">
					<s:param name="value" value="%{fattura.totaleFattura}"/>
				</s:text> &#8364;
			</span>
		</div>
		<s:checkbox labelposition="left" label="Pagato:" name="fattura.pagato"/>   

    	<s:submit value="Salva" cssClass="button"/>
</s:form>
