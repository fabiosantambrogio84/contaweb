// dbgActive: 0 - [None] Disabled 
//            1 - [High] High priority messages only
//            2 - [H+L]  High and Low priority messages only
//            3 - [All]  High, Low and Old messages 
var dgbActive = 1; 
var dbgString = 'Debug mode is active - ';
var breakline = "<br />";

function openModal(id) {
	
	$('#modal').modal('show');		
	$('#modal .modal-content').load("/ContaWeb_v1.0.1/ddt.do?idDDT=" + id, function(){

		if(id === "0"){
			openNewDdt();
		}
	});
}

function newDdtWithLoad(){
	$('#modal .modal-content').load("/ContaWeb_v1.0.1/ddt.do?idDDT=0", function(){
		newDdt();
	});
}

//#### BEGIN CLIENTE ##########################################
function getClienti(){
	
	$("#clientilist").addClass("show").removeClass("hide");
	$('#clientilist').load("/ContaWeb_v1.0.1/clientiFindList.do");
}

function setCliente(){

	var id = $("input[name='idCliente']:checked").val();
	$.ajax({
		  method: "GET",
		  contentType: "application/json",
		  dataType: "json",
		  url: "/ContaWeb_v1.0.1/cliente.do?id=" + id
		})
		.done(function( data ) {

			console.log( "cliente: " + JSON.stringify(data) );
			
			$("#cliente .panel-body").html(data.rs);
			$("#ddtObject_ddt_idCliente").val(data.id);
			
			getPuntoConsegna('', id);
		});
	
	closeGetClienti();
}

function getPuntoConsegna(type, id)
{
	var index = 0;
	if(typeof id === 'undefined' 
		|| id === '')
	{
		id = $("#ddtObject_ddt_idCliente").val();
		index = parseInt($("#index_puntoConsegna").val());
		if(type == "next")
			index++;
		else
			index--;
		
		$("#index_puntoConsegna").val(index);
	}
	
	if(index < 0 )
		index = 0;
	
	$.ajax({
		  method: "GET",
		  contentType: "application/json",
		  dataType: "json",
		  url: "/ContaWeb_v1.0.1/clienteDestinazione.do?id=" + id + "&index=" + index
		})
		.done(function( data ) {
			$("#ddtObject_ddt_idPuntoConsegna").val(data.id);
			var destinazione = data.nome + breakline + data.cap + " - " + data.indirizzo + breakline + data.localita + " - " + data.prov;
			$("#destinazione .panel-body").html(destinazione);
			
			console.log( "destinazione: " + JSON.stringify(data) );
		});
}

function closeGetClienti(){
	
	$("#clientilist").addClass("hide").removeClass("show");
}
//#### END CLIENTE ##########################################

//#### BEGIN ARTICLE ##########################################
function tableColorRows(){
	$.each($("#prodotti_rows tr"), function(){
		var id = $(this).attr("id");
						
		var pezzi = parseInt($(this).find("[name$='.pezzi']").val());
		var pezziEvasi = parseInt($(this).find("[name$='.pezziEvasi']").val());
		var lotto = parseInt($(this).find("[name$='.lotto']").val());
		
		if(lotto === "")
			$(this).attr("style", "background-color:red;");
		else if(pezziEvasi < 0)
			$(this).attr("style", "background-color:green;");
		else if(pezziEvasi > 0)
			$(this).attr("style", "background-color:yellow;");
		else
			$(this).attr("style", "");
	})
}

function enableNewArticle(){
	$("#modOn").val("0");
	$("input[data-enable='new-article']").prop("disabled", false);
	$("button[data-enable='new-article']").prop("disabled", false);
	$("select[data-enable='new-article']").prop("disabled", false);
	$("input[data-enable='new-article']").val("");
	$("span[data-enable='new-article']").html("");
}

function disableNewArticle(){
	$("input[data-enable='new-article']").prop("disabled", true);
	$("input[data-enable='new-article']").val("");
	$("button[data-enable='new-article']").prop("disabled", true);
	$("select[data-enable='new-article']").prop("disabled", true);
	
	closeGetArticoli();
}

function disableNewArticleSelect2(){
	$("input[data-enable='new-article']").prop("disabled", true);
	$("input[data-enable='new-article']").val("");
	$("button[data-enable='new-article']").prop("disabled", true);
	$("select[data-enable='new-article']").prop("disabled", true);
	$("span[data-enable='new-article']").html("");
	$("input[data-enable='new-article']").val("");
}

function selectArticle(){
	setArticoloMod();
}

function addArticle(){

	var isUpdate = $("#modOn").val() != "0";	
	if(isUpdate){
		removeArticle();
		$("#modOn").val("0");	
		addArticle();
		$("#modOn").val("1");
		
		return;
	}
	
	if(checkAddArticle()){
		
		var prezzo = parseFloat($("#prezzoProdotto").val());
		var prezzoOrg = parseFloat($("#prezzoOriginaleProdotto").val())
		var lotto = $("#lottoProdotto").val();
		var descrizione = $("#descrizioneProdotto").val();
		var um = $("#umProdotto").val();
		var pezzi = $("#pezziProdotto").val();
		var sconto = $("#scontoProdotto").val();
		var qta = parseFloat($("#qtaProdotto").val());
		var iva = $("#ddtObject_ddt_iva").val();
		var idarticolo = $("#idProdotto").val();
		var codice = $("#codiceProdotto").val();
		var pezziEvasi = $("#pezziEvasiProdotto").val();
		
		var isOk = true;
		
		if(sconto == "")
			sconto = 0.00;
		
		if(lotto == ""){
			alert("Inserire il lotto");
			isOk = false;
		}
		else if(isNaN(prezzo)){
			alert("Inserire un prezzo valido");
			isOk = false;
		}
		else if(isNaN(qta)){
			alert("Inserire una qta valida");
			isOk = false;
		}
		else if(isNaN(pezzi)){
			alert("Inserire valore pezzi valido");
			isOk = false;
		}
		
		if(isOk){
			var findArticle = false;
			var findQta = qta;
			$.each($("#prodotti_rows tr"), function(){
				var id = $(this).attr("id");
								
				var lotto_tmp = $(this).find("[name$='.lotto']").val();
				if(id.match("/articolo_" + idarticolo + "$/")
						&& lotto == lotto_tmp){
					findArticle = true;
				
					var qtaCol = $(this).find("[name$='.qta']");
					var qta_tmp = parseFloat(qtaCol.val());
					
					findQta = findQta + qta_tmp;			
					qtaCol.html(findQta);
				}
				
				i++;
			})
				
			var i = $("#prodotti_rows tr").length;
			
			if(!findArticle)
			{
				$("#prodotti_rows").append('<tr id="dett_'+ 0 +'articolo_'+idarticolo+' />"> \
			    	<td>	\
			    		<input type="radio" onclick="selectArticle()" id="idArticoloP" name="idArticoloP" value="'+idarticolo+'"> \
		    			<input type="hidden" id="ddtObject_art['+ i +']_idArticolo" name="ddtObject.art['+ i +'].idArticolo" value="'+ idarticolo +'" /> \
		    			<input type="hidden" id="ddtObject_art['+ i +']_id" name="ddtObject.art['+ i +'].id" value="0" /> \
			    	</td> \
			    	<td> \
			    		'+ codice +' \
			    		<input type="hidden" id="ddtObject_art['+ i +']_codiceArticolo" name="ddtObject.art['+ i +'].codiceArticolo" value="'+ codice +'" /> \
			    	</td> \
			    	<td> \
			    		'+ descrizione +' \
			    		<input type="hidden" id="ddtObject_art['+ i +']_descrizioneArticolo" name="ddtObject.art['+ i +'].descrizioneArticolo" value="'+ descrizione +'" /> \
			    	</td> \
			    	<td> \
			    		'+ findQta +' \
			    		<input type="hidden" id="ddtObject_art['+ i +']_qta" name="ddtObject.art['+ i +'].qta" value="'+ findQta +'" /> \
			    	</td> \
			    	<td> \
			    		'+ pezzi +' \
			    		<input type="hidden" id="ddtObject_art['+ i +']_pezzi" name="ddtObject.art['+ i +'].pezzi" value="'+ pezzi +'" /> \
			    	</td> \
			    	<td> \
				    	'+ pezziEvasi +' \
			    		<input type="hidden" id="ddtObject_art['+ i +']_pezziEvasi" name="ddtObject.art['+ i +'].pezziEvasi" value="'+ pezziEvasi +'" /> \
			    	</td> \
			    	<td> \
			    		'+ prezzo +' \
			    		<input type="hidden" id="ddtObject_art['+ i +']_prezzo" name="ddtObject.art['+ i +'].prezzo" value="'+ prezzo +'" /> \
			    	</td> \
			    	<td> \
			    		'+ sconto +' \
			    		<input type="hidden" id="ddtObject_art['+ i +']_sconto" name="ddtObject.art['+ i +'].sconto" value="'+ sconto +'" /> \
			    	</td> \
			    	<td> \
			    		'+ iva +' \
			    		<input type="hidden" id="ddtObject_art['+ i +']_iva" name="ddtObject.art['+ i +'].iva" value="'+ iva +'" /> \
			    	</td> \
			    	<td> \
			    		'+ um +' \
			    		<input type="hidden" id="ddtObject_art['+ i +']_um" name="ddtObject.art['+ i +'].um" value="'+ um +'" /> \
			    	</td> \
			    	<td> \
			    		'+ lotto +' \
			    		<input type="hidden" id="ddtObject_art['+ i +']_lotto" name="ddtObject.art['+ i +'].lotto" value="'+ lotto +'" /> \
			    	</td> \
			    </tr>');
			}
			
			var tot = 0;
			if($("#ddtObject_ddt_totaleImponibile").val() !== ""){
				tot = parseFloat($("#ddtObject_ddt_totaleImponibile").val());
			}
			var intTotale = tot + (prezzo * findQta);
			$("#ddtObject_ddt_totaleImponibile").val(Math.round(intTotale * 100) / 100);
			
			disableNewArticle();
		}
	}
	else{
		alert("Attenzione alcuni campi obbligatori non sono stati compilati!");
	}
	
	tableColorRows();
}

