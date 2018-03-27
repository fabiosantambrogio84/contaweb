<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="row">
			<div class="col-md-10">
				<div class="col-md-8"><input type="text" id="filter" placeholder="Cerca cliente..." class="form-control" /></div> 
				<div class="col-md-4"><a href="javascript:void(0)" class="btn btn-default" id="btnFilter">Filtra</a></div>
				<script>
				$(document).ready(function(){
			    	$('#btnFilter').click(function() { 
			    		event.preventDefault();
			    		var val = $("#filter").val().toLowerCase();
						if(val !== ""){
						
							$.each($("#table_clienti .filter_rs"), function(){
								var text = $(this).text().toLowerCase();
								if(text.indexOf(val) > -1){
									$(this).parent().show();
								}
								else{
									$(this).parent().hide();
								}
							})
			    		}
			    		else{
			        		$("#table_clienti td.filter_rs").parent().show();
			    		}
			    	});
				});	
				</script>
			</div>
		</div>
	</div>
	<div class="panel-body">		
		<div class="row table-wrapper">
			<div class="col-md-10">
				<table class="table" id="table_clienti">
			    	<thead>
			        	<tr>
			        		<th></th>
			            	<th>Ragione Sociale</th>
			        	</tr>
			    	</thead>
			    	<tbody>
			    	<s:iterator value="clienti">
			        	<tr>
			        		<td>
			        			<input type="radio" id="idCliente" name="idCliente" value="<s:property value="id" />">
			        		</td>
			        		<td class="filter_rs">
			        			<s:property value="rs" />
			        		</td>
			        	</tr>
			       	</s:iterator>
			    	</tbody>
				</table>
			</div>	
		</div>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="pull-right">
				<button type="button" onclick="closeGetClienti()" class="btn btn-default">Chiudi</button>
			    <button type="button" onclick="setCliente()"  class="btn btn-default">Ok</button>
			</div>
		</div>
	</div>
</div>