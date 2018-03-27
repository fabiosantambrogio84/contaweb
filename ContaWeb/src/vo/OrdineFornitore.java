package vo;

import java.util.Date;
import java.util.Vector;

public class OrdineFornitore extends VOElement {

	private static final long serialVersionUID = 2767346264394280612L;

	private Integer numeroProgressivo = null;
	private Integer annoContabile = null;
	private Date dataCreazione = null;
	private Integer idFornitore = null;
	private String note = null;
	private Boolean spedito = null;
	
	private Fornitore fornitore = null;
	private Vector<DettaglioOrdineFornitore> dettagliOrdineFornitore = null;
	
	public Vector getDettagliOrdineFornitore() {
		return dettagliOrdineFornitore;
	}

	public void setDettagliOrdineFornitore(Vector dettagliOrdineFornitore) {
		this.dettagliOrdineFornitore = dettagliOrdineFornitore;
	}

	public Integer getNumeroProgressivo() {
		return numeroProgressivo;
	}

	public void setNumeroProgressivo(Integer numeroProgressivo) {
		this.numeroProgressivo = numeroProgressivo;
	}

	public Integer getAnnoContabile() {
		return annoContabile;
	}

	public void setAnnoContabile(Integer annoContabile) {
		this.annoContabile = annoContabile;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Integer getIdFornitore() {
		return idFornitore;
	}

	public void setIdFornitore(Integer idFornitore) {
		this.idFornitore = idFornitore;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Fornitore getFornitore() {
		return fornitore;
	}

	public void setFornitore(Fornitore fornitore) {
		this.fornitore = fornitore;
	}

	public Integer getStato() {
		if (getSpedito() != null && getSpedito() == true)
			return STORED;
		return NORMAL;
	}
	
	public String getProgressivoCompleto() {
		return numeroProgressivo.toString() + "/" + annoContabile.toString();
	}

	public Boolean getSpedito() {
		return spedito;
	}

	public void setSpedito(Boolean spedito) {
		this.spedito = spedito;
	}

}