function applicaScontoProdotto(){
	var strSale = $("#scontoProdotto").val();
    var strPrice = $("#prezzoOriginaleProdotto").val();
    var salePrice = 0;
    
    if (!isNaN(strSale)) {
    	var price = parseFloat(strPrice);
    	var sale = parseFloat(strSale);
    	salePrice = price;
    	    	
    	if(sale <= 100
			&& sale > 0) {
    		var num = price - (price * (sale / 100));
    		salePrice = Math.round(num * 100) / 100;
    	}
    	
    	$("#prezzoProdotto").val(salePrice);
    }
}

function getArticoli(){
	
	$("#articolilist").addClass("show").removeClass("hide");
	$('#articolilist').load("/ContaWeb_v1.0.1/articoliFindList.do");
}

function setArticolo(){
	
	var id = $("input[name='idArticolo']:checked").val();
	var idCliente = $("#ddtObject_ddt_idCliente").val();
	
	$.ajax({
		  method: "GET",
		  contentType: "application/json",
		  dataType: "json",
		  url: "/ContaWeb_v1.0.1/articolo.do?id=" + id + "&otherId=" + idCliente
		})
		.done(function( data ) {
			$("#descrizioneProdotto").val(data.descrizione);	
			$("#txtdescrizioneProdotto").html(data.descrizione);
			if(typeof data.pezziEvasi === 'undefined' 
				|| data.pezziEvasi === '')
				$("#pezziEvasiProdotto").val("0");
			else{
				$("#pezziEvasiProdotto").val(data.pezziEvasi);
			}
			
			$("#umProdotto").val(data.um);
			$("#txtumProdotto").html(data.um);
			$("#codiceProdotto").val(data.codiceArticolo);
			$("#idProdotto").val(id);
			$("#prezzoProdotto").val(data.prezzoConSconto);
			$("#prezzoOriginaleProdotto").val(data.prezzoConSconto);
			$("#ddtObject_ddt_iva option[value='"+ data.iva.valore +"']").prop("selected", true);
			$("#ddtObject_ddt_iva").prop("disabled", true);
			
			console.log( "destinazione: " + JSON.stringify(data) );
			
			closeGetArticoli();
		});
}

function setArticoloSelect2(){
	
	var id = $("#idProdotto").val();
	var idCliente = $("#ddtObject_ddt_idCliente").val();
	
	$.ajax({
		  method: "GET",
		  contentType: "application/json",
		  dataType: "json",
		  url: "/ContaWeb_v1.0.1/articolo.do?id=" + id + "&otherId=" + idCliente
		})
		.done(function( data ) {
			$("#descrizioneProdotto").val(data.descrizione);	
			$("#txtdescrizioneProdotto").html(data.descrizione);
			if(typeof data.pezziEvasi === 'undefined' 
				|| data.pezziEvasi === '')
				$("#pezziEvasiProdotto").val("0");
			else{
				$("#pezziEvasiProdotto").val(data.pezziEvasi);
			}
						
			$("#umProdotto").val(data.um);
			$("#txtumProdotto").html(data.um);
			$("#codiceProdotto").val(data.codiceArticolo);
			$("#idProdotto").val(id);
			$("#prezzoProdotto").val(data.prezzoConSconto);
			$("#prezzoOriginaleProdotto").val(data.prezzoConSconto);
			$("#ddtObject_ddt_iva option[value='"+ data.iva.valore +"']").prop("selected", true);
			$("#ddtObject_ddt_iva").prop("disabled", true);
			
			console.log( "destinazione: " + JSON.stringify(data) );
		});
}

function setArticoloMod(){
	
	$("#modOn").val("1");
	
	$("input[data-enable='new-article']").prop("disabled", false);
	$("button[data-enable='new-article']").prop("disabled", false);
	$("select[data-enable='new-article']").prop("disabled", false);
	
	var id = $("input[name='idArticoloP']:checked").val();

	var dett = $("input[name='idArticoloP']:checked").parent('td').parent('tr');
	var iddett = dett.find("[name$='.id']").val();	
	var descr = dett.find("[name$='.descrizioneArticolo']").val();
	var um = dett.find("[name$='.um']").val();
	var cod = dett.find("[name$='.codiceArticolo']").val();
	var price = dett.find("[name$='.prezzo']").val();
	var orgPrice = dett.find("[name$='.prezzo']").val();
	var iva = dett.find("[name$='.iva']").val();
	var pezzi = dett.find("[name$='.pezzi']").val();
	var sconto = dett.find("[name$='.sconto']").val();
	var lotto = dett.find("[name$='.lotto']").val();
	var qta = dett.find("[name$='.qta']").val();
	var pezziEvasi = dett.find("[name$='.pezziEvasi']").val();
	
	$("#descrizioneProdotto").val(descr);	
	$("#txtdescrizioneProdotto").html(descr);
	$("#umProdotto").val(um);
	$("#txtumProdotto").html(um);
	$("#codiceProdotto").val(cod);
	$("#qtaProdotto").val(qta);
	$("#idProdotto").val(id);
	$("#codiceProdotto").prop("disabled", true);
	$("#pezziProdotto").val(pezzi);
	$("#prezzoProdotto").val(price);
	$("#lottoProdotto").val(lotto);
	$("#scontoProdotto").val(sconto);
	$("#prezzoOriginaleProdotto").val(orgPrice);
	$("#ddtObject_ddt_iva option[value='"+ iva +"']").prop("selected", true);
	$("#ddtObject_ddt_iva").prop("disabled", true);
	$("#pezziEvasiProdotto").val(pezziEvasi);
}

