<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="propertiesEdit" id="propertiesEdit" validate="true" method="POST" theme="css_xhtml">
    <table width="100%">
        <tr>  
	    	<td align="left" width="30%">Applet Url: </td>
	    	<td align="left" width="50%"> 
	    		<s:property value="appletUrl"/> <br>
	        	<input type="text" name="appletUrl" value="" size="48"> </td>
	    	<td width="20%"> <s:submit name="action:writeAppletUrl" value="Write Data" cssClass="button"/> </td>
        </tr> 
	
		<tr> 
	    	<td align="left">Articoli Images Repository: </td>
	    	<td align="left"> 
	    		<s:property value="articoliImgRepository"/> <br>
	        	<input type="text" name="articoliImgRepository" value="" size="48"> </td>
	    	<td> <s:submit name="action:writeArticoliImgRepository" value="Write Data" cssClass="button"/> </td>
        </tr> 
	
		<tr> 
            <td align="left">Fatture - From Address: </td>
            <td align="left"> 
            	<s:property value="fattureFromAddress"/> <br>
	        	<input type="text" name="fattureFromAddress" value="" size="48"> </td>
            <td> <s:submit name="action:writeFattureFromAddress" value="Write Data" cssClass="button"/> </td>
        </tr> 
        
		<tr> 
            <td align="left">Fatture - Contenuto mail: </td>
            <td align="left"> 
            	<s:property value="fattureMailContent"/> <br>
	        	<input type="text" name="fattureMailContent" value="" size="48"> </td>
            <td> <s:submit name="action:writeFattureMailContent" value="Write Data" cssClass="button"/> </td>
        </tr> 
			
		<tr> 
            <td align="left">Fatture - Prefisso: </td>
            <td align="left"> 
            	<s:property value="fattureMailSubjectPrefix"/> <br>
	        	<input type="text" name="fattureMailSubjectPrefix" value="" size="48"> </td>
            <td> <s:submit name="action:writeFattureMailSubjectPrefix" value="Write Data" cssClass="button"/> </td>
        </tr> 
			
		<tr>
            <td align="left">Numero elementi: </td>
            <td align="left"> 
            	<s:property value="listElementi"/> <br>
	        	<input type="text" name="listElementi" value="" size="48"> </td>
            <td> <s:submit name="action:writeListElementi" value="Write Data" cssClass="button"/> </td>
        </tr> 
			
		<tr>
            <td align="left">Repository Fatture: </td>
            <td align="left"> 
            	<s:property value="mailRepositoryFatture"/> <br>
	        	<input type="text" name="mailRepositoryFatture" value="" size="48"> </td>
            <td> <s:submit name="action:writeMailRepositoryFatture" value="Write Data" cssClass="button"/> </td>
        </tr>
			
		<tr>
            <td align="left">Repository Ordini Fornitori: </td>
            <td align="left"> 
            	<s:property value="mailRepositoryOrdiniFornitori"/> <br>
	        	<input type="text" name="mailRepositoryOrdiniFornitori" value="" size="48"> </td>
            <td> <s:submit name="action:writeMailRepositoryOrdiniFornitori" value="Write Data" cssClass="button"/> </td>
        </tr>

		<tr>
            <td align="left">SMTP Host: </td>
            <td align="left"> 
            	<s:property value="mailSmtpHost"/> <br>
	        	<input type="text" name="mailSmtpHost" value="" size="48"> </td>
            <td> <s:submit name="action:writeMailSmtpHost" value="Write Data" cssClass="button"/> </td>
        </tr> 

		<tr>
            <td align="left">SMTP Password: </td>
            <td align="left"> 
            	<s:property value="mailSmtpPassword"/> <br>
	        	<input type="text" name="mailSmtpPassword" value="" size="48"> </td>
            <td> <s:submit name="action:writeMailSmtpPassword" value="Write Data" cssClass="button"/> </td>
        </tr>

		<tr>
            <td align="left">SMTP User: </td>
            <td align="left"> 
            	<s:property value="mailSmtpUser"/> <br>
	        	<input type="text" name="mailSmtpUser" value="" size="48"> </td>
            <td> <s:submit name="action:writeMailSmtpUser" value="Write Data" cssClass="button"/> </td>
        </tr> 

		<tr>
            <td align="left">Ordini Fornitori - From Address: </td>
            <td align="left"> 
            	<s:property value="ordiniFornitoriFromAddress"/> <br>
	        	<input type="text" name="ordiniFornitoriFromAddress" value="" size="48"> </td>
            <td> <s:submit name="action:writeOrdiniFornitoriFromAddress" value="Write Data" cssClass="button"/> </td>
        </tr> 

		<tr>
            <td align="left">Ordini Fornitori - Contenuto: </td>
            <td align="left"> 
            	<s:property value="ordiniFornitoriMailContent"/> <br>
	        	<input type="text" name="ordiniFornitoriMailContent" value="" size="48"> </td>
            <td> <s:submit name="action:writeOrdiniFornitoriMailContent" value="Write Data" cssClass="button"/> </td>
        </tr>

		<tr>
            <td align="left">Ordini Fornitori - Prefisso: </td>
            <td align="left"> 
            	<s:property value="ordiniFornitoriMailSubjectPrefix"/> <br>
	        	<input type="text" name="ordiniFornitoriMailSubjectPrefix" value="" size="48"> </td>
            <td> <s:submit name="action:writeOrdiniFornitoriMailSubjectPrefix" value="Write Data" cssClass="button"/> </td>
        </tr> 

		<tr>
            <td align="left">PEC SMTP Host: </td>
            <td align="left"> 
            	<s:property value="pecSmtpHost"/> <br>
	        	<input type="text" name="pecSmtpHost" value="" size="48"> </td>
            <td> <s:submit name="action:writePecSmtpHost" value="Write Data" cssClass="button"/> </td>
        </tr>

		<tr>
            <td align="left">PEC SMTP Password: </td>
            <td align="left"> 
            	<s:property value="pecSmtpPassword"/> <br>
	        	<input type="text" name="pecSmtpPassword" value="" size="48"> </td>
            <td> <s:submit name="action:writePecSmtpPassword" value="Write Data" cssClass="button"/> </td>
        </tr>

		<tr> 
            <td align="left">PEC SMTP User: </td>
            <td align="left"> 
            	<s:property value="pecSmtpUser"/> <br>
	        	<input type="text" name="pecSmtpUser" value="" size="48"> </td>
            <td> <s:submit name="action:writePecSmtpUser" value="Write Data" cssClass="button"/> </td>
        </tr>

		<tr>
            <td align="left">PDF Repository: </td>
            <td align="left"> 
            	<s:property value="printPdfRepository"/> <br>
	        	<input type="text" name="printPdfRepository" value="" size="48"> </td>
            <td> <s:submit name="action:writePrintPdfRepository" value="Write Data" cssClass="button"/> </td>
        </tr>

		<tr>
            <td align="left">Password accesso Completo: </td>
            <td align="left"> 
            	<s:property value="passwordAccessoCompleto"/> <br>
	        	<input type="text" name="passwordAccessoCompleto" value="" size="48" disabled="disabled"> </td>
            <td> <s:submit name="action:writePasswordAccessoCompleto" value="Write Data" cssClass="button"/> </td>
        </tr>

		<tr>
            <td align="left">Password accesso Normale: </td>
            <td align="left"> 
            	<s:property value="passwordAccessoNormale"/> <br>
	        	<input type="text" name="passwordAccessoNormale" value="" size="48" disabled="disabled"> </td>
            <td> <s:submit name="action:writePasswordAccessoNormale" value="Write Data" cssClass="button"/> </td>
        </tr>

		<tr>
            <td align="left">Password accesso Limitato: </td>
            <td align="left"> 
            	<s:property value="passwordAccessoLimitato"/> <br>
	        	<input type="text" name="passwordAccessoLimitato" value="" size="48" disabled="disabled"> </td>
            <td> <s:submit name="action:writePasswordAccessoLimitato" value="Write Data" cssClass="button"/> </td>
        </tr>
	</table>	
	
        	 
    <%-- <s:submit name="action:updateData" value="Update data" cssClass="button"/> --%>

	<br><br>
    <table width="100%">
        <tr>  
	    	<td align="left" width="30%">Backup Location: </td>
            <td align="left">
                <s:property value="backupLocation"/> <br>
                <input type="text" name="backupLocation" value="" size="48">
            </td>
			<td><s:submit name="action:writeBackupLocation" value="Write Data" cssClass="button"/></td>
        </tr>
        <tr>
        	<td></td>
        	<td></td>
        	<td>
        		<s:submit name="action:backupDB" value="backup DB" cssClass="button"/>
				<s:submit name="action:restoreDB" value="restore DB" cssClass="button" disabled="disabled"/>
			</td>
		</tr>
	</table>	
</s:form>
