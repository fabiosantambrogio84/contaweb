package applet.barcode;

import java.math.BigDecimal;

import vo.PrezzoConSconto;

public class Barcode {
	
	private String barcodeString;
	private PrezzoConSconto datiAssociati;
	private BigDecimal qtaLetta;
	private boolean isNumeroLotto;

	public String getBarcodeString() {
		return barcodeString;
	}

	public void setBarcodeString(String barcodeString) {
		this.barcodeString = barcodeString;
	}

	public boolean isNumeroLotto() {
		return isNumeroLotto;
	}

	public void setNumeroLotto(boolean isNumeroLotto) {
		this.isNumeroLotto = isNumeroLotto;
	}

	public BigDecimal getQtaLetta() {
		return qtaLetta;
	}

	public void setQtaLetta(BigDecimal qtaLetta) {
		this.qtaLetta = qtaLetta;
	}

	public PrezzoConSconto getDatiAssociati() {
		return datiAssociati;
	}

	public void setDatiAssociati(PrezzoConSconto datiAssociati) {
		this.datiAssociati = datiAssociati;
	}

}