function closeGetArticoli(){
	
	$("#articolilist").addClass("hide").removeClass("show");
}



function removeArticle() {
	var id = $("input[name='idArticoloP']:checked").val();
	var dett = $("input[name='idArticoloP']:checked").parent('td').parent('tr');
	var iddett = dett.find("[name$='.id']").val();
	var isUpdate = $("#modOn").val() != "0";
	if(!isUpdate) {
		var prodottiRimossi = $("#prodottiRimossi").val();
		
		if(prodottiRimossi != "")
			$("#prodottiRimossi").val(prodottiRimossi + "|" + id);
		else
			$("#prodottiRimossi").val(id);
		
	}
	var qta = parseFloat($("#dett_"+iddett+"articolo_" + id).find("[name$='.qta']").val());
	var prezzo = parseFloat($("#dett_"+iddett+"articolo_" + id).find("[name$='.prezzo']").val());
		
	var intTotale = parseFloat($("#ddtObject_ddt_totaleImponibile").val()) - (prezzo * qta);
	$("#ddtObject_ddt_totaleImponibile").val(Math.round(intTotale * 100) / 100);
	$("#dett_"+iddett+"articolo_" + id).remove();
	
	tableColorRows();
}

function checkAddArticle(){
	var isOk = true;
	
	$.each($("input[data-mandatoryArt]"), function(){
		var data = $(this).attr("data-mandatoryArt");
		var id = $(this).attr("id");
		var val = $(this).val();
		var input = $(this);
		
		if(!isNaN(data)){
			if(val == ""){
				input.attr("style", "border-color: #a94442");
				isOk = false;
			}
			else{
				input.attr("style", "");
			}
		}
		else{
			// nothing to do
		}
	});
	
	return isOk;
}

//#### END ARTICLE ##########################################

//#### BEGIN AUTISTA ##########################################
function selectAutista(){
	var idaut = $("#ddtObject_ddt_idAutista").val();
	
	if(typeof idaut !== 'undefined' 
		|| idaut !== ''){
		$("#ddtObject_ddt_autista option[value='"+idaut+"']").prop("selected", true);
	}
}

function setAutista(){
	var id = $("#ddtObject_ddt_autista option:selected").val();
	$("#ddtObject_ddt_idAutista").val(id);
}
//#### END AUTISTA ##########################################

//#### BEGIN ACTION ##########################################


function openNewDdt(){
	$("#autonum").prop("disabled", false);
	$("#autonum").prop("checked", true);
	$("#saveNewDdt").prop("disabled", false);
	$("#newDdt").prop("disabled", false);
	$("#saveNewAndPrintDdt").prop("disabled", false);
	
	$("#saveDdt").prop("disabled", true);
	$("#printDdt").prop("disabled", true);
	
	/*
	var d = new Date();
	var day = d.getDate();
	if(day < 10)
		day = "0"+day;
	
	var today = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + day;
	var time = d.getHours() + ":" + d.getMinutes();
	$("#ddtObject_ddt_oraTrasporto").val(time);
	$("#ddtObject_ddt_dataTrasporto").val(today);
	$("#ddtObject_ddt_data").val(today);
	*/
	
	var now = new Date();
	var day = ("0" + now.getDate()).slice(-2);
	var month = ("0" + (now.getMonth() + 1)).slice(-2);
	var today = now.getFullYear()+"-"+(month)+"-"+(day) ;		
	$("#ddtObject_ddt_data").val(today);
	
	enableNumAut();
}


function printDdtMod()
{
	var id = $("#ddt_id").id;
		
	$.ajax({
		  method: "GET",
		  contentType: "application/json",
		  dataType: "json",
		  url: "/ContaWeb_v1.0.1/printDdt.do?id=" + id
		})
		.done(function( data ) {			
			console.log( "Stampa ddt in corso: " + id );
		});
}


function saveDdtNew()
{
	if(checkSaveDdt()) {
		
		var serObj = $("#ddtform").serialize();
		$.ajax({
			  method: "POST",
			  dataType: "json",
			  url: "/ContaWeb_v1.0.1/salvaDDT.do",
			  data: serObj
			})
			.done(function( data ) {			
				console.log( data );
				
				if(data.success === true){				
					$("#message_error").addClass("hide");
					$("#message_success").html("<strong>" + data.message + "</strong>");
					
					setTimeout(function(){ newDdtWithLoad(); }, 3000);
				}
				else{
					$("#message_success").addClass("hide");
					$("#message_error").html("<strong>" + data.message + "</strong>");
				}
				
				$("#message").removeClass("hide");			
			});
	}
	else {
		alert("Attenzione alcuni campi obbligatori non sono stati compilati!");
	}
}

function saveAndPrintDdtNew(){
if(checkSaveDdt()) {
		
		var serObj = $("#ddtform").serialize();
		$.ajax({
			  method: "POST",
			  dataType: "json",
			  url: "/ContaWeb_v1.0.1/salvaDDT.do",
			  data: serObj
			})
			.done(function( data ) {			
				console.log( data );
				
				if(data.success === true){				
					$("#message_error").addClass("hide");
					$("#message_success").html("<strong>" + data.message + "</strong>");
					
					$.ajax({
						  method: "GET",
						  contentType: "application/json",
						  dataType: "json",
						  url: "/ContaWeb_v1.0.1/printDdt.do?id=" + data.id
						})
						.done(function( data ) {			
							console.log( "Stampa ddt in corso: " + data.id );
							setTimeout(function(){ newDdtWithLoad(); }, 3000);
						});
				}
				else{
					$("#message_success").addClass("hide");
					$("#message_error").html("<strong>" + data.message + "</strong>");
				}
				
				$("#message").removeClass("hide");			
			});
	}
	else {
		alert("Attenzione alcuni campi obbligatori non sono stati compilati!");
	}
}

function saveDdtMod()
{
	if(checkSaveDdt()) {
		
		var serObj = $("#ddtform").serialize();
		$.ajax({
			  method: "POST",
			  dataType: "json",
			  url: "/ContaWeb_v1.0.1/salvaDDT.do",
			  data: serObj
			})
			.done(function( data ) {			
				console.log( data );
				
				if(data.success === true){				
					$("#message_error").addClass("hide");
					$("#message_success").html("<strong>" + data.message + "</strong>");
				}
				else{
					$("#message_success").addClass("hide");
					$("#message_error").html("<strong>" + data.message + "</strong>");
				}
				
				$("#message").removeClass("hide");			
			});
	}
	else {
		alert("Attenzione alcuni campi obbligatori non sono stati compilati!");
	}
}

function checkSaveDdt(){
	var isOk = true;
	
	$.each($("input[data-mandatory]"), function(){
		var data = $(this).attr("data-mandatory");
		var id = $(this).attr("id");
		var val = $(this).val();
		var input = $(this);
		var type = input.attr("type");
		
		if(!isNaN(data)){
			if(val == ""){
				if(type !== "hidden"){
					input.attr("style", "border-color: #a94442");
				}
				else{
					input.after("<span id='mandatory_index_"+ id +"' style='color: #a94442; font-size:16px; font-weight: bold;'>*</span>")
				}
				isOk = false;
			}
			else{
				input.attr("style", "");
				if(type === "hidden"){
					$("#mandatory_index_"+ id).remove();
				}
			}
		}
		else{
			if(id == "ddtObject_ddt_numeroProgressivo"){		
				if(!$('#autonum').prop('checked')
				&& val == ""){
					input.attr("style", "border-color: #a94442");
					isOk = false;
				}
				else{
					input.attr("style", "");
				}
			}
		}
	});
	
	return isOk;
}

//#### END ACTION ##########################################

function apriFinestra(link,action,id) {
	var url = link + '?action=' + action + '&id=' + id;
	window.open(url, 'subwnd', 'width=655,height=630,toolbar=0,resizable=0');
	return false;
}

