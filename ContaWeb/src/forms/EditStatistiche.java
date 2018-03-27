package forms;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import vo.Articolo;
import vo.Cliente;
import vo.Fornitore;
import vo.TotaliStatistica;
import web.List;
import dao.Articoli;
import dao.BolleAcquisto;
import dao.Clienti;
import dao.DDTs;
import dao.DataAccessException;
import dao.Fornitori;
import dao.StatisticheVendite;

public class EditStatistiche extends List {

   private static final long serialVersionUID = 1L;
   
   private Collection listClienti = null;
   private Collection listFornitori = null;
   private Collection listArticoli = null;
   private Collection datiTabella = null;
   
   private Collection listDDT = null;
   private Collection listBolle = null;
   
   private Map periodi = null;
   private Integer idFornitore = null;
   private Integer idCliente = null;
   private Integer idArticolo = null;
   private String lotto = null;
   private Integer periodo = 0;
   private Date dataDal = null;
   private Date dataAl = null;
   private Boolean mostraDettagli = null;
   public Collection getListDDT() {
	return listDDT;
}

public void setListDDT(Collection listDDT) {
	this.listDDT = listDDT;
}

public Collection getListBolle() {
	return listBolle;
}

public void setListBolle(Collection listBolle) {
	this.listBolle = listBolle;
}

private Boolean groupArticoli = null;
   private Integer count = null;
   private BigDecimal totale = null;
   private BigDecimal qta = null;
   private BigDecimal media = null;
   private JFreeChart chart = null;
   
