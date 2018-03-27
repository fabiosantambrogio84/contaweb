package vo;

import java.math.BigDecimal;
import java.util.Collection;

public class TotaliStatistica  {
	private BigDecimal totale = null;
	private BigDecimal qtaVenduta = null;
	private BigDecimal media = null;
	private Collection dettagli = null;

	public BigDecimal getMedia() {
		return media;
	}
	public void setMedia(BigDecimal media) {
		this.media = media;
	}
	public BigDecimal getTotale() {
		return totale;
	}
	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}
	public Collection getDettagli() {
		return dettagli;
	}
	public void setDettagli(Collection dettagli) {
		this.dettagli = dettagli;
	}
	public BigDecimal getQtaVenduta() {
		return qtaVenduta;
	}
	public void setQtaVenduta(BigDecimal qtaVenduta) {
		this.qtaVenduta = qtaVenduta;
	}
}