function confermaCancellazione(id, link) {
	document.getElementById("messageBox").style.backgroundColor = "green";
	document.getElementById("messageBox").textContent= 'confermaCancellazione - Start';		
	
	var row = document.getElementById('tr_' + id);
	row.style.backgroundColor = "#FF0000";
	answer = confirm('Sei sicuro di voler cancellare la riga selezionata?');
	if (answer != "0") {
		document.getElementById("messageBox").style.backgroundColor = "yellow";
		document.getElementById("messageBox").textContent= 'Cancellazione in corso...';		
		//AJAX call		
		link = link + "&id=" + id;
		dojo.io.bind({
			url: link,
			method: "get",
			handle: function(type,data,evt) {
	         if ((type=='load') && (data != 'error')) {
	        	 document.getElementById("messageBox").style.backgroundColor = "#66FF33";
	        	 document.getElementById("messageBox").textContent= 'Riga cancellata con successo!';
	        	row.parentNode.removeChild(row);	        	
	         } else {
	        	document.getElementById("messageBox").style.backgroundColor = "yellow";
	        	document.getElementById("messageBox").textContent= 'Errore nella cancellazione!';
	         	row.style.backgroundColor = "#ffffff";      
	         }
	        },
		});
	} else {
		row.style.backgroundColor = "#ffffff";
	}
	return false;
}

function confermaCancellazioneTuttiDDT2() {
	document.getElementById("messageBox").style.backgroundColor = "green";
	document.getElementById("messageBox").textContent= 'confermaCancellazioneTuttiDDT - Start';		
}

function confermaCancellazioneTuttiDDT() {
	document.getElementById("messageBox").style.backgroundColor = "green";
	document.getElementById("messageBox").textContent= 'confermaCancellazioneTuttiDDT - Start';		
	//document.getElementById("messageBox").style.backgroundColor = "yellow";
	//document.getElementById("messageBox").textContent= 'Cancellazione in corso...';		
	
	var table = document.getElementById(tableListaId);
	if (table != null) {
		//CHECK ALL THE ID ARTICOLO IN THE TABLE
		var rows = table.rows;
		for(i=0;i<rows.length;++i) {
			// var cell = rows.item(i).cells.item(0);
			var row = rows.item(i); // document.getElementById('tr_' + id);
			row.style.backgroundColor = "#FF0000";
			answer = confirm('Sei sicuro di voler cancellare questa riga?');
			if (answer != "0") {
				$( "#update-confirm" ).dialog({
					resizable: false,
					height:180,
					modal: true,
					buttons: {
						"Yes": function() {
							$( this ).dialog( "close" );
							link = link + "&id=" + id;
							doCancellazioneDDT(row, link)
						},
						"No": function() {
							link = link + "2&id=" + id;
							doCancellazioneDDT(row, link)
							$( this ).dialog( "close" );
						}
					}
				});
			} else {
				row.style.backgroundColor = "#ffffff";
			}
			return false;
		}
	}
}
function confermaCancellazioneTuttiOrdini() {
	var answer = confirm('Sei sicuro di voler cancellare tutti gli ordini?');
	return (answer != "0");
}


function confermaCancellazioneDDT(id, link) {
	document.getElementById("messageBox").style.backgroundColor = "green";
	document.getElementById("messageBox").textContent= 'confermaCancellazioneDDT - Start';		

	var row = document.getElementById('tr_' + id);
	row.style.backgroundColor = "#FF0000";
	answer = confirm('Sei sicuro di voler cancellare la riga selezionata?');
	if (answer != "0") {
		$( "#update-confirm" ).dialog({
	      resizable: false,
	      height:180,
	      modal: true,
	      buttons: {
	        "Yes": function() {
	          $( this ).dialog( "close" );
	          link = link + "&id=" + id;
	          doCancellazioneDDT(row, link)
	        },
	        "No": function() {
	          link = link + "2&id=" + id;
		      doCancellazioneDDT(row, link)
	          $( this ).dialog( "close" );
	        }
	      }
	    });
	} else {
		row.style.backgroundColor = "#ffffff";
	}
	return false;
}

function doCancellazioneDDT(row, link) {
	document.getElementById("messageBox").style.backgroundColor = "yellow";
	document.getElementById("messageBox").textContent= 'Cancellazione in corso...';		
	//AJAX call	
	dojo.io.bind({
		url: link,
		method: "get",
		handle: function(type,data,evt) {
            if ((type=='load') && (data != 'error')) {
        	    document.getElementById("messageBox").style.backgroundColor = "#66FF33";
        	    document.getElementById("messageBox").textContent= 'Riga cancellata con successo!';
        	    row.parentNode.removeChild(row);	        	
            } else {
        	    document.getElementById("messageBox").style.backgroundColor = "yellow";
        	    document.getElementById("messageBox").textContent= 'Errore nella cancellazione!';
         	    row.style.backgroundColor = "#ffffff";      
            }
        },
	});
}

function attivaPuntiConsegna(link,input) {
	dojo.io.bind({
		url: link,
		method: "get",
		handle: function(type,data,evt) {
         if ((type=='load') && (data != 'error')) {
        	alert('Modifica effettuata con successo!');
         } else {
        	alert('Errore! Impossibile attivare/disattivare il punto consegna');
        	input.checked = !input.checked;
         }
        },
	});
}

function aggiornaListaDDTs() {
	//RICAVO LA DATA E IL CODICE CLIENTE
	var dataFattura = document.getElementById('fattureEdit_fattura_data').value;
	var idCliente = document.getElementById('fattureEdit_fattura_idCliente').value;
	
	var reportDiv = dojo.widget.manager.getWidgetById("reportDivId");
	reportDiv.href = '/ContaWeb_v1.0.1/aggiornaDDTs.do?fattura.idCliente=' + idCliente + '&fattura.data=' + dataFattura;
	//LANCIO EVENTO	DI CARICAMENTO
	dojo.event.topic.publish('/aggiornaDDTs');
}

function selectionaListaDDTs() {
	var i = 1;
	var prefix = "listaDDTs-";
	
	var element = document.getElementById(prefix + i);
	while(element != null) {
		element.checked = true;
		i = i + 1;
		element = document.getElementById(prefix + i);
	}
	dojo.event.topic.publish('/calcolaTotaleFattura');
}

//EVENTO CARICA TOTALE DELLA FATTURA
dojo.event.topic.subscribe(
	'/calcolaTotaleFattura',function() {
		var scontoFattura = document.getElementById('fattureEdit_fattura_sconto').value;
		if (scontoFattura == '')
			scontoFattura = '0';
		var reportDiv = dojo.widget.manager.getWidgetById("divTotaleFattura");
		var prefix = "listaDDTs-";
		var i = 1;
		
		reportDiv.href="/ContaWeb_v1.0.1/calcolaTotaleFattura.do?fattura.sconto=" + scontoFattura;
		
		//AGGIUNGO I DDTs selezionati
		var listaDDTs='';
		
		var element = document.getElementById(prefix + i);
		while(element != null) {
			if (element.checked == true) {
				listaDDTs = listaDDTs + '&listaDDTs' + '=' + element.value;
			}
			i = i + 1;
			element = document.getElementById(prefix + i);
		}
		reportDiv.href += listaDDTs;
    }
);

//FUNZIONE CHE SI ATTIVA QUANDO SI COMINCIA A PROCESSARE LE FATTURE
function startProcessaFatture() {
	//dojo.event.topic.publish('/startRefresh');
	document.getElementById('submitFormProcessaFatture').disabled = true;
	//DISABILITO I BOTTONI
}

function stopProcessaFatture() {
	dojo.event.topic.publish('/stopRefresh');
	document.getElementById('submitFormProcessaFatture').disabled = false;
}

//NASCONDO I SOTTOMENU
function ChiudiMenu() {
	document.getElementById('sottomenuContabilita').style.visibility = 'hidden';
	document.getElementById('sottomenuOrdini').style.visibility = 'hidden';
	document.getElementById('sottomenuMagazzino').style.visibility = 'hidden';
	document.getElementById('sottomenuTabelle').style.visibility = 'hidden';
}



