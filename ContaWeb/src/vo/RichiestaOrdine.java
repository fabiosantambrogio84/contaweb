package vo;

public class RichiestaOrdine extends VOElement {

	private static final long serialVersionUID = 2767346114394280662L;
	
	private Integer idDettaglioOrdineFornitore = null;
	private Integer idDettaglioOrdine = null;
	private Integer qta = null;

	private DettaglioOrdine dettaglioOrdine = null;
	private DettaglioOrdineFornitore dettaglioOrdineFornitore = null;
	
	public Integer getIdDettaglioOrdine() {
		return idDettaglioOrdine;
	}
	public void setIdDettaglioOrdine(Integer idOrdine) {
		this.idDettaglioOrdine = idOrdine;
	}
	public Integer getQta() {
		return qta;
	}
	public void setQta(Integer qta) {
		this.qta = qta;
	}
	public Integer getIdDettaglioOrdineFornitore() {
		return idDettaglioOrdineFornitore;
	}
	public void setIdDettaglioOrdineFornitore(Integer idOrdineFornitore) {
		this.idDettaglioOrdineFornitore = idOrdineFornitore;
	}
	public DettaglioOrdine getDettaglioOrdine() {
		return dettaglioOrdine;
	}
	public void setDettaglioOrdine(DettaglioOrdine dettaglioOrdine) {
		this.dettaglioOrdine = dettaglioOrdine;
	}
	public DettaglioOrdineFornitore getDettaglioOrdineFornitore() {
		return dettaglioOrdineFornitore;
	}
	public void setDettaglioOrdineFornitore(
			DettaglioOrdineFornitore dettaglioOrdineFornitore) {
		this.dettaglioOrdineFornitore = dettaglioOrdineFornitore;
	}
}
