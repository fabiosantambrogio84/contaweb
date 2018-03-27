 package vo;

import java.math.BigDecimal;
import java.util.*;

public class Sconto extends VOElement {

	private static final long serialVersionUID = -8089278456122908253L;

	protected BigDecimal sconto = null;
	protected Date dataScontoDal = null;
	protected Date dataScontoAl = null;

	protected Fornitore fornitore = null;
	protected Integer idFornitore = null;
	protected Cliente cliente = null;
	protected Integer idCliente = null;
	protected Articolo articolo = null;
	protected Integer idArticolo = null;
	
	public Integer getStato() {
		
		if (dataScontoAl != null && dataScontoAl.before(new Date()))
			return WARNING;
		
		return NORMAL;
	}

	public Articolo getArticolo() {
		return articolo;
	}
	public void setArticolo(Articolo articolo) {
		this.articolo = articolo;
	}
	public Integer getIdArticolo() {
		return idArticolo;
	}
	public void setIdArticolo(Integer idArticolo) {
		this.idArticolo = idArticolo;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.idCliente = cliente.getId();
		this.cliente = cliente;
	}
	public Date getDataScontoAl() {
		return dataScontoAl;
	}
	public void setDataScontoAl(Date dataScontoAl) {
		this.dataScontoAl = dataScontoAl;
	}
	public Date getDataScontoDal() {
		return dataScontoDal;
	}
	public void setDataScontoDal(Date dataScontoDal) {
		this.dataScontoDal = dataScontoDal;
	}
	public Integer getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	public BigDecimal getSconto() {
		return sconto;
	}
	public void setSconto(BigDecimal sconto) {
		this.sconto = sconto;
	}

	public String getTipoSconto() {
		if (idArticolo == null)
			return "Fornitore";
		else
			return "Articolo";
	}
	public String getDescrizioneSconto() {
		if (idArticolo == null)
			return fornitore.getDescrizione();
		else
			return articolo.getDescrizione();
	}
	public Fornitore getFornitore() {
		return fornitore;
	}
	public void setFornitore(Fornitore fornitore) {
		this.idFornitore = fornitore.getId();
		this.fornitore = fornitore;
	}
	public void setIdFornitore(Integer idFornitore) {
		this.idFornitore = idFornitore;
	}
	public Integer getIdFornitore() {
		return idFornitore;
	}
}