function changeAutista(idOrdine) {
	try
	{
		var xmlhttp;
		if (window.XMLHttpRequest)
		{
			xmlhttp=new XMLHttpRequest();
		}
		else
		{
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		xmlhttp.open("GET","storeAutista.do?idOrdine="+idOrdine+"&idAutista="+document.getElementById("ordine_idAutista_"+idOrdine).value,true);
		xmlhttp.send();
		
	}
	catch(e)
	{
		alert(e.message);
	}
}
function changeAutistaDDT(idDDT) {
	try
	{
		var xmlhttp;
		if (window.XMLHttpRequest)
		{
			xmlhttp=new XMLHttpRequest();
		}
		else
		{
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		xmlhttp.open("GET","storeAutistaDDT.do?idDDT="+idDDT+"&idAutista="+document.getElementById("ddt_idAutista_"+idDDT).value,true);
		xmlhttp.send();
		
	}
	catch(e)
	{
		alert(e.message);
	}
}
function changeAutistaTelefonata(el) {
	try
	{
		var sel = $(el);
		$.get("storeAutistaTelefonata.do?idOrdine=" + sel.attr('name').substr(4)
				+ "&idAutista=" + sel.val(), function() {
			location.reload();	
		});
	}
	catch(e)
	{
		alert(e.message);
	}
}

function changeDateShip(idOrdine) {
	try
	{
		var xmlhttp;
		if (window.XMLHttpRequest)
		{
			xmlhttp=new XMLHttpRequest();
		}
		else
		{
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		xmlhttp.open("GET","storeDateShip.do?idOrdine="+idOrdine+"&dataSpedizione="+document.getElementById("dataSpedizione_"+idOrdine).value,true);
		xmlhttp.send();
		
		document.getElementById("msg_"+idOrdine).className = 'left msg-ok';
		
		setTimeout(function(){document.getElementById("msg_"+idOrdine).className = 'left hide msg-ok';},2000)
		
	}
	catch(e)
	{
		alert(e.message);
	}
}


function evidenziaVoceMenuPrincipale(voce) {
	var elements = document.getElementsByName('menuItem');
	var i = 0;
	
	if (voce == 1) { //CONTABILITA
		document.getElementById('sottomenuContabilita').style.visibility = 'visible';
		document.getElementById('sottomenuMagazzino').style.visibility = 'hidden';
		document.getElementById('sottomenuOrdini').style.visibility = 'hidden';
		document.getElementById('sottomenuTabelle').style.visibility = 'hidden';
		evidenziaVoceMenu(-1);
	} else if (voce == 2) { //ORDINI
		document.getElementById('sottomenuOrdini').style.visibility = 'visible';
		document.getElementById('sottomenuContabilita').style.visibility = 'hidden';
		document.getElementById('sottomenuMagazzino').style.visibility = 'hidden';
		document.getElementById('sottomenuTabelle').style.visibility = 'hidden';
		evidenziaVoceMenu(-1);
	} else if (voce == 3) { //MAGAZZINO
		document.getElementById('sottomenuMagazzino').style.visibility = 'visible';
		document.getElementById('sottomenuContabilita').style.visibility = 'hidden';
		document.getElementById('sottomenuOrdini').style.visibility = 'hidden';
		document.getElementById('sottomenuTabelle').style.visibility = 'hidden';
		evidenziaVoceMenu(-1);
	} else if (voce == 4) { //TABELLE
		document.getElementById('sottomenuMagazzino').style.visibility = 'hidden';
		document.getElementById('sottomenuContabilita').style.visibility = 'hidden';
		document.getElementById('sottomenuOrdini').style.visibility = 'hidden';
		document.getElementById('sottomenuTabelle').style.visibility = 'visible';
		evidenziaVoceMenu(-1);
	} else
		ChiudiMenu();
	
	for(i=0;i<elements.length;++i) {
		if (i != voce) {
			elements.item(i).style.backgroundColor = '#B43C00';
		} else {
			elements.item(i).style.backgroundColor = '#FF6600';
		}
	}
}

//CONTROLLA GLI ELEMENTI DELLA LISTA ORDINI
function checkOrdineCell(element) {
	var value = element.value;
	for(i=0;i<value.length;++i) {
		if ((value[i]<'0' || value[i]>'9') && value[i]!=',' && value[i]!='.') {
			element.style.backgroundColor = 'red';
			alert('Attenzione, valore non corretto');
			return false;
		}
	}
	
	//Fix eventual color red
	element.style.backgroundColor = 'white';
	return true;
}

/*function checkOrdineForm(element) {
	//Copy note field outside the form into the hidden field
	var value = document.getElementById('txtOrdiniNote').value;
	document.getElementById('ordiniEdit_ordine_note').value = value;
	
	//disable all fields that are not set
	var i = 0;
	var inputField = eval(document.getElementById('detInput' + i));
	while (inputField != null) {
		if (inputField.value == '')
			inputField.disabled = true;

		++i;
		inputField = eval(document.getElementById('detInput' + i));
	}
}*/


/*ORDINI FORNITORI */
/* function cancellaRigheTabellaOrdini(table) {
	var tbl = document.getElementById(table);
	for (idx=0; i<tbl.row; i++) {
	    var tblRow = document.getElementById("riga"+i);
	    tbl.deleteRow(tblRow.rowIndex);
	}
}*/

function cancellaRigaTabellaOrdini(table,riga) {
	var tbl = document.getElementById(table);
	var tblRow = document.getElementById(riga);
	tbl.deleteRow(tblRow.rowIndex);
	return false;
}

function modificaRigaTabellaOrdini(table,cell) {
	if (dgbActive > 0) { // High priority Debug message
		document.getElementById("lblDebug").style.backgroundColor = "yellow";
		dbgString = dbgString + ' | modificaRigaTabellaOrdini('; 
		document.getElementById("lblDebug").textContent= dbgString;
		dbgString = dbgString + table,cell + ','; 
		document.getElementById("lblDebug").textContent= dbgString;
		dbgString = dbgString + table,cell + ')'; 
		document.getElementById("lblDebug").textContent= dbgString;
	}

	var tbl = document.getElementById(table);
	var tblCell = document.getElementById('pezzo' + cell);
	var hdn = document.getElementById('hdnPezzo' + cell);
	var lbl = document.getElementById('lblPezzo' + cell);
	
	hdn.value = lbl.textContent;
	lbl.textContent = '';
	hdn.className = 'testo';

	return false;
}

function updateRigaTabellaOrdini(table,cell) {
	var hdn = document.getElementById('hdnPezzo' + cell);
	var lbl = document.getElementById('lblPezzo' + cell);
	lbl.textContent = hdn.value;
	hdn.className = 'hiddenOrdineQta';
	//Update hidden field
	var riga = document.getElementById('riga' + cell);
	riga.childNodes[1].childNodes[0].value = hdn.value;
	
	return true;
}

function replaceSpaces(strInput) {
	var strReplaceAll = strInput;
	var intIndexOfMatch = strReplaceAll.indexOf(" ");
	while (intIndexOfMatch != -1) {
		strReplaceAll = strReplaceAll.replace(" ","%20");
		intIndexOfMatch = strReplaceAll.indexOf(" ");
	}
	return strReplaceAll;
}

var reqArticolo;
function uploadFieldsArticolo() {
	 if (reqArticolo.readyState == 4) { // Complete
      if (reqArticolo.status == 200) { // OK response
        var response = reqArticolo.responseText.split("@");
        if (response[0] == "200") {
        	var lblDescArticolo = document.getElementById("lblDescrizioneArticolo");
        	var lblPezzi = document.getElementById("lblPezziArticoli");
        	var txtPezzi = document.getElementById("txtPezziArticoli");
        	var bttInserisci = document.getElementById("bttInserisciArticolo");
        	var lblPrezzoArticolo = document.getElementById("lblPrezzoArticoloDaListino");
 
        	lblDescArticolo.textContent = response[3];
        	lblPrezzoArticolo.textContent = response[4];

        	bttInserisci.className = "button";
        	bttInserisci.disabled = false;
        	txtPezzi.focus();
   		} else {
   			disableFieldsArticolo();
		}
      } else { //disableFieldsArticolo(); 
      }
    }
}

function loadArticoloFornitore(input,urlRequest) { //usato in ordini fornitore
	if (input.value == '')
		return;
	//Add parameters
	
	var idArticolo = replaceSpaces(input.value);
	var idFornitore = document.getElementById("form1_ordineFornitore_idFornitore").value;
	urlRequest += '&idFornitore=' + idFornitore + '&idArticolo=' + idArticolo;

	if (window.XMLHttpRequest) {
      reqArticolo = new XMLHttpRequest();
      reqArticolo.onreadystatechange = uploadFieldsArticolo;
      try {
        reqArticolo.open("GET", urlRequest, true);
      } catch (e) { }
      reqArticolo.send(null);
    } 
}

function loadArticolo(inputId,urlRequest) { //usato in ordini cliente
	if (dgbActive > 2) { // Old Debug message
		document.getElementById("lblDebug").style.backgroundColor = "red";
		document.getElementById("lblDebug").textContent= dbgString;
	        dbgString = dbgString + ' | loadArticolo(';
		document.getElementById("lblDebug").textContent= dbgString;
	        dbgString = dbgString + inputId + ', ';
		document.getElementById("lblDebug").textContent= dbgString;
	        dbgString = dbgString + urlRequest + ')';
		document.getElementById("lblDebug").textContent= dbgString;
	}
	
	var input = $('#txtIdArticolo');
	var idOrdine = document.getElementById('idOrdine').value;
	var idCliente = document.getElementById('idClienteSelect').value;
	var dataSpedizione = document.getElementById('dataSpedizioneSelect').value;

	if (input.data("id") == '')
		return;
	var idArticolo = input.attr("data-id");
	
	urlRequest += '?idArticolo=' + idArticolo + "&idOrdine=" + idOrdine + "&idCliente=" + idCliente + "&dataSpedizione=" + dataSpedizione;

	if (dgbActive > 2) { // Old Debug message
		dbgString = dbgString + ', urlRequest: ' + urlRequest;
		document.getElementById("lblDebug").style.backgroundColor = "yellow";
	}

	if (window.XMLHttpRequest) {
      reqArticolo = new XMLHttpRequest();
      reqArticolo.onreadystatechange = uploadFieldsArticolo;
      try {
        reqArticolo.open("GET", urlRequest, true);
      } catch (e) { }
      reqArticolo.send(null);
    } 

	if (dgbActive > 2) { // Old Debug message
		document.getElementById("lblDebug").style.backgroundColor = "grey";
	}
}

function split( val ) {
	return val.split( /,\s*/ );
}

function extractLast( term ) {
	return split( term ).pop();
}

function autocompleteArticoli()
{
	try
	{
		$( "#txtIdArticolo" ).bind( "keydown", function( event ) {
			if ( event.keyCode === $.ui.keyCode.TAB && $( this ).autocomplete( "instance" ).menu.active ) 
			{
				event.preventDefault();
			}
		}).autocomplete({
			source : function(request, response) {
				try
				{
					
					xmlhttp=new XMLHttpRequest();
					xmlhttp.open("GET","searchCodiciArticoli.do?action=insert&codiceArticolo="+extractLast( request.term ),false);
					xmlhttp.send();

					var data = xmlhttp.responseText;
					
					var jsondata = JSON.parse(data);
					
					response(jsondata);
					
				}
				catch(e)
				{
					alert(e.message);
				}
            },
            
				search: function() {
				// custom minLength
				var term = extractLast( this.value );
				if ( term.length < 1 ) {
					return false;
				}
			},
			
			focus: function() {
				// prevent value inserted on focus
				return false;
			},
        	
			select: function( event, ui ) {
				
				this.value = ui.item.value;
				this.setAttribute("data-id", ui.item.id);
				
				var terms = split( this.value );
				// remove the current input
				terms.pop();
				// add the selected item
				terms.push( ui.item.value );
				// add placeholder to get the comma-and-space at the end
				terms.push( "" );
				this.value = terms.join( ", " );
				
				$("#txtPezziArticoli").focus();
				
				return false;
			}
		});
	}
	catch(e)
	{
		alert(e.message);
	}
}

function disableFieldsArticolo() {
	var bttInserisci = document.getElementById("bttInserisciArticolo");
	var lblDesc = document.getElementById("lblDescrizioneArticolo");
    var txtPezzi = document.getElementById("txtPezziArticoli");
	var lblPrezzi = document.getElementById("lblPrezzoArticoloDaListino");
    
//    var input = dojo.widget.byId('txtIdArticolo');
    var input = $('#txtIdArticolo');
//	var idArticolo = input.comboBoxSelectionValue.value;
    var idArticolo = input.data("id");
//	input.setValue('');
    input.val("");
	
	bttInserisci.className = 'hiddenElements';
	bttInserisci.disabled = true;
	txtPezzi.value = '';
//	lblDesc.textContent = '';
	lblDesc.innerHTML = '';
	lblPrezzi.innerHTML = '';
	input.focus();
}

function isNumeric(sText) {
   var ValidChars = "0123456789";
   var IsNumber=true;
   var Char;
 
   for (i = 0; i < sText.length && IsNumber == true; i++) {
      Char = sText.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) 
         IsNumber = false;
   }
   return IsNumber;
}

function inserisciArticolo() {
	if (dgbActive > 2) { // Old Debug message
		document.getElementById("lblDebug").style.backgroundColor = "yellow";
		dbgString = dbgString + ' | inserisciArticolo()'; 
		document.getElementById("lblDebug").textContent= dbgString;
	}

	var txtArticolo = $("#txtIdArticolo")
	var codiceArticolo = txtArticolo.val().split(" - ");
	codiceArticolo = codiceArticolo[0];

	if (dgbActive > 2) { // Old Debug message
		dbgString = dbgString + ', codice articolo:' + codiceArticolo;
		document.getElementById("lblDebug").textContent= dbgString;
	}
	
	var idArticolo = txtArticolo.attr("data-id");
	var table = document.getElementById('tableListaOrdini');
	if (table != null) {
		//CHECK ALL THE ID ARTICOLO IN THE TABLE
		var rows = table.rows;
		var rows_mod = -1;

		if (dgbActive > 2) { // Old Debug message
			dbgString = dbgString + ', table with ' + rows.length + ' rows'; 
			document.getElementById("lblDebug").textContent= dbgString;
		}
		
		for(i=0;i<rows.length;++i) {
			var cell = rows.item(i).cells.item(0);
			if(cell != null){
				var codice = cell.textContent;
				codice = codice.replace('\n','');
				var codiceTmp = codice.toString();
				codiceTmp = codiceTmp.replace('\n','');
				var lunghezza =  codiceTmp.length; 
				if (lunghezza > 20)
					codiceTmp = codiceTmp.substring(20, lunghezza - 7);

				if (dgbActive > 2) { // Old Debug message
					dbgString = dbgString + ',' + codice + '-' + typeof(codice) + '_' + codiceTmp + ':' + lunghezza; 
					document.getElementById("lblDebug").textContent= dbgString;
				}
				
				// if (codice.toUpperCase() == codiceArticolo.toUpperCase()) {
				if (codiceTmp.toUpperCase() == codiceArticolo.toUpperCase()) {
					rows_mod = i;
					break;
				}
			}
		}

		if (dgbActive > 2) { // Old Debug message
			dbgString = dbgString + ', rows_mod ' + rows_mod; 
			document.getElementById("lblDebug").textContent= dbgString;
		}
		
		var pezzi = 1 - 1; 
		pezzi = Number(document.getElementById('txtPezziArticoli').value);
		//check is numeric
		if (!isNumeric(pezzi)) 
			pezzi = 0 + 1;

		if (dgbActive > 2) { // Old Debug message
			dbgString = dbgString + ', pezzi ' + pezzi + ':' + typeof(pezzi); 
			document.getElementById("lblDebug").textContent= dbgString;
		}
		
		//INSERT A NEW ROW
		var newRow = table.insertRow(-1);
		var idx = newRow.rowIndex;
		newRow.setAttribute('id','riga'+idx);
		var desc = document.getElementById('lblDescrizioneArticolo').textContent;
		var prezzo = document.getElementById('lblPrezzoArticoloDaListino').textContent;

		if(rows_mod != -1) { // Articolo già presente
			var p_pezzi = parseInt(document.getElementById('lblPezzo'+rows_mod).innerHTML);
			p_pezzi = p_pezzi + pezzi;
			if (dgbActive > 2) { // Old Debug message
				dbgString = dbgString + ', p_pezzi ' + p_pezzi + ':' + typeof(p_pezzi); 
				document.getElementById("lblDebug").textContent= dbgString;
			}
			document.getElementById('lblPezzo'+rows_mod).innerHTML = p_pezzi;
			document.getElementById('dettagliOrdine('+idArticolo+')_pezziOrdinati').value = p_pezzi; 
		} else { // Articolo nuovo
			newRow.innerHTML = '\n \t<td><input name=\"dettagliOrdine('+idArticolo+').pezziOrdinati\" value=\"'+pezzi+'\" id=\"dettagliOrdine('+idArticolo+')_pezziOrdinati\" type=\"hidden\">\n'+codiceArticolo+'</td>\n \t<td>'+desc+'</td>\n \t<td id=\"pezzo'+idx+'\"><input id=\"hdnPezzo'+idx+'\" size=\"2\" class=\"hiddenOrdineQta\" value=\"'+pezzi+'\" onblur=\"return updateRigaTabellaOrdini(\'tableListaOrdini\',\''+idx+'\')\" type=\"text\"><label id=\"lblPezzo'+idx+'\">'+pezzi+'</label>\n \t</td>\n \t<td></td>\n \t<td>'+prezzo+'</td>\n \t<td><a href=\"\" onclick=\"return cancellaRigaTabellaOrdini(\'tableListaOrdini\',\'riga'+idx+'\')\">C</a></td>\n ';
			// newRow.innerHTML = '\n \t<td><input name=\"dettagliOrdine('+idArticolo+').pezziOrdinati\" value=\"'+pezzi+'\" id=\"dettagliOrdine('+idArticolo+')_pezziOrdinati\" type=\"hidden\">\n'+codiceArticolo+'</td>\n \t<td>'+desc+'</td>\n \t<td id=\"pezzo'+idx+'\"><input id=\"hdnPezzo'+idx+'\" size=\"2\" class=\"hiddenOrdineQta\" value=\"'+pezzi+'\" onblur=\"return updateRigaTabellaOrdini(\'tableListaOrdini\',\''+idx+'\')\" type=\"text\"><label id=\"lblPezzo'+idx+'\">'+pezzi+'</label>\n \t</td>\n \t<td></td>\n \t<td>'+prezzo+'</td>\n \t<td><a href=\"\" onclick=\"return modificaRigaTabellaOrdini(\'tableListaOrdini\',\''+idx+'\')\">M</a> <a href=\"\" onclick=\"return cancellaRigaTabellaOrdini(\'tableListaOrdini\',\'riga'+idx+'\')\">C</a></td>\n ';
		}
		disableFieldsArticolo();
	}
	return false;
}

function inserisciArticolo_daStats(id, codice, descrizione) {
	if (dgbActive > 2) { // Old Debug message
		document.getElementById("lblDebug").style.backgroundColor = "yellow";
		dbgString = dbgString + ' | inserisciArticolo_daStats('; 
		document.getElementById("lblDebug").textContent= dbgString;
		dbgString = dbgString + id + ','; 
		document.getElementById("lblDebug").textContent= dbgString;
		dbgString = dbgString + codice + ','; 
		document.getElementById("lblDebug").textContent= dbgString;
		dbgString = dbgString + descrizione + ')'; 
		document.getElementById("lblDebug").textContent= dbgString;
	}
	
	var codiceArticolo = codice;
	var idArticolo = id;
	if (dgbActive > 2) { // Old Debug message
		dbgString = dbgString + ', codice articolo:' + codiceArticolo + ', id:' + idArticolo;
		document.getElementById("lblDebug").textContent= dbgString;
	}
	
	var table = document.getElementById('tableListaOrdini');
	if (table != null) {
		//CHECK ALL THE ID ARTICOLO IN THE TABLE
		var rows = table.rows;
		var rows_mod = -1;

		if (dgbActive > 2) { // Old Debug message
			dbgString = dbgString + ', table with ' + rows.length + ' rows'; 
			document.getElementById("lblDebug").textContent= dbgString;
		}

		for(i=0;i<rows.length;++i) {
			var cell = rows.item(i).cells.item(0);
			if(cell != null){
				var codice = cell.textContent;
				codice = codice.replace('\n','');
				var codiceTmp = codice.toString();
				codiceTmp = codiceTmp.replace('\n','');
				var lunghezza =  codiceTmp.length; 
				if (lunghezza > 20)
					codiceTmp = codiceTmp.substring(20, lunghezza - 7);

				if (dgbActive > 2) { // Old Debug message
					dbgString = dbgString + ',' + codice + '-' + typeof(codice) + '_' + codiceTmp + ':' + lunghezza; 
					document.getElementById("lblDebug").textContent= dbgString;
				}

				// if (codice.toUpperCase() == codiceArticolo.toUpperCase()) {
				if (codiceTmp.toUpperCase() == codiceArticolo.toUpperCase()) {
					rows_mod = i;
					break;
				}
			}
		}

		if (dgbActive > 2) { // Old Debug message
			dbgString = dbgString + ', rows_mod ' + rows_mod; 
			document.getElementById("lblDebug").textContent= dbgString;
		}
		
		var pezzi = 1;
		if (!isNumeric(pezzi)) 
			pezzi = 0 + 1;
		if (dgbActive > 2) { // Old Debug message
			dbgString = dbgString + ', pezzi ' + pezzi + ':' + typeof(pezzi); 
			document.getElementById("lblDebug").textContent= dbgString;
		}

		//INSERT A NEW ROW
		var newRow = table.insertRow(-1);
		var idx = newRow.rowIndex;
		newRow.setAttribute('id','riga'+idx);
		var desc = descrizione;
		
		if(rows_mod != -1) {
			var p_pezzi = parseInt(document.getElementById('lblPezzo'+rows_mod).innerHTML);
			p_pezzi = p_pezzi + pezzi;
			if (dgbActive > 2) { // Old Debug message
				dbgString = dbgString + ', p_pezzi ' + p_pezzi + ':' + typeof(p_pezzi); 
				document.getElementById("lblDebug").textContent= dbgString;
			}
			document.getElementById('lblPezzo'+rows_mod).innerHTML = p_pezzi;
			document.getElementById('dettagliOrdine('+idArticolo+')_pezziOrdinati').value = p_pezzi; 
		} else {
			newRow.innerHTML =' \n \t<td><input name=\"dettagliOrdine('+idArticolo+').pezziOrdinati\" value=\"'+pezzi+'\" id=\"dettagliOrdine('+idArticolo+')_pezziOrdinati\" type=\"hidden\">\n'+codiceArticolo+'</td>\n \t<td>'+desc+'</td>\n \t<td id=\"pezzo'+idx+'\"><input id=\"hdnPezzo'+idx+'\" size=\"2\" class=\"hiddenOrdineQta\" value=\"'+pezzi+'\" onblur=\"return updateRigaTabellaOrdini(\'tableListaOrdini\',\''+idx+'\')\" type=\"text\"><label id=\"lblPezzo'+idx+'\">'+pezzi+'</label>\n \t</td><td></td><td> ... </td>\n \t<td><a href=\"\" onclick=\"return cancellaRigaTabellaOrdini(\'tableListaOrdini\',\'riga'+idx+'\')\">C</a></td>\n ';
			// newRow.innerHTML =' \n \t<td><input name=\"dettagliOrdine('+idArticolo+').pezziOrdinati\" value=\"'+pezzi+'\" id=\"dettagliOrdine('+idArticolo+')_pezziOrdinati\" type=\"hidden\">\n'+codiceArticolo+'</td>\n \t<td>'+desc+'</td>\n \t<td id=\"pezzo'+idx+'\"><input id=\"hdnPezzo'+idx+'\" size=\"2\" class=\"hiddenOrdineQta\" value=\"'+pezzi+'\" onblur=\"return updateRigaTabellaOrdini(\'tableListaOrdini\',\''+idx+'\')\" type=\"text\"><label id=\"lblPezzo'+idx+'\">'+pezzi+'</label>\n \t</td><td></td><td> ... </td>\n \t<td><a href=\"\" onclick=\"return modificaRigaTabellaOrdini(\'tableListaOrdini\',\''+idx+'\')\">M</a> <a href=\"\" onclick=\"return cancellaRigaTabellaOrdini(\'tableListaOrdini\',\'riga'+idx+'\')\">C</a></td>\n ';
		}
    	disableFieldsArticolo();
	}
	return false;
}

function modificaPuntoConsegna(idRow) {
	var row = document.getElementById('tr_' + idRow);
	
	var nome = row.cells[0].textContent;
	var indirizzo = row.cells[1].textContent;
	var cap = row.cells[2].textContent;
	var localita = row.cells[3].textContent;
	var prov = row.cells[4].textContent;
	var conad = row.cells[5].textContent;
	
	document.getElementById('puntiConsegnaList_input_puntoConsegna_id').value = idRow;
	document.getElementById('puntiConsegnaList_input_puntoConsegna_nome').value = nome;
	document.getElementById('puntiConsegnaList_input_puntoConsegna_indirizzo').value = indirizzo;
	document.getElementById('puntiConsegnaList_input_puntoConsegna_cap').value = cap;
	document.getElementById('puntiConsegnaList_input_puntoConsegna_localita').value = localita;
	document.getElementById('puntiConsegnaList_input_puntoConsegna_prov').value = prov;
	document.getElementById('puntiConsegnaList_input_puntoConsegna_codConad').value = conad;
	
	return false;
}

function pulisciCampiPuntoConsegna() {
	document.getElementById('puntiConsegnaList_input_puntoConsegna_id').value = "";
	document.getElementById('puntiConsegnaList_input_puntoConsegna_nome').value = "";
	document.getElementById('puntiConsegnaList_input_puntoConsegna_indirizzo').value = "";
	document.getElementById('puntiConsegnaList_input_puntoConsegna_cap').value = "";
	document.getElementById('puntiConsegnaList_input_puntoConsegna_localita').value = "";
	document.getElementById('puntiConsegnaList_input_puntoConsegna_prov').value = "";
	document.getElementById('puntiConsegnaList_input_puntoConsegna_codConad').value = "";
	
	return false;
}

function listaProdottiFornitori(id)
{
	window.open("articoliFornitoriList.do?fornitore="+id, "Lista prodotti fornitore", "width=400, height=500, scrollbars=yes");
}

function viewList()
{
	try
	{
		
		$(".dojoPopupContainer").show();// dojoComboBoxOptions
		$(".dojoPopupContainer").css("opacity","1");
		alert($(".dojoPopupContainer").attr("style"));
	}
	catch(e)
	{
		alert(e.message);
	}
}

function storeTelefonataEseguita(el)
{
	try
	{	
		el = $(el);
		var id = el.val();
		var checked = el.is(':checked');
		
		$.get( "storeTelefonataEseguita.do", { idTelefonata: id, eseguita:  checked} );
		
		var tr = el.parents("tr")
		if(checked) $("td:gt(0):lt(5)", tr).addClass("eseguita");
		else $("td:gt(0):lt(5)", tr).removeClass("eseguita");
				
	}
	catch(e)
	{
		alert(e.message);
	}
	
}
function setUrlStampaListino(url, id)
{
	var c = $("#selCategoria_" + id);
	var f = $("#selFornitore_" + id);
	
	if(c.val()) url += '&idCategoria=' + c.val();
	if(f.val()) url += '&idFornitore=' + f.val();
	
	$("#stampalistino_" + id).attr('href', url);	
}


function setUrlVariaListino(url, id)
{
	var c = $("#selCategoria_" + id);
	var f = $("#selFornitore_" + id);	
	var au = $("#aumento_" + id);
	var tp = $("input[name=tipo_" + id + ']:checked');
	
	url += '&action=varia';
	if(c.val()) url += '&idCategoria=' + c.val();
	if(f.val()) url += '&idFornitore=' + f.val();
	if(au.val()) url += '&aumento=' + au.val();
	if(tp.val()) url += '&tipo=' + tp.val();
	
	$("#variaListino_" + id).attr('href', url);	   
}


// Popup di conferma delete (09/02/2018)
function confirmDelete(entity) {
	var message = "Sei sicuro di proseguire con la cancellazione";
	if(entity != null && entity != undefined){
		if(entity == "ordiniClienti"){
			message = message + " degli ordini?"
		}
	}
    return confirm(message);
}

// Popup di conferma stampe (20/02/2018)
function confirmStampe(buttonPressed) {
	if(buttonPressed != undefined){
		if(buttonPressed == "Spedizione Fatture PEC" || buttonPressed == "Spedizione Fatture MAIL"){
			var message = "Al termine dell'operazione verra' inviata un'email di riepilogo a info@urbanialimentari.com.\nConfermi?";
			return confirm(message);
		}
	}
}

// Funzione per abilitare/disabilitare campi nella creazione/modifica di un cliente
function enableClienti(){
	var cbDittaIndividuale = document.getElementById('clienteDittaIndividuale');
    if(cbDittaIndividuale != undefined){
      if(!cbDittaIndividuale.checked){
        document.getElementById('clienteNome').disabled = true;
        document.getElementById('clienteNome').readOnly = true;
        document.getElementById('clienteCognome').disabled = true;
        document.getElementById('clienteCognome').readOnly = true;
      }else{
        document.getElementById('clienteNome').disabled = false;
        document.getElementById('clienteNome').readOnly = false;
        document.getElementById('clienteCognome').disabled = false;
        document.getElementById('clienteCognome').readOnly = false;
      }
    }
    var cbRaggruppaRiBa = document.getElementById('clienteRaggruppaRiba');
    if(cbRaggruppaRiBa != undefined){
    	if(!cbRaggruppaRiBa.checked){
            document.getElementById('clienteNomeRaggruppamentoRiBa').disabled = true;
            document.getElementById('clienteNomeRaggruppamentoRiBa').readOnly = true;
          }else{
            document.getElementById('clienteNomeRaggruppamentoRiBa').disabled = false;
            document.getElementById('clienteNomeRaggruppamentoRiBa').readOnly = false;
          }
    }
}

// Funzione caricata al load del body
function onBodyLoad(){
	enableClienti();
}

//### NUOVA MASCHERA ###
function enableNumAut() {
	if($("#autonum") != undefined && $("#autonum").length){
		if($("#autonum").is(':checked')){
			$("#ddtObject_ddt_numeroProgressivo").prop('disabled', true);
			$("#ddtObject_ddt_annoContabile").prop('disabled', true);
		} else{
			$("#ddtObject_ddt_numeroProgressivo").prop('disabled', false);
			$("#ddtObject_ddt_annoContabile").prop('disabled', false);
		}
	}
}


// ### END NUOVA MASCHERA ###