package vo;

import java.math.BigDecimal;

public class ReportGiacenza extends VOElement {
	
	private static final long serialVersionUID = -4841498650059851157L;

	private String codiceArticolo = null;
	private String descrizione = null;
	private BigDecimal qta = null;

	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public BigDecimal getQta() {
		return qta;
	}
	public void setQta(BigDecimal qta) {
		this.qta = qta;
	}
	public String getCodiceArticolo() {
		return codiceArticolo;
	}
	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

}
