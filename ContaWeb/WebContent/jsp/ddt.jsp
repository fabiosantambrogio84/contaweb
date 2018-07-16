<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<form id="ddtform" action="saveDdt()" onload="return onDdtMaskFormLoad()">
	<s:hidden name="ddtObject.ddt.id" />
	<div class="modal-header">
	  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	  <h2 class="modal-title">
		<tiles:getAsString name="title"/>
	  </h2>
	  <div class="clearfix"></div>
	</div>
	<div class="modal-body">
		<div class="container col-md-12">
		    <fieldset class="intestazione">
		        <legend class="row">Intestazione</legend>
		
		        <div class="row">
		            <div class="form-inline">
		                <div class="col-md-7 col-sm-7">
		                    <div class="checkbox col-md-4 col-sm-6 col-xs-5">
		                        <label>
		                            <input id="autonum" onchange="enableNumAut()" name="autonum" type="checkbox"> Numerazione Automatica
		                        </label>
		                    </div>
		                    <div class="input-group col-md-8">
		                    	<input id="ddtObject_ddt_numeroProgressivo" type="number" data-mandatory="autonum" name="ddtObject.ddt.numeroProgressivo" class="form-control col-md-3 col-sm-4 col-xs-4" value='<s:property value="ddtObject.ddt.numeroProgressivo"/>' />
		                        <span class="input-group-addon">/</span>
		                        <input id="ddtObject_ddt_annoContabile" type="number" name="ddtObject.ddt.annoContabile" class="form-control col-md-3 col-sm-4 col-xs-4" value='<s:property value="ddtObject.ddt.annoContabile"/>' />
		                    </div>
		                </div>                
		
		                <div class="col-md-3 col-sm-5 pull-right">
		                    <label class="control-label">
		                        Data:
		                    </label>
		                    <div class="form-group">
		                   		<input type="date" data-mandatory="1" id="ddtObject_ddt_data" name="ddtObject.ddt.data" value="<s:property value="ddtObject.ddt.data"/>" class="form-control" /> 
		                    </div>
		                </div>
		            </div>
		        </div>
		        <div class="row top-buffer">
		        	<div class="col-md-6">
		        		<input type="hidden" name="ddtObject.ddt.idCliente" id="ddtObject_ddt_idCliente" value="<s:property value="ddtObject.ddt.idCliente" />" data-mandatory="1" />
			            <input type="hidden" name="ddtObject.ddt.cliente.rs" id="ddtObject_ddt_cliente_rs" value="<s:property value="ddtObject.ddt.cliente.rs"/>" />
			            <div class="panel panel-default col-sm-10" id="cliente">	           
 			            	<div class="panel-body"> 
			            		<s:property value="ddtObject.ddt.cliente.rs"/> 
 			            	</div> 
 			            </div>	             
 			            <div class="col-sm-2">
 			            	<button class="btn btn-block btn-default" onclick="getClienti()" type="button">Trova</button>
 			            </div> 

						<!-- 
						<s:select  label="Cliente" 
							list="ddtObject.ddt.listClienti" emptyOption="true" listKey="id" listValue="rs" name="codiceCliente" id="codiceCliente" cssClass="form-control"/>
							-->
						<!-- 
						<select class="form-control" name="codiceCliente" id="codiceCliente"></select>
						-->
		            </div>
		            <div class="col-md-6">
		            	<input type="hidden" name="ddtObject.ddt.idPuntoConsegna" id="ddtObject_ddt_idPuntoConsegna" value="<s:property value="ddtObject.ddt.idPuntoConsegna" />" data-mandatory="1" />
		            	<input type="hidden" id="index_puntoConsegna" value="-1" />
			            <div class="panel panel-default col-sm-10" id="destinazione">
				            <div class="panel-body">
				            	<s:property value="ddtObject.ddt.puntoConsegna.nome"/> <br />
				            	<s:property value="ddtObject.ddt.puntoConsegna.cap"/> - <s:property value="ddtObject.ddt.puntoConsegna.indirizzo"/> <br />
				            	<s:property value="ddtObject.ddt.puntoConsegna.localita"/> - <s:property value="ddtObject.ddt.puntoConsegna.prov"/>
			            	</div>
			            </div>
			            <div class="col-sm-2">
			                <button class="btn btn-block btn-default" onclick="getPuntoConsegna('prev')" type="button">Prec</button>
			                <button class="btn btn-block btn-default" onclick="getPuntoConsegna('next')" type="button">Suc</button>
			            </div>
		            </div>
					<div id="clientilist" class="col-md-12 hide"></div>
		        </div>
				<div class="row">
					<div class="col-md-6 list-group" id="destinazione2">
						<!--
						<ul class="dest-list">
							<li>First item</li>
							<li>Second item</li>
							<li>Third item</li>
						</ul>
						-->
						<!--
						<div class="list-group">
							<a href="#" class="list-group-item">First item</a>
							<a href="#" class="list-group-item">Second item</a>
							<a href="#" class="list-group-item">Third item</a>
						</div>
						-->
					</div>
				</div>
				
				
		    </fieldset>
		    <fieldset class="dettaglio top-buffer">
		        <legend class="row">Dettaglio</legend>
		        <div class="row">
		        	<input type="hidden" id="prodottiRimossi" name="prodottiRimossi" />
		            <table class="table table-responsive">
		                <thead>
		                    <tr>
		                    	<th></th>
		                        <th>Codice</th>
		                        <th>Descrizione</th>
		                        <th>Qtà</th>
		                        <th>P.</th>
		                        <th>P.E.</th>
		                        <th>Prezzo</th>
		                        <th>Sc.</th>
		                        <th>Iva</th>
		                        <th>UM</th>
		                        <th>Lotto</th>
		                    </tr>
		                </thead>
		                <tbody id="prodotti_rows">
		                <% int i = 0;%>
		                <s:iterator value="ddtObject.art">
		                    <tr id='dett_<s:property value="id" />articolo_<s:property value="idArticolo" />'>
		                    	<td>
		                    		<input onclick="selectArticle()" type="radio" id="idArticoloP" name="idArticoloP" value="<s:property value="idArticolo" />">
		                    		<input type="hidden" name="ddtObject.art[<%=i %>].idArticolo" id="ddtObject_art[<%=i %>]_idArticolo" value="<s:property value="idArticolo" />" />
		                    		<input type="hidden" name="ddtObject.art[<%=i %>].id" id="ddtObject_art[<%=i %>]_id" value="<s:property value="id" />" />
		                    	</td>
		                    	<td>
		                    		<s:property value="codiceArticolo" />
		                    		<input type="hidden" name="ddtObject.art[<%=i %>].codiceArticolo" id="ddtObject_art[<%=i %>]_codiceArticolo" value="<s:property value="codiceArticolo" />" />
		                    	</td>
		                    	<td>
		                    		<s:property value="descrizioneArticolo" />
		                    		<input type="hidden" name="ddtObject.art[<%=i %>].descrizioneArticolo" id="ddtObject_art[<%=i %>]_descrizioneArticolo" value="<s:property value="descrizioneArticolo" />" />
		                    	</td>
		                    	<td>
		                    		<s:property value="qta" />
		                    		<input type="hidden" name="ddtObject.art[<%=i %>].qta" id="ddtObject_art[<%=i %>]_qta" value="<s:property value="qta" />" />
		                    	</td>
		                    	<td>
		                    		<s:property value="pezzi" />
		                    		<input type="hidden" name="ddtObject.art[<%=i %>].pezzi" id="ddtObject_art[<%=i %>]_pezzi" value="<s:property value="pezzi" />" />
		                    	</td>
		                    	<td>
		                    		<s:property value="pezziEvasi" />
		                    		<input type="hidden" name="ddtObject.art[<%=i %>].pezziEvasi" id="ddtObject_art[<%=i %>]_pezziEvasi" value="<s:property value="pezziEvasi" />" />
		                    	</td>
		                    	<td>
		                    		<s:property value="prezzo" />
		                    		<input type="hidden" name="ddtObject.art[<%=i %>].prezzo" id="ddtObject_art[<%=i %>]_prezzo" value="<s:property value="prezzo" />" />
		                    	</td>
		                    	<td>
		                    		<s:property value="sconto" />
		                    		<input type="hidden" name="ddtObject.art[<%=i %>].sconto" id="ddtObject_art[<%=i %>]_sconto" value="<s:property value="sconto" />" />
		                    	</td>
		                    	<td>
		                    		<s:property value="iva" />
		                    		<input type="hidden" name="ddtObject.art[<%=i %>].iva" id="ddtObject_art[<%=i %>]_iva" value="<s:property value="iva" />" />
		                    	</td>
		                    	<td>
		                    		<s:property value="um" />
		                    		<input type="hidden" name="ddtObject.art[<%=i %>].um" id="ddtObject_art[<%=i %>]_um" value="<s:property value="um" />" />
		                    	</td>
		                    	<td>
		                    		<input type="hidden" name="ddtObject.art[<%=i %>].lotto" id="ddtObject_art[<%=i %>]_lotto" value="<s:property value="lotto" />" />
		                    		<s:property value="lotto" />
		                    	</td>
		                    </tr>
		                    
		                    <%i++; %>
	                    </s:iterator>
		                </tbody>
		            </table>
		        </div>
		        <div class="row">
					<input type="hidden" id="modOn" value="0" />
		            <div class="form-group">
		                <div class="row">
		                    <div class="col-xs-2 col-sm-2">
		                        <label class="control-label">Cerca</label>
		                        <select class="form-control" name="tipoRicerca" id="tipoRicerca">
		                            <option value="1">cod. barre</option>
		                            <option value="2">EUR</option>
		                        </select>
		                    </div>
		
		                    <div class="col-xs-2 col-sm-2">
		                        <label class="control-label">Codice</label>
<!-- 		                        <div class="input-group"> -->
		                       		<input type="hidden" data-mandatoryArt="1" name="idProdotto" id="idProdotto" />
		                       		<select class="form-control col-md-12" data-enable='new-article' disabled="disabled" id="codiceProdottoSel" name="codiceProdottoSel">
		                       		</select>
		                       		<input type="hidden" data-mandatoryArt="1" name="codiceProdotto" id="codiceProdotto">
<!-- 		                       		  -->
<!-- 		                            <input type="text" data-mandatoryArt="1" class="form-control" disabled="disabled" data-enable="new-article" name="codiceProdotto" id="codiceProdotto"> -->
<!-- 		                            <div class="input-group-btn"> -->
<!-- 		                                <button type="button" onclick="getArticoli()" data-enable="new-article" disabled="disabled" class="btn btn-default">...</button> -->
<!-- 		                            </div> -->
<!-- 		                        </div>	                                                -->
		                    </div>
<!-- 		                    <div class="visible-xs" style="padding-left:10px;"></div> -->
		                    <div class="col-xs-2 col-sm-2">
		                        <label class="control-label">Q.tà</label>
		                        <input type="number" data-mandatoryArt="1" class="form-control" data-enable="new-article" disabled="disabled"  name="qtaProdotto" id="qtaProdotto">
		                    </div>
		                    <div class="col-xs-2 col-sm-2">
		                        <label class="control-label">Pezzi</label>
		                        <input type="number" data-mandatoryArt="1" class="form-control" data-enable="new-article" disabled="disabled"  name="pezziProdotto" id="pezziProdotto">
		                        <input type="hidden" name="pezziEvasiProdotto" id="pezziEvasiProdotto" value="0" />
		                    </div>
		                    <div class="col-xs-2 col-sm-2">
		                        <label class="control-label">Prezzo</label>
		                        <input type="hidden" id="prezzoOriginaleProdotto" />
		                        <input type="number" data-mandatoryArt="1" step="0.05" class="form-control" data-enable="new-article" disabled="disabled" name="prezzoProdotto" id="prezzoProdotto">
		                    </div>
		                    <div class="col-xs-2 col-sm-1">
		                        <label class="control-label">Sconto</label>
		                        <input type="number" step="0.50" class="form-control" onchange="applicaScontoProdotto()" data-enable="new-article" disabled="disabled" name="scontoProdotto" id="scontoProdotto">
		                    </div>
		                    <div id="articolilist" class="top-buffer col-md-12 hide"></div>    
		                </div>
		            </div>	
		        </div>
		        <div class="row">
		            <div class="form-group">
		                <div class="row">
		                	<div class="col-xs-4 col-sm-3">
		                        <label class="control-label">Descrizione</label><br/><span data-enable='new-article' id="txtdescrizioneProdotto"></span>
		                        <input type="hidden"  name="descrizioneProdotto" id="descrizioneProdotto">
		                    </div>
		                    <div class="col-xs-2 col-sm-1">
		                        <label class="control-label">UM</label><br/><span data-enable='new-article' id="txtumProdotto"></span>
		                        <input type="hidden" name="umProdotto" id="umProdotto">		                        
		                    </div>		                    
		                    <div class="col-xs-2 col-sm-2">
		                        <label class="control-label">Iva</label>	
		                        <select name="ddtObject.ddt.iva" id="ddtObject_ddt_iva" disabled="disabled" class="form-control">
								 <s:iterator value="ddtObject.ddt.getIvas()">
								    <option value="<s:property value="valore"/>"><s:property value="valore"/> %</option>
								 </s:iterator>
								</select>      
		                    </div>
		                    <div class="col-xs-12 col-sm-6">
		                        <label class="control-label">Lotto</label>
		                        <input type="text" data-mandatoryArt="1" data-enable="new-article" class="form-control" disabled="disabled" name="lottoProdotto" id="lottoProdotto">
		                    </div>
		                </div>
		            </div>	
		        </div>
		
