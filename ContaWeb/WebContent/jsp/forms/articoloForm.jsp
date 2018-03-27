<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="articoliEdit" validate="true" enctype="multipart/form-data" method="POST" 
 	theme="css_xhtml">
<table style="width: 100%">
	<tr>
		<td>
	<s:hidden name="action" value="%{action}"/>
	<s:hidden name="articolo.id" value="%{articolo.id}"/>
    <s:select labelposition="left" label="Categoria" list="listCategorie" listKey="id" listValue="nome" value="articolo.idCategoria" name="articolo.idCategoria" cssClass="testo"/>
		    <s:select labelposition="left" label="Destinazione" emptyOption="1" list="listDestinazioni" listKey="id" listValue="nome" value="articolo.idDestinazione" name="articolo.idDestinazione" cssClass="testo"/>
	<s:textfield label="Codice" labelposition="left" name="articolo.codiceArticolo" required="true" size="10" cssClass="testo"/>
	<s:textfield label="Descrizione" labelposition="left" name="articolo.descrizione" required="true" size="40" cssClass="testo"/>
    <s:textfield label="Um" labelposition="left" name="articolo.um" size="2" cssClass="testo" required="true"/>

	<s:if test="articolo.qtaPredefinita != null">    
    	<s:textfield label="Q.tà predefinita" labelposition="left" name="articolo.qtaPredefinita" value="%{getText('format.qta',{articolo.qtaPredefinita})}" size="8" cssClass="testo"/>
	</s:if>
	<s:else>
		<s:textfield label="Q.tà predefinita" labelposition="left" name="articolo.qtaPredefinita" size="8" cssClass="testo"/>
	</s:else>
	<s:textfield label="Prezzo acquisto" labelposition="left" name="articolo.prezzoAcquisto" value="%{getText('format.prezzo',{articolo.prezzoAcquisto})}"  size="8" cssClass="testo"/>
	<s:textfield label="Giorni scadenza" labelposition="left" name="articolo.ggScadenza" size="4" cssClass="testo"/>
	<s:textfield label="Cod. barre" labelposition="left" name="articolo.barCode" size="20" cssClass="testo"/>
    <s:checkbox  label="Cod. barre comp.:" labelposition="left" name="articolo.completeBarCode"/>
    <s:checkbox  label="Articolo attivo:" labelposition="left" name="articolo.attivo" value="articolo.attivo"/>  
    <!-- FORNITORI -->
    <s:select labelposition="left" label="Fornitore" list="listFornitori" listKey="id" listValue="descrizione" value="articolo.idFornitore" name="articolo.idFornitore" cssClass="testo"/>
    <!-- IVA -->
    <s:select labelposition="left" label="Iva" list="listIvas" listKey="id" listValue="valore" value="articolo.idIva" name="articolo.idIva" cssClass="testo"/>
		
    <!-- PREZZI -->
	    <div class="formSubGroup">
	    		<h4>Prezzo per listini:</h4>
	    		<s:iterator value="articolo.prezzi">
 						<s:textfield label="%{listino.descrizione} €" labelposition="left" name="listaPrezzi(%{idListino}).prezzo" value="%{getText('format.prezzo',{prezzo})}" size="6" cssClass="testo"/>	
				</s:iterator>
	    </div>
		</td>
		<td>
			<s:checkbox labelposition="left" label="Sito Web" value="articolo.web" name="articolo.web"></s:checkbox>		
		<table>
			<tr>
				<td>
					<img src="<s:property value="articolo.immagine1" />" style="width: 120px; float: right;" />
					</td>
				<td>
					<s:file labelposition="left" label="Immagine 1" required="false" name="fileUpload"></s:file>
				</td>
			</tr>
			<tr>
				<td>
					<img src="<s:property value="articolo.immagine2" />" style="width: 120px; float: right;" />
					</td>
				<td>
					<s:file labelposition="left" label="Immagine 2" required="false" name="fileUpload"></s:file>
				</td>
			</tr>
			<tr>
				<td>
					<img src="<s:property value="articolo.immagine3" />" style="width: 120px; float: right;" />
					</td>
				<td>
					<s:file labelposition="left" label="Immagine 3" required="false" name="fileUpload"></s:file>
				</td>
			</tr>
			<tr>
				<td>
					<img src="<s:property value="articolo.immagine4" />" style="width: 120px; float: right;" />
					</td>
				<td>
					<s:file labelposition="left" label="Immagine 4" required="false" name="fileUpload"></s:file>
				</td>
			</tr>
			<tr>
				<td>
					<img src="<s:property value="articolo.immagine5" />" style="width: 120px; float: right;" />
					</td>
				<td>
					<s:file labelposition="left" label="Immagine 5" required="false" name="fileUpload"></s:file>
				</td>
			</tr>
			<tr>
				<td>
					<img src="<s:property value="articolo.immagine6" />" style="width: 120px; float: right;" />
					</td>
				<td>
					<s:file labelposition="left" label="Immagine 6" required="false" name="fileUpload"></s:file>
				</td>
			</tr>
		</table>		
		</td>
	</tr>
</table>

    <s:submit value="Salva" cssClass="button"/>
</s:form>