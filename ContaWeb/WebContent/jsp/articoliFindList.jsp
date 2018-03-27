<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="row">
			<div class="col-md-10">
				<div class="col-md-2">
				<select class="form-control" id="filtertype">
					<option value="cod">COD</option>
					<option value="desc">DESC</option>
				</select>
				</div>
				<div class="col-md-6"><input type="text" id="filter" placeholder="Cerca articolo..." class="form-control" /></div> 
				<div class="col-md-4"><a href="javascript:void(0)" class="btn btn-default" id="btnFilter">Filtra</a></div>
				<script>
				$(document).ready(function(){
				    $('#btnFilter').click(function() { 
				    	event.preventDefault();
				    	var filType = $("#filtertype option:selected").val();
				    	var val = $("#filter").val().toLowerCase();
						if(val !== ""){
							
							$.each($("#table_articoli .filter_" + filType), function(){
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
				        	$("#table_articoli td.filter_" + filType).parent().show();
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
				<table class="table" id="table_articoli">
				    <thead>
				        <tr>
				        	<th></th>
				        	<th>Codice</th>
				            <th>Descrizione</th>
				        </tr>
				    </thead>
				    <tbody>
				    <s:iterator value="articoli">
				        <tr>
				        	<td>
				        		<input type="radio" id="idArticolo" name="idArticolo" value="<s:property value="id" />">
				        	</td>
				        	<td class="filter_cod">
				        		<s:property value="codiceArticolo" />
				        	</td>
				        	<td class="filter_desc">
				        		<s:property value="descrizione" />
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
				<button type="button" onclick="closeGetArticoli()" class="btn btn-default">Chiudi</button>
			    <button type="button" onclick="setArticolo()"  class="btn btn-default">Ok</button>
			</div>
		</div>
	</div>	
</div>