<!-- 		        <div class="row"> -->
<!-- 		            <div class="form-group"> -->
<!-- 		                <div class="row"> -->
<!-- 		                    <div class="col-xs-12 col-sm-6"> -->
<!-- 		                        <label class="control-label">Lotto</label> -->
<!-- 		                        <input type="text" data-mandatoryArt="1" data-enable="new-article" class="form-control" disabled="disabled" name="lottoProdotto" id="lottoProdotto"> -->
<!-- 		                    </div> -->
<!-- 		                </div> -->
<!-- 		            </div> -->
<!-- 		        </div> -->
		        <div class="row">
		            <div class="form-group">
		                <div class="row">
		                    <div class="col-xs-6 col-sm-2">
		                        <label class="control-label hidden-xs">&nbsp;</label>
		                        <button type="button" onclick="enableNewArticle()" class="btn btn-block btn-default">Nuovo articolo</button>
		                    </div>
		                    <div class="col-xs-6 col-sm-2">
		                        <label class="control-label hidden-xs">&nbsp;</label>
		                        <button type="button" onclick="removeArticle()" class="btn  btn-block btn-default">Cancella articolo</button>
		                    </div>
		                    <div class="col-xs-12 visible-xs">
		                        <label class="control-label">&nbsp;</label>
		                    </div>
		
		                    <div class="col-xs-6 col-sm-4">                            
		                        <div class="form-group ">
		                            <label>Totale Euro:</label>	                            
		                            <input type="number" data-mandatory="1" class="form-control" readonly="readonly"  name="ddtObject.ddt.totaleImponibile" id="ddtObject_ddt_totaleImponibile" value="<s:property value="ddtObject.ddt.totaleImponibile" />" />
		                        </div>
		                    </div>
		                    
		                    <div class="col-xs-2 col-sm-2">
		                        <label class="control-label">&nbsp;</label>
		                        <button onclick="addArticle()" type="button" class="btn btn-block btn-default">Ok</button>
		                    </div>
		                    <div class="col-xs-4 col-sm-2">
		                        <label class="control-label">&nbsp;</label>
		                        <button type="button" onclick="disableNewArticleSelect2()" class="btn btn-block btn-default">Annulla</button>
		                    </div>
		                </div>
		            </div>
		        </div>
		        
		
		    </fieldset>
		    <fieldset>
		        <legend class="row">Spedizione</legend>
		        <div class="row">
		            <div class="form-group">
		                <div class="row">
		                    <div class="col-xs-6 col-sm-6">
		                        <label class="control-label">N.Colli:</label>
		                        <input type="number" data-mandatory="1" class="form-control" name="ddtObject.ddt.colli" id="ddtObject_ddt_colli" value="<s:property value="ddtObject.ddt.colli" />" />
		                    </div>
		                    <div class="col-xs-6 col-sm-3 pull-right">
		                        <label class="control-label">Data trasporto:</label>
		                        <input type="date" data-mandatory="1" value="<s:property value="ddtObject.ddt.dataTrasporto" />" class="form-control" name="ddtObject.ddt.dataTrasporto" id="ddtObject_ddt_dataTrasporto" />
		                    </div>
		                </div>
		            </div>
		            <div class="form-group">
		                <div class="row">
		                    <div class="col-xs-6 col-sm-3">	                    
		                        <label class="control-label">Trasporto a cura del:</label>
		                        <s:select cssClass="form-control" name="ddtObject.ddt.trasporto" list="#{'mittente':'mittente', 'destinatario':'destinatario'}" value="ddtObject.ddt.trasporto" required="false" />
		                    </div>
		                    <div class="col-xs-6 col-sm-3">	                    
		                        <label class="control-label">Autista:</label>
		                        <input type="hidden" id="ddtObject_ddt_idAutista" name="ddtObject.ddt.idAutista" value="<s:property value="ddtObject.ddt.idAutista"/>" />
		                        <select onchange="setAutista()" name="ddtObject.ddt.autista" id="ddtObject_ddt_autista" class="form-control">
								 <s:iterator value="ddtObject.ddt.getAutistis()">
								    <option value="<s:property value="id"/>"><s:property value="nome"/></option>
								 </s:iterator>
								</select>    
		                    </div>
		                    <div class="col-xs-6 col-sm-3 pull-right">
		                        <label class="control-label">Ora trasporto:</label>
		                        <input type="time" data-mandatory="1" class="form-control" value="<s:property value="ddtObject.ddt.oraTrasporto" />" name="ddtObject.ddt.oraTrasporto" id="ddtObject_ddt_oraTrasporto" />
		                    </div>
		                </div>
		            </div>
		        </div>
		    </fieldset>	    
		</div>
		<div class="clearfix"></div>
	</div>		
	<div class="modal-footer">
	
		<div class="row hide" id="message">
			<p class="bg-success" id="message_success">...</p>
			<p class="bg-danger" id="message_error">...</p>
		</div>
	   <div class="row">
	       <div class="form-group">
	           <div class="row">
	               <div class="col-xs-4 col-sm-2">
	                   <button onclick="openModal('0')" id="newDdt" type="button" class="btn btn-block btn-lg btn-default">Nuovo DDT</button>
	               </div>
	               <div class="col-xs-4 col-sm-2">
	                   <button onclick="saveDdtNew()" id="saveNewDdt" type="button" disabled="disabled" class="btn btn-lg  btn-block btn-default">Salva/Nuovo</button>
	               </div>
	               <div class="col-xs-4 col-sm-3">
	                   <button onclick="saveAndPrintDdtNew()" id="saveNewAndPrintDdt" disabled="disabled" type="button" class="btn btn-lg  btn-block btn-default">Salva/Stampa/Nuovo</button>
	               </div>
	
	               <div class="col-xs-6 col-sm-2 pull-right">
	                   <label class="control-label  visible-xs">&nbsp;</label>
	                   <button onclick="printDdtMod()" id="printDdt" type="button" class="btn btn-lg btn-block btn-default">Stampa</button>
	               </div>
	
	               <div class="col-xs-6 col-sm-2 pull-right">
	                   <label class="control-label visible-xs">&nbsp;</label>
	                   <button onclick="saveDdtMod()" id="saveDdt" type="button" class="btn btn-lg btn-block btn-default">Modifica</button>
	               </div>
	           </div>
			</div>
		   
			<!-- Editable table -->
			<div class="card">    
			  <h3 class="card-header text-center font-weight-bold text-uppercase py-4">Editable table</h3>    
			  <div class="card-body">        
				<div id="table" class="table-editable">            
				  <span class="table-add float-right mb-3 mr-2">
					<a href="#!" class="text-success">
					  <i class="fa fa-plus fa-2x" aria-hidden="true"></i></a>
				  </span>            
				  <table class="table table-bordered table-responsive-md table-striped text-center">                
					<tr>                    
						<th class="text-center">Codice</th>                    
						<th class="text-center">Descrizione</th>                    
						<th class="text-center">Qta</th>                    
						<th class="text-center">Pezzi</th>                    
						<th class="text-center">Prezzo</th>                    
						<th class="text-center">Sconto</th>
						<th class="text-center">Iva</th> 
						<th class="text-center">UdM</th>
						<th class="text-center">Lotto</th>
						<th class="text-center">Sort</th>                      
						<th class="text-center">Remove</th>                
					</tr>           
				  </table>        
				</div>    
			  </div>
			</div>
			<!-- Editable table -->

			<!-- SCRIPTS -->
			<!-- JQuery -->
			<!--<script type="text/javascript" src="mdb/js/jquery-3.3.1.min.js"></script>-->
			<!-- Bootstrap tooltips -->
			<script type="text/javascript" src="materialDesignBootstrap/js/popper.min.js"></script>
			<!-- Bootstrap core JavaScript -->
			<!--<script type="text/javascript" src="materialDesignBootstrap/js/bootstrap.min.js"></script>-->
			<!-- MDB core JavaScript -->
			<script type="text/javascript" src="materialDesignBootstrap/js/mdb.min.js"></script>

			<!-- Custom -->
			<script id="emptyRow" type="text/plain">
			  <tr class="hide">                    
				<td class="pt-3-half" contenteditable="true"></td>                    
				<td class="pt-3-half" contenteditable="true"></td>                    
				<td class="pt-3-half" contenteditable="true"></td>                    
				<td class="pt-3-half" contenteditable="true"></td>                    
				<td class="pt-3-half" contenteditable="true"></td>
				<td class="pt-3-half" contenteditable="true"></td> 
				<td class="pt-3-half" contenteditable="true"></td>
				<td class="pt-3-half" contenteditable="true"></td>
				<td class="pt-3-half" contenteditable="true"></td>                     
				<td class="pt-3-half">                        
				  <span class="table-up">
					<a href="#!" class="indigo-text">
					  <i class="fa fa-long-arrow-up" aria-hidden="true"></i></a>
				  </span>                        
				  <span class="table-down">
					<a href="#!" class="indigo-text">
					  <i class="fa fa-long-arrow-down" aria-hidden="true"></i></a>
				  </span>                    </td>                    <td>                        
				  <span class="table-remove">
					<button type="button" class="btn btn-danger btn-rounded btn-sm my-0" onclick='removeRow.call(this,event);'>Remove
					</button>
				  </span>                    </td>                
			  </tr>
			</script>
			
			<script type="text/javascript" src="materialDesignBootstrap/js/extras.js"></script>
			  
	   </div>
	</div>
</form>

<script>
$(document).ready(function(){
	tableColorRows();

	selectAutista();
	
	//cliente #######################################
	$('select[name="codiceCliente"]').select2({
		minimumInputLength: 2,
	    tags: [],
	    tabindex: false,
	    ajax: {
	        url: "/ContaWeb/clienteSelect2.do",
	        dataType: 'json',
	        type: "GET",
	        quietMillis: 50,
	        data: function (term) {
	        	console.log(term.term);
	            return {
	                term: term.term
	            };
	        },
	        results: function (data) {
	        	console.log(data);
	            return {
	                results: $.map(data, function (item) {
	                	console.log(item);
	                    return {
	                        text: item.text,
	                        id: item.id
	                    }
	                })
	            };
	        }
	    }
	})
	.append('<option value="'+ $("#ddtObject_ddt_idCliente").val() +'">'+ $("#ddtObject_ddt_cliente_rs").val() +'</option>')
	.trigger('change');
	 //selected="selected"
	
	$('select[name="codiceCliente"]').on("change", function(){
			var id = $('select[name="codiceCliente"] option:last').val();
			console.log(id);
			$("#ddtObject_ddt_idCliente").val(id);
			
			getPuntoConsegna('', id);
		});
	 
	//articolo #######################################
	$('select[name="codiceProdottoSel"]').select2({
		minimumInputLength: 2,
	    tags: [],
	    tabindex: false,
	    ajax: {
	        url: "/ContaWeb/articoloSelect2.do",
	        dataType: 'json',
	        type: "GET",
	        quietMillis: 50,
	        data: function (term) {
	        	console.log(term.term);
	            return {
	                term: term.term
	            };
	        },
	        results: function (data) {
	        	console.log(data);
	            return {
	                results: $.map(data, function (item) {
	                	console.log(item);
	                    return {
	                        text: item.text,
	                        id: item.id
	                    }
	                })
	            };
	        }
	    }
	})
	//.trigger('change');
	 //selected="selected"
	
	$('select[name="codiceProdottoSel"]').on("change", function(){
			var id = $('select[name="codiceProdottoSel"] option:last').val();
			console.log(id);
			$("#idProdotto").val(id);
			
			setArticoloSelect2();
		});
	 
	$('#modal').on('hidden.bs.modal', function () {
		location.reload();
	})
});
</script>