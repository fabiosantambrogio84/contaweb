package vo;

import java.math.BigDecimal;

public class Pagamento extends VOElement {

	private static final long serialVersionUID = 4932182164185788296L;

	private String descrizione = null;
	private Integer scadenza = null;
	private BigDecimal amount = BigDecimal.ZERO;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Integer getScadenza() {
		return scadenza;
	}

	public void setScadenza(Integer scadenza) {
		this.scadenza = scadenza;
	}
	
	public void setAmount(BigDecimal amount)
	{
		this.amount = amount;
	}
	
	public BigDecimal getAmount()
	{
		return amount;
	}

}
