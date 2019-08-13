package vo;

import java.math.BigDecimal;

public class ReportGiacenza extends VOElement {
	
	private static final long serialVersionUID = -4841498650059851157L;

	private String codiceArticolo = null;
	private String descrizione = null;
	private BigDecimal qta = null;
	private Boolean attivo = null;
	private String descrizioneFornitore;

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

	public Boolean getAttivo() {
		return attivo;
	}

	public void setAttivo(Boolean attivo) {
		this.attivo = attivo;
	}
	
	public String getDescrizioneFornitore() {
		return descrizioneFornitore;
	}
	public void setDescrizioneFornitore(String descrizioneFornitore) {
		this.descrizioneFornitore = descrizioneFornitore;
	}
}
