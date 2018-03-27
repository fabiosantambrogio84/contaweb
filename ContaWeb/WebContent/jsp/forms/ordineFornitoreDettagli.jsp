<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="wwgrp_form1_ordineFornitore_numeroProgressivo" class="wwgrp">
  <span id="wwlbl_form1_ordineFornitore_numeroProgressivo" class="wwlbl">
    <label for="form1_ordineFornitore_numeroProgressivo" class="label">Numero progressivo:
    </label>
  </span> 
  <span id="wwctrl_form1_ordineFornitore_numeroProgressivo" class="wwctrl">
    <s:property value="ordineFornitore.progressivoCompleto"/>
  </span>
</div>

<div id="wwgrp_form1_ordineFornitore_dataCreazione" class="wwgrp">
  <span id="wwlbl_form1_ordineFornitore_dataCreazione" class="wwlbl">
    <label for="form1_ordineFornitore_dataCreazione" class="label">Data creazione:
    </label>
  </span> 
  <span id="wwctrl_form1_ordineFornitore_dataCreazione" class="wwctrl">
    <s:property value="ordineFornitore.dataCreazione"/>
  </span>
</div>

<div id="wwgrp_form1_ordineFornitore_idFornitore" class="wwgrp">
  <span id="wwlbl_form1_ordineFornitore_idFornitore" class="wwlbl">
    <label for="form1_ordineFornitore_idFornitore" class="label">Fornitore:
    </label>
  </span> 
  <span id="wwctrl_form1_ordineFornitore_idFornitore" class="wwctrl">
    <s:property value="ordineFornitore.fornitore.descrizione"/>
</span></div>

<br/>
<strong>Dettagli</strong>
<table cellpadding="0" cellspacing="0" id="tableListaOrdini">
  <tr>
	<th>Codice</th>
	<th>Descrizione</th>
	<th>Pezzi</th>
</tr>
<s:iterator value="ordineFornitore.dettagliOrdineFornitore" status="itr">
 <tr>
 	<td><s:property value="articolo.codiceArticolo"/></td>
 	<td><s:property value="articolo.descrizione"/></td>
 	<td><s:property value="pezziOrdinati"/></td>
 </tr>
</s:iterator>
</table>

<br/>
<div id="wwgrp_form1_ordineFornitore_note" class="wwgrp">
  <span id="wwlbl_form1_ordineFornitore_note" class="wwlbl">
    <label for="form1_ordineFornitore_note" class="label">Note:
    </label>
  </span> 
  <span id="wwctrl_form1_ordineFornitore_note" class="wwctrl">
    <s:property value="ordineFornitore.note"/>
  </span>
</div>