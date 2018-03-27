package vo;

import java.math.*;

public class Prezzo extends VOElement {

	private static final long serialVersionUID = 5646650467816979084L;

	private Articolo articolo;
	private Listino listino;
	
	private Integer idArticolo;
	private int idListino;
	
	private BigDecimal prezzo;
	
	public Articolo getArticolo() {
		return articolo;
	}
	
	public int getIdListino() {
		return idListino;
	}
	
	public Integer getIdArticolo() {
		return idArticolo;
	}
	
	public void setIdListino(int idListino) {
		this.idListino = idListino;
	}
	
	public void setIdArticolo(Integer idArticolo) {
		this.idArticolo = idArticolo;
	}

	public void setArticolo(Articolo articolo) {
		this.articolo = articolo;
		if (articolo != null)
			this.idArticolo = articolo.getId();
	}

	public Listino getListino() {
		return listino;
	}

	public void setListino(Listino listino) {
		this.listino = listino;
		if (listino != null)
			this.idListino = (Integer)listino.getId();
	}

	public BigDecimal getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}
}
