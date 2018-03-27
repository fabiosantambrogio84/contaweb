package vo;

public class EvasioneOrdine extends VOElement {

	private static final long serialVersionUID = -7302540693232770767L;

	private Integer idDettaglioOrdine = null;
	private Integer idDettaglioDDT = null;
	
	private DettaglioOrdine dettaglioOrdine = null;
	private DettaglioDDT dettaglioDDT = null;

	public Integer getIdDettaglioOrdine() {
		return idDettaglioOrdine;
	}
	public void setIdDettaglioOrdine(Integer idDettaglioOrdine) {
		this.idDettaglioOrdine = idDettaglioOrdine;
	}
	public Integer getIdDettaglioDDT() {
		return idDettaglioDDT;
	}
	public void setIdDettaglioDDT(Integer idDettaglioDDT) {
		this.idDettaglioDDT = idDettaglioDDT;
	}
	public DettaglioOrdine getDettaglioOrdine() {
		return dettaglioOrdine;
	}
	public void setDettaglioOrdine(DettaglioOrdine dettaglioOrdine) {
		this.dettaglioOrdine = dettaglioOrdine;
	}
	public DettaglioDDT getDettaglioDDT() {
		return dettaglioDDT;
	}
	public void setDettaglioDDT(DettaglioDDT dettaglioDDT) {
		this.dettaglioDDT = dettaglioDDT;
	}
}
