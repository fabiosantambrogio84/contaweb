package vo;

import java.math.BigDecimal;

public class Iva extends VOElement {

	private static final long serialVersionUID = 4932182164185778296L;

	private BigDecimal valore = null;
	
	public BigDecimal getValore() {
		return valore;
	}
	
	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}
	
	public String toString() {
		if(valore == null) return "0";
		return getValore().toString();
	}

}
