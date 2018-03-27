<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<!-- MENU -->
<div id="menu_container">
	<div class="menu_block" name="menuItem" onmouseover="evidenziaVoceMenuPrincipale(0)">
			<a href="welcome.do" class="menu_link">Welcome</a>
	</div>
	<div id="menuContabilita" name="menuItem" class="menu_block" onmouseover="evidenziaVoceMenuPrincipale(1)">
			<a href="javascript:void(0)" class="menu_link">Contabilità</a>
	</div>
	<div id="menuOrdini" name="menuItem" class="menu_block" onmouseover="evidenziaVoceMenuPrincipale(2)">
			<a href="javascript:void(0)" class="menu_link">Ordini</a>
	</div>
	<div id="menuMagazzino" name="menuItem" class="menu_block" onMouseOver="evidenziaVoceMenuPrincipale(3)">
			<a href="javascript:void(0)" class="menu_link">Magazzino</a>
	</div>
	<div id="menuTabelle" name="menuItem" class="menu_block" onMouseOver="evidenziaVoceMenuPrincipale(4)">
			<a href="javascript:void(0)" class="menu_link">Tabelle</a>
	</div>
	<div class="menu_block" name="menuItem">
			<a href="statisticheEdit_input.do" class="menu_link" onmouseover="evidenziaVoceMenuPrincipale(5)">Statistiche</a>
	</div>
	<div class="menu_block" name="menuItem">
			<a href="stampeList_input.do" class="menu_link" onmouseover="evidenziaVoceMenuPrincipale(6)">Stampe</a>
	</div>
	<div class="menu_block" name="menuItem">
			<a href="propertiesEdit_input.do" class="menu_link" onmouseover="evidenziaVoceMenuPrincipale(7)">Config</a>
	</div>
</div>

<!-- SOTTOMENU CONTABILITA' -->
<div id="sottomenuContabilita">
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(0)">
			<a href="ddtList.do" class="menu_link">DDT</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(1)">
			<a href="fattureList.do" class="menu_link">Fatture</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(2)">
			<a href="bolleAcquistoList.do" class="menu_link">B. Acquisto</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(3)">
			<a href="noteAccreditoList.do" class="menu_link">Note Accredito</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(4)">
			<a href="pagamentiEseguitiList.do" class="menu_link">Pagamenti</a>
	</div>
</div>

<!-- SOTTOMENU ORDINI -->
<div id="sottomenuOrdini">
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(5)">
			<a href="telefonateOggi.do" class="menu_link">Telef. oggi</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(6)">
			<a href="telefonateList.do" class="menu_link">Lista telef.</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(7)">
			<a href="ordiniList.do" class="menu_link">Ordini clienti</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(8)">
			<a href="ordiniFornitoriList.do" class="menu_link">Ordini fornit.</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(9)"> 
			<a href="ordiniAutistaList.do" class="menu_link">Ordini Autista</a>
	</div>
</div>

<!-- SOTTOMENU MAGAZZINO -->
<div id="sottomenuMagazzino">
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(10)">
			<a href="articoliList.do" class="menu_link">Articoli</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(11)">
			<a href="categorieArticoliList.do" class="menu_link">Cat. Articoli</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(12)">
			<a href="destinazioniArticoliList.do" class="menu_link">Dest. Articoli</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(13)">
			<a href="fornitoriList.do" class="menu_link">Fornitori</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(14)">
			<a href="giacenzeList.do" class="menu_link">Giacenze</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(15)">
			<a href="listiniList.do" class="menu_link">Listini</a>
	</div>
</div>

<div id="sottomenuTabelle">
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(16)">
			<a href="clientiList.do" class="menu_link">Clienti</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(17)">
			<a href="agentiList.do" class="menu_link">Agenti</a>
	</div>
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(18)">
			<a href="autistiList.do" class="menu_link">Autisti</a>
	</div>	
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(19)">
			<a href="scontiList.do" class="menu_link">Sconti</a>
	</div>	
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(20)">
			<a href="pagamentiList.do" class="menu_link">Tip. Pagamento</a>
	</div>	
	<div class="sottomenuBlock" name="sottoMenuItem" onmouseover="evidenziaVoceMenu(21)">
			<a href="ivaList.do" class="menu_link">Aliquote Iva</a>
	</div>
</div>