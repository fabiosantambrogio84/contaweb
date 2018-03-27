<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="passwordEdit" validate="true" method="POST" theme="css_xhtml">
    <table width="100%">
        <tr>  
	    	<td align="left" width="40%">Access Level: </td>
	    	<td align="left" width="30%"> 
	    		<s:property value="accessLevel"/> </td>
	    	<td align="left" width="30%"> 
	        	<input type="text" name="accessLevel" value="" size="48"> </td>
        </tr> 
	
		<tr> 
	    	<td align="left" width="40%">Access Password: </td>
	    	<td align="left" width="30%"> 
	    		<s:property value="accessPassword"/> </td>
	    	<td align="left" width="30%"> 
	        	<input type="password" name="accessPassword" value="" size="48"> </td>
        </tr> 

		<tr> 
	    	<td align="left" width="40%">Access Allowed: </td>
	    	<td align="left" width="30%"> 
	    		<s:property value="accessAllowed"/> </td>
	    	<td width="30%"> <s:submit name="action:checkAccessPassword" value="Check" cssClass="button"/> </td>
        </tr> 
    </table>	
</s:form>
