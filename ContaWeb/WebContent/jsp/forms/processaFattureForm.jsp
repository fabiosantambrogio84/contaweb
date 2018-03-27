<%@ taglib prefix="s" uri="/struts-tags" %>
	
<s:div theme="ajax" id="progressContainer"/>

<s:form id="form" onsubmit="startProcessaFatture();" action="calcolaFatture">
  <s:textfield size="10" required="true" labelposition="left" label="Data fatturazione" name="dataLimite" cssClass="testo"/>
  <s:submit type="submit" id="submitFormProcessaFatture" loadingText="." theme="ajax" targets="progressContainer" value="Processa fatture" cssClass="button"/>		
</s:form>
