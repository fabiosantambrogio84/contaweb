package vo;

import java.util.Date;
import java.util.Vector;
import java.util.List;
import vo.DettaglioOrdine;

public class Ordine extends VOElement {

	public static final Integer ORARIO_MATTINA = 1;
	public static final Integer ORARIO_POMERIGGIO = 0;
	
	static public final int DA_EVADERE = 0;
	static public final int PARZIALMENTE_EVASO = 2;
	static public final int EVASO = 1;
	private static final long serialVersionUID = 2767346264394280662L;

	private Integer numeroProgressivo = null;
	private Integer annoContabile = null;
	private Integer idCliente = null;
	private Integer idAutista = null;
	private Integer statoOrdine = null;
	private Integer idAgente = null;
	private Integer idPuntoConsegna = null;
	private Date dataCreazione = null;
	private Date dataSpedizione = null;
	private Integer orarioSpedizione = null;
	private String note = null;
	private Vector dettagliOrdine = null;
	private Vector<DettaglioOrdine> dettaglioOrdine = null;
	private Cliente cliente = null;
	private Autista autista = null;
	private Agente agente = null;
	private PuntoConsegna puntoConsegna = null;

	public Integer getIdPuntoConsegna() {
		return idPuntoConsegna;
	}
	public void setIdPuntoConsegna(Integer idPuntoConsegna) {
		this.idPuntoConsegna = idPuntoConsegna;
	}
	public PuntoConsegna getPuntoConsegna() {
		return puntoConsegna;
	}
	public void setPuntoConsegna(PuntoConsegna puntoConsegna) {
		this.puntoConsegna = puntoConsegna;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Autista getAutista() {
		return autista;
	}
	public void setAutista(Autista autista) {
		this.autista = autista;
	} 
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public Date getDataSpedizione() {
		return dataSpedizione;
	}
	public void setDataSpedizione(Date dataSpedizione) {
		this.dataSpedizione = dataSpedizione;
	}
	public Integer getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	public Integer getIdAutista() {
		return idAutista;
	}
	public void setIdAutista(Integer idAutista) {
		this.idAutista = idAutista;
	}
	public Integer getOrarioSpedizione() {
		return orarioSpedizione;
	}
	public void setOrarioSpedizione(Integer orarioSpedizione) {
		this.orarioSpedizione = orarioSpedizione;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Vector getDettagliOrdine() {
		return dettagliOrdine;
	}
	public void setDettagliOrdine(Vector dettagliOrdine) {
		this.dettagliOrdine = dettagliOrdine;
	}
	
	public Vector<DettaglioOrdine> getDettaglioOrdine() {
		return dettaglioOrdine;
	}
	public void setDettaglioOrdine(Vector<DettaglioOrdine> dettaglioOrdine) {
		this.dettaglioOrdine = dettaglioOrdine;
	}
	public Integer getStato() {
	    /* ***
		if (getDettagliOrdine() != null)
			for(int i=0; i< dettagliOrdine.size(); ++i)
				if (((DettaglioOrdine)dettagliOrdine.get(i)).getPezziDaEvadere()>0)
					return WARNING;
		*** */		
		
		if(getStatoOrdine() == DA_EVADERE) return WARNING;
		else if(getStatoOrdine() == PARZIALMENTE_EVASO) return 4;
		else if(getStatoOrdine() == EVASO) return NORMAL;
					
		return NORMAL;
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
	public String getProgressivoCompleto() {
		if (numeroProgressivo != null && annoContabile != null)
			return numeroProgressivo.toString() + "/" + annoContabile.toString();
		else
			return null;
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
	public Integer getStatoOrdine() {
		return statoOrdine;
	}
	public void setStatoOrdine(Integer statoOrdine) {
		this.statoOrdine = statoOrdine;
	}
}