   private Cliente cliente = null;
   private Fornitore fornitore = null;
   private Articolo articolo = null;

public Cliente getCliente() {
	return cliente;
}

public void setCliente(Cliente cliente) {
	this.cliente = cliente;
}

public Fornitore getFornitore() {
	return fornitore;
}

public void setFornitore(Fornitore fornitore) {
	this.fornitore = fornitore;
}

public Articolo getArticolo() {
	return articolo;
}

public void setArticolo(Articolo articolo) {
	this.articolo = articolo;
}

public JFreeChart getChart() {
	return chart;
}

public Boolean getGroupArticoli() {
	return groupArticoli;
}

public void setGroupArticoli(Boolean groupArticoli) {
	this.groupArticoli = groupArticoli;
}

public void setChart(JFreeChart chart) {
	this.chart = chart;
}

public Integer getPeriodo() {
	return periodo;
}

public void setPeriodo(Integer periodo) {
	this.periodo = periodo;
}

@SuppressWarnings("unchecked")
public Map getPeriodi() {
	if (periodi == null) {
		periodi = new HashMap();
		periodi.put(StatisticheVendite.ANNO_CORRENTE, "anno corrente");
		periodi.put(StatisticheVendite.MESE_CORRENTE, "mese corrente");
		periodi.put(StatisticheVendite.PERIODO_CORRENTE, "giorni");
	}
	return periodi;
}

public void setPeriodi(Map periodi) {
	this.periodi = periodi;
}

@SuppressWarnings("unchecked")
public Collection getListArticoli() {
	if (listArticoli == null) {
		try {
			listArticoli = new ArrayList();
			Articolo articolo = new Articolo();
			articolo.setId(-1);
			articolo.setCodiceArticolo("");
			articolo.setDescrizione("TUTTI");
			listArticoli.add(articolo);
			listArticoli.addAll(new Articoli().getElements());
		} catch (Exception e) {
			return null;
		}
	}
	return listArticoli;
}

public void setListArticoli(Collection listArticoli) {
	this.listArticoli = listArticoli;
}

@SuppressWarnings("unchecked")
public Collection getListClienti() {
	if (listClienti == null) {
		try {
			listClienti = new ArrayList();
			Cliente cliente = new Cliente();
			cliente.setId(-1);
			cliente.setRs("TUTTI");
			listClienti.add(cliente);
			listClienti.addAll(new Clienti().getElements());
		} catch (Exception e) {
			return null;
		}
	}
	return listClienti;
}

public void setListClienti(Collection listClienti) {
	this.listClienti = listClienti;
}

@SuppressWarnings("unchecked")
public Collection getListFornitori() {
	if (listFornitori == null) {
		try {
			listFornitori = new ArrayList();
			Fornitore fornitore = new Fornitore();
			fornitore.setId(-1);
			fornitore.setDescrizione("TUTTI");
			listFornitori.add(fornitore);
			listFornitori.addAll(new Fornitori().getElements());
		} catch (Exception e) {
			return null;
		}
	}
	return listFornitori;
}

public void setListFornitori(Collection listFornitori) {
	this.listFornitori = listFornitori;
}

@SuppressWarnings("unchecked")
public String input() {
	return "input";
}

private JFreeChart getChart(Vector results) {
	
	String asseX = "Mesi";
	int counter = Calendar.MONTH;
	SimpleDateFormat date;
	Calendar cal = Calendar.getInstance();
	
	switch (periodo) {
	case StatisticheVendite.ANNO_CORRENTE: asseX = "Mesi";
		counter = Calendar.MONTH;
		date = new SimpleDateFormat("MMM", Locale.ITALIAN);			
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		break;	
	case StatisticheVendite.MESE_CORRENTE: asseX = "Settimane";
		counter = Calendar.WEEK_OF_MONTH;
		cal.set(Calendar.WEEK_OF_MONTH, 1);
		date = new SimpleDateFormat("WW", Locale.ITALIAN);
		break;
	case StatisticheVendite.PERIODO_CORRENTE: asseX = "Giorni";
		date = new SimpleDateFormat("dd/MM", Locale.ITALIAN);
		counter = Calendar.DAY_OF_YEAR;
		cal.setTime(dataDal);
		break;
	default:
		date = new SimpleDateFormat("MMM", Locale.ITALIAN);
	}
	
	DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
	int i;
	
	for (i=0; i<results.size(); ++i) {
		dataSet.addValue((BigDecimal) results.get(i), "Euro", date.format(cal.getTime()));
		cal.add(counter, 1);
	}

	JFreeChart chart = ChartFactory.createBarChart(null,asseX, "Euro", dataSet, PlotOrientation.VERTICAL, false, false, false);

	CategoryPlot plot = chart.getCategoryPlot();
	BarRenderer renderer = (BarRenderer)plot.getRenderer();
	DecimalFormat format = new DecimalFormat("� #,###,##0.00");
	renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",format));		
	renderer.setItemLabelsVisible(true);
	renderer.setBaseItemLabelsVisible(true);
	renderer.setSeriesPaint(0, Color.BLUE, true);
	plot.setRenderer(renderer);
	
	return chart;
}

public StatisticheVendite getStatisticheParametrizzate() throws DataAccessException {
	StatisticheVendite vendite = new StatisticheVendite();
	
   if (idCliente != -1) {
	   cliente = new Clienti().find(idCliente);
	   vendite.setCliente(cliente);
   }
   
   if (idFornitore != -1) {
       fornitore = new Fornitori().find(idFornitore);
	   vendite.setFornitore(fornitore);
   }
		 
   if (idArticolo != -1) {
	   articolo = new Articoli().find((Integer)idArticolo);
	   vendite.setArticolo(articolo);   
   }
   
   vendite.setLotto(lotto);
   
   vendite.setPeriodo(periodo);
   vendite.setDataFine(dataAl);
   vendite.setDataInizio(dataDal);
   
   return vendite;
}

public String viewChart() throws DataAccessException {
	chart = getChart(getStatisticheParametrizzate().calcolaValoriGrafico());
	return SUCCESS;
}

public String ricercaLotti() throws DataAccessException {
	if (lotto != null && lotto.length() > 0) {
		listDDT = new DDTs().ricercaLotto(lotto);
		listBolle = new BolleAcquisto().ricercaLotto(lotto);
	}
	
	return SUCCESS;
}

@SuppressWarnings("unchecked")
public String execute() {
	   try {
		   
		   TotaliStatistica tot = getStatisticheParametrizzate().calcolaTotali(mostraDettagli, groupArticoli);
		   if (tot != null) {
			   totale = tot.getTotale();
			   qta = tot.getQtaVenduta();
			   media = tot.getMedia();
			   if (mostraDettagli != null && mostraDettagli) {
				   datiTabella = tot.getDettagli();
				   count = datiTabella.size();
			   }
		   }
	   } catch (Exception e) {
		   stampaErrore("EditStatistiche.execute()",e);
		   return ERROR;
	   }
	   return SUCCESS;
   }

public Integer getIdArticolo() {
	return idArticolo;
}

public void setIdArticolo(Integer idArticolo) {
	this.idArticolo = idArticolo;
}

public Integer getIdFornitore() {
	return idFornitore;
}

public void setIdFornitore(Integer idFornitore) {
	this.idFornitore = idFornitore;
}

public String getLotto() {
	return lotto;
}

public void setLotto(String lotto) {
	this.lotto = lotto;
}

public Date getDataAl() {
	return dataAl;
}

public void setDataAl(Date dataAl) {
	this.dataAl = dataAl;
}

public Date getDataDal() {
	return dataDal;
}

public void setDataDal(Date dataDal) {
	this.dataDal = dataDal;
}

public Integer getIdCliente() {
	return idCliente;
}

public void setIdCliente(Integer idCliente) {
	this.idCliente = idCliente;
}

public Collection getDatiTabella() {
	return datiTabella;
}

public void setDatiTabella(Collection datiTabella) {
	this.datiTabella = datiTabella;
}

public Integer getCount() {
	return count;
}

public void setCount(Integer count) {
	this.count = count;
}

public BigDecimal getMedia() {
	return media;
}

public void setMedia(BigDecimal media) {
	this.media = media;
}

public BigDecimal getTotale() {
	return totale;
}

public void setTotale(BigDecimal totale) {
	this.totale = totale;
}

public Boolean getMostraDettagli() {
	return mostraDettagli;
}

public void setMostraDettagli(Boolean mostraDettagli) {
	this.mostraDettagli = mostraDettagli;
}

public BigDecimal getQta() {
	return qta;
}

public void setQta(BigDecimal qta) {
	this.qta = qta;
}
}