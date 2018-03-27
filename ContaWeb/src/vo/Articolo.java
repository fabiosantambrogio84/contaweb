 package vo;

import java.math.BigDecimal;
import java.math.BigInteger;
//import java.sql.Timestamp;
import java.util.*;

public class Articolo extends VOElement {

	private static final long serialVersionUID = -9089378456122908253L;
	
	private Integer idArticolo;
	private String codiceArticolo;
	private String descrizione;
	private String um;
	private Fornitore fornitore;
	private Iva iva;
	private Integer idIva = null;
	private Integer idFornitore = null;
	private Integer idCategoria;
	private Integer idDestinazione;
	private Date data = null;
	private Vector prezzi = null;
	private String barCode = null;
	private Boolean completeBarCode = null;
	private BigDecimal qtaPredefinita = null;
	private BigDecimal prezzoAcquisto = null;
	private Integer ggScadenza = null;
	private Boolean attivo = null;
	private Boolean web = null; 
	private Date dataAggiornamento = null;
	private String immagine1;
	private String immagine2;
	private String immagine3;
	private String immagine4;
	private String immagine5;
	private String immagine6;
	private BigDecimal prezzoConSconto;
	
	public Boolean getAttivo() {
		return attivo;
	}

	public void setAttivo(Boolean attivo) {
		this.attivo = attivo;
	}

	public Integer getIdDestinazione() {
		return idDestinazione;
	}

	public void setIdDestinazione(Integer idDestinazione) {
		this.idDestinazione = idDestinazione;
	}
	
	public String getDescCompleta() {
		return codiceArticolo + " - " + descrizione;
	}
	
	public String getDescrizioneOrdini() {
		String desc = descrizione;
		if (descrizione.length() > 42)
			desc = descrizione.substring(0, 35) + "..." + descrizione.substring(descrizione.length() - 5, descrizione.length());
		return desc;
	}
	
	public Vector getPrezzi() {
		return prezzi;
	}
	
	public void setPrezzi(Vector prezzi) {
		this.prezzi = prezzi;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Fornitore getFornitore() {
		return fornitore;
	}

	public void setFornitore(Fornitore fornitore) {
		this.fornitore = fornitore;
	}

	public Iva getIva() {
		return iva;
	}

	public void setIva(Iva iva) {
		this.iva = iva;
	}

	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public boolean isCompleteBarCode() {
		return completeBarCode;
	}

	public void setCompleteBarCode(boolean completeBarCode) {
		this.completeBarCode = completeBarCode;
	}

	public BigDecimal getQtaPredefinita() {
		return qtaPredefinita;
	}

	public void setQtaPredefinita(BigDecimal qtaPredefinita) {
		this.qtaPredefinita = qtaPredefinita;
	}

	public Integer getIdIva() {
		return idIva;
	}
	
	public Integer getIdCategoria() {
		return idCategoria;
	}
	
	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}
	
	public Integer getIdArticolo() {
		return idArticolo;
	}
	
	public void setIdArticolo(Integer idArticolo) {
		this.idArticolo = idArticolo;
	}

	public void setIdIva(Integer idIva) {
		this.idIva = idIva;
	}

	public Integer getIdFornitore() {
		return idFornitore;
	}

	public void setIdFornitore(Integer idFornitore) {
		this.idFornitore = idFornitore;
	}

	public Integer getGgScadenza() {
		return ggScadenza;
	}

	public void setGgScadenza(Integer ggScadenza) {
		this.ggScadenza = ggScadenza;
	}

	public BigDecimal getPrezzoAcquisto() {
		return prezzoAcquisto;
	}

	public void setPrezzoAcquisto(BigDecimal prezzoAcquisto) {
		this.prezzoAcquisto = prezzoAcquisto;
	}
	
	public Integer getStato() {
		if (attivo !=null && !attivo)
			return NOT_ACTIVE;
		return NORMAL;
	}

	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

	public Boolean getWeb() {
		return web;
	}

	public void setWeb(Boolean web) {
		this.web = web;
	}

	public Date getDataAggiornamento() {
		return dataAggiornamento;
	}

	public void setDataAggiornamento(Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}

	public String getImmagine1() {
		return immagine1;
	}

	public void setImmagine1(String immagine1) {
		this.immagine1 = immagine1;
	}
	
	public String getImmagine2() {
		return immagine2;
	}

	public void setImmagine2(String immagine2) {
		this.immagine2 = immagine2;
	}

	public String getImmagine3() {
		return immagine3;
	}

	public void setImmagine3(String immagine3) {
		this.immagine3 = immagine3;
	}

	public String getImmagine4() {
		return immagine4;
	}

	public void setImmagine4(String immagine4) {
		this.immagine4 = immagine4;
	}

	public String getImmagine5() {
		return immagine5;
	}

	public void setImmagine5(String immagine5) {
		this.immagine5 = immagine5;
	}

	public String getImmagine6() {
		return immagine6;
	}

	public void setImmagine6(String immagine6) {
		this.immagine6 = immagine6;
	}

	public BigDecimal getPrezzoConSconto() {
		return prezzoConSconto;
	}

	public void setPrezzoConSconto(BigDecimal prezzoConSconto) {
		this.prezzoConSconto = prezzoConSconto;
	}

}
