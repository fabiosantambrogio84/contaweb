<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="container-fluid">
  <nav class="navbar navbar-fixed-top navbar-default">
    <div class="container-fluid">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar1">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="welcome.do">
        	<img style="height: 30px" src="img/star.jpg" alt="ContaWeb">ContaWeb
        </a>
      </div>
      <div id="navbar1" class="navbar-collapse collapse">
        <ul class="nav navbar-nav">
          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Contabilità</a>
            <ul class="dropdown-menu" role="menu">
              <li><a href="ddtList.do">DDT</a></li>
              <li><a href="fattureList.do">Fatture</a></li>              
              <li><a href="bolleAcquistoList.do">B. Acquisto</a></li>
              <li><a href="noteAccreditoList.do">Note Accredito</a></li>
              <li><a href="pagamentiEseguitiList.do">Pagamenti</a></li>
            </ul>
          </li>
          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Ordini</a>
            <ul class="dropdown-menu" role="menu">
              <li><a href="telefonateOggi.do">Telef. oggi</a></li>
              <li><a href="telefonateList.do">Lista telef.</a></li>       
              <li><a href="ordiniList.do">Ordini clienti</a></li>
              <li><a href="ordiniFornitoriList.do">Ordini Fornitori</a></li>
              <li><a href="ordiniAutistaList.do">Ordini Autista</a></li>
            </ul>           
          </li>
          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Magazzino</a>
            <ul class="dropdown-menu" role="menu">
              <li><a href="articoliList.do">Articoli</a></li>
              <li><a href="categorieArticoliList.do">Cat. Articoli</a></li>              
              <li><a href="destinazioniArticoliList.do">Dest. Articoli</a></li>
              <li><a href="fornitoriList.do">Fornitori</a></li>
              <li><a href="giacenzeList.do">Giacenze</a></li>
              <li><a href="listiniList.do">Listini</a></li>
            </ul>
          </li>
          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Tabelle</a>
            <ul class="dropdown-menu" role="menu">
              <li><a href="clientiList.do">Clienti</a></li>
              <li><a href="agentiList.do">Agenti</a></li>              
              <li><a href="autistiList.do">Autisti</a></li>
              <li><a href="scontiList.do">Sconti</a></li>
              <li><a href="pagamentiList.do">Tip. Pagamento</a></li>
              <li><a href="ivaList.do">Aliquote Iva</a></li>
            </ul>
          </li>
          <li><a href="statisticheEdit_input.do">Statistiche</a></li>
          <li><a href="stampeList_input.do">Stampe</a></li>
          <li><a href="propertiesEdit_input.do">Config</a></li>
        </ul>
      </div>
      <!--/.nav-collapse -->
    </div>
    <!--/.container-fluid -->
  </nav>
</div>