package vo;

public class DettaglioOrdine extends VOElement {

	private static final long serialVersionUID = 2767346164394280662L;

	private Integer idOrdine = null;
	private Ordine ordine = null;
	private Integer idArticolo = null;
	private Articolo articolo = null;
	private Integer pezziOrdinati = null;
	private Integer pezziDaEvadere = null;
	private Integer pezziDaOrdinare = null;

	public Integer getPezziDaOrdinare() {
		return pezziDaOrdinare;
	}
	public void setPezziDaOrdinare(Integer pezziDaOrdinare) {
		if (pezziDaOrdinare < 0)
			this.pezziDaOrdinare = 0;
		else
			this.pezziDaOrdinare = pezziDaOrdinare;
	}
	public Integer getPezziDaEvadere() {
		return pezziDaEvadere;
	}
	public void setPezziDaEvadere(Integer pezziDaEvadere) {
		this.pezziDaEvadere = pezziDaEvadere;
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
	public Integer getIdOrdine() {
		return idOrdine;
	}
	public void setIdOrdine(Integer idOrdine) {
		this.idOrdine = idOrdine;
	}
	public Ordine getOrdine() {
		return ordine;
	}
	public void setOrdine(Ordine ordine) {
		this.ordine = ordine;
	}
	public Integer getPezziOrdinati() {
		return pezziOrdinati;
	}
	public void setPezziOrdinati(Integer pezziOrdinati) {
		this.pezziOrdinati = pezziOrdinati;
	}
}
