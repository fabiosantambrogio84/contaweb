<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="bollaAcquistoImportMondin" validate="true" method="POST" enctype="multipart/form-data">
	<s:file name="fileUpload" label="Seleziona file da importare" size="40" /> 
	<s:submit name="action" value="Carica" /> 
</s:form>