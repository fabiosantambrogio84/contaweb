package vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

public class Cliente extends VOElement {

	private static final long serialVersionUID = -4652820330359236949L;

	private String rs;
	private String rs2;
	private String indirizzo;
	private String localita;
	private String prov;
	private String cap;
	private String piva;
	private String email;
	private String emailPec;
	private Integer prefissoTelefono;
	private String numeroTelefono;
	private String codiceFiscale;
	private String bancaDescrizione;
	private String bancaABI;
	private String bancaCAB;
	private String bancaCC;
	private BigDecimal fido;
	private Integer idPagamento;
	private Pagamento pagamento;
	private Date dataInserimento;
	private Boolean bloccaDDT;
	private Boolean nascondiPrezzi;
	private Vector puntiConsegna;
	private Vector listiniAssociati;
	private Vector telefonate;
	private String note;
	private Integer formatoConad;
	private Integer idAgente = null;
	private Integer idAutista = null;
	private Autista autista;
	private Agente agente;

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Boolean getBloccaDDT() {
		return bloccaDDT;
	}
	public Integer getStato() {
		if (bloccaDDT)
			return NOT_ACTIVE;
		return NORMAL;
	}
	public void setBloccaDDT(Boolean bloccaDDT) {
		this.bloccaDDT = bloccaDDT;
	}
	public Vector getListiniAssociati() {
		return listiniAssociati;
	}
	public void setListiniAssociati(Vector listiniAssociati) {
		this.listiniAssociati = listiniAssociati;
	}
	public Boolean getNascondiPrezzi() {
		return nascondiPrezzi;
	}
	public void setNascondiPrezzi(Boolean nascondiPrezzi) {
		this.nascondiPrezzi = nascondiPrezzi;
	}
	public Vector getPuntiConsegna() {
		return puntiConsegna;
	}
	public void setPuntiConsegna(Vector puntiConsegna) {
		this.puntiConsegna = puntiConsegna;
	}
	public void setFido(BigDecimal fido) {
		this.fido = fido;
	}
	public String getBancaABI() {
		return bancaABI;
	}
	public void setBancaABI(String bancaABI) {
		this.bancaABI = bancaABI;
	}
	public String getBancaCAB() {
		return bancaCAB;
	}
	public void setBancaCAB(String bancaCAB) {
		this.bancaCAB = bancaCAB;
	}
	public String getBancaCC() {
		return bancaCC;
	}
	public void setBancaCC(String bancaCC) {
		this.bancaCC = bancaCC;
	}
	public String getBancaDescrizione() {
		return bancaDescrizione;
	}
	public void setBancaDescrizione(String bancaDescrizione) {
		this.bancaDescrizione = bancaDescrizione;
	}
	public boolean isBloccaDDT() {
		return bloccaDDT;
	}
	public void setBloccaDDT(boolean bloccaDDT) {
		this.bloccaDDT = bloccaDDT;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public Date getDataInserimento() {
		return dataInserimento;
	}
	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public BigDecimal getFido() {
		return fido;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getLocalita() {
		return localita;
	}
	public void setLocalita(String localita) {
		this.localita = localita;
	}
	public boolean isNascondiPrezzi() {
		return nascondiPrezzi;
	}
	public void setNascondiPrezzi(boolean nascondiPrezzi) {
		this.nascondiPrezzi = nascondiPrezzi;
	}
	public String getPiva() {
		return piva;
	}
	public void setPiva(String piva) {
		this.piva = piva;
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
	public String getRs2() {
		return rs2;
	}
	public void setRs2(String rs2) {
		this.rs2 = rs2;
	}
	public Integer getIdPagamento() {
		return idPagamento;
	}
	public void setIdPagamento(Integer idPagamento) {
		this.idPagamento = idPagamento;
	}
	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}
	public Pagamento getPagamento() {
		return pagamento;
	}
	public String getNumeroTelefono() {
		return numeroTelefono;
	}
	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}
	public Vector getTelefonate() {
		return telefonate;
	}
	public void setTelefonate(Vector telefonate) {
		this.telefonate = telefonate;
	}
	public Integer getPrefissoTelefono() {
		if (prefissoTelefono == null)
			return 39;
		
		return prefissoTelefono;
	}
	public void setPrefissoTelefono(Integer prefissoTelefono) {
		this.prefissoTelefono = prefissoTelefono;
	}
	
	public String getTelefonoCompleto() {
		return prefissoTelefono + numeroTelefono;
	}
	public String getEmailPec() {
		return emailPec;
	}
	public void setEmailPec(String emailPec) {
		this.emailPec = emailPec;
	}
	public Integer getFormatoConad() {
		return formatoConad;
	}
	public void setFormatoConad(Integer formatoConad) {
		this.formatoConad = formatoConad;
	}
	public Integer getIdAutista() {
		return idAutista;
	}
	public void setIdAutista(Integer idAutista) {
		this.idAutista = idAutista;
	}
	public Autista getAutista() {
		return autista;
	}
	public void setAutista(Autista autista) {
		this.autista = autista;
	}
	public Integer getIdAgente() {
		return idAgente;
	}
	public void setIdAgente(Integer idAgente) {
		this.idAgente = idAgente;
	}
	public Agente getAgente() {
		return agente;
	}
	public void setAgente(Agente agente) {
		this.agente = agente;
	}
}
