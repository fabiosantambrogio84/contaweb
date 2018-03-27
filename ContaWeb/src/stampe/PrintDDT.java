package stampe;

import dao.DDTs;

import stampemgr.StampeMgr;
import vo.DDT;

public class PrintDDT extends PrintPDF {
	
	//private static String SUCCESS_NO_PREZZI = "success_no_prezzi";
	//private static String SUCCESS_PREZZI = "success_prezzi";
	
	private static final long serialVersionUID = 5417096769427980677L;
	/*private List dettagliDDT = null;*/
	private Integer id = null;
	
	/*//CLIENTE
	private String rs = null;
	private String rs2 = null;
	private String indirizzo = null;
	private String cap = null;
	private String loc = null;
	private String prov = null;
	
	//DESTINAZIONE
	private String dnome = null;
	private String dindirizzo = null;
	private String dcap = null;
	private String dloc = null;
	private String dprov = null;
	
	//HEADER
	private String nprog = null;
	private Date data = null;
	private String piva = null;
	private String codiceFiscale = null;
	private String causale = null;
	
	
	//PAGE FOOTER
	private BigDecimal totaleDDT = null;
	private String trasporto = null;
	private Integer colli = null;
	private Date dataTrasporto = null;
	private Date oraTrasporto = null;*/
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/*public String execute() throws Exception {
		DDT ddt = new DDT();
		ddt.setId(id);
		ddt = (DDT) new DDTs().findWithAllReferences(ddt);
		
		dettagliDDT = ddt.getDettagliDDT();
		//CLIENTE
		rs = ddt.getCliente().getRs();
		rs2 = ddt.getCliente().getRs2();
		indirizzo = ddt.getCliente().getIndirizzo();
		cap = ddt.getCliente().getCap();
		prov = ddt.getCliente().getProv();
		loc = ddt.getCliente().getLocalita();
		
		//DESTINAZIONE
		dnome = ddt.getPuntoConsegna().getNome();
		dindirizzo = ddt.getPuntoConsegna().getIndirizzo();
		dloc = ddt.getPuntoConsegna().getLocalita();
		dcap = ddt.getPuntoConsegna().getCap();
		dprov = ddt.getPuntoConsegna().getProv();

		//HEADER
		nprog = ddt.getNumeroProgressivoCompleto();
		data = ddt.getData();
		piva = ddt.getCliente().getPiva();
		codiceFiscale = ddt.getCliente().getCodiceFiscale();
		causale = ddt.getCausale();

		//PAGE FOOTER
		trasporto = ddt.getTrasporto();
		dataTrasporto = ddt.getDataTrasporto();
		oraTrasporto = ddt.getOraTrasporto();
		totaleDDT = ddt.calcolaTotale();
		colli = ddt.getColli();
		
		if (ddt.getCliente().isNascondiPrezzi())
			return SUCCESS_NO_PREZZI;
		else
			return SUCCESS_PREZZI;
		
	}*/
	
	public String execute() throws Exception {
		try {
			DDT ddt = new DDT();
			ddt.setId(id);
			ddt = (DDT) new DDTs().findWithAllReferences(ddt);
			
			pdfFile = StampeMgr.getInstance().richiediPDFDocumento(ddt); 
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}

	/*public List getDettagliDDT() {
		return dettagliDDT;
	}

	public void setDettagliDDT(List dettagliDDT) {
		this.dettagliDDT = dettagliDDT;
	}*/

	/*public Integer getColli() {
		return colli;
	}

	public void setColli(Integer colli) {
		this.colli = colli;
	}

	public Date getDataTrasporto() {
		return dataTrasporto;
	}

	public void setDataTrasporto(Date dataTrasporto) {
		this.dataTrasporto = dataTrasporto;
	}

	public Date getOraTrasporto() {
		return oraTrasporto;
	}

	public void setOraTrasporto(Date oraTrasporto) {
		this.oraTrasporto = oraTrasporto;
	}

	public BigDecimal getTotaleDDT() {
		return totaleDDT;
	}

	public void setTotaleDDT(BigDecimal totaleDDT) {
		this.totaleDDT = totaleDDT;
	}

	public String getTrasporto() {
		return trasporto;
	}

	public void setTrasporto(String trasporto) {
		this.trasporto = trasporto;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getRs() {
		return rs;
	}

	public void setRs(String rs) {
		this.rs = rs;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDcap() {
		return dcap;
	}

	public void setDcap(String dcap) {
		this.dcap = dcap;
	}

	public String getDindirizzo() {
		return dindirizzo;
	}

	public void setDindirizzo(String dindirizzo) {
		this.dindirizzo = dindirizzo;
	}

	public String getDloc() {
		return dloc;
	}

	public void setDloc(String dloc) {
		this.dloc = dloc;
	}

	public String getDnome() {
		return dnome;
	}

	public void setDnome(String dnome) {
		this.dnome = dnome;
	}

	public String getDprov() {
		return dprov;
	}

	public void setDprov(String dprov) {
		this.dprov = dprov;
	}

	public String getNprog() {
		return nprog;
	}

	public void setNprog(String nprog) {
		this.nprog = nprog;
	}

	public String getPiva() {
		return piva;
	}

	public void setPiva(String piva) {
		this.piva = piva;
	}

	public String getRs2() {
		return rs2;
	}

	public void setRs2(String rs2) {
		this.rs2 = rs2;
	}*/
}
