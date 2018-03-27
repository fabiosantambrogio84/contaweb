package vo;

import java.math.BigDecimal;

public class PrezzoConSconto extends Prezzo {

	private static final long serialVersionUID = -7316551651905948288L;
	
	private BigDecimal sconto = null;

	public BigDecimal getSconto() {
		return sconto;
	}

	public void setSconto(BigDecimal sconto) {
		this.sconto = sconto;
	}
	
}
