 package vo;

import java.math.BigDecimal;
import java.util.*;

public class Giacenza extends VOElement {

	private static final long serialVersionUID = -7089278456122908253L;

	protected BigDecimal qta = null;
	protected Date dataScadenza = null;
	protected String lotto = null;
	protected Vector movimenti = null;
	protected Articolo articolo = null;
	protected Integer idArticolo = null;

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
	public BigDecimal getQta() {
		return qta;
	}
	public void setQta(BigDecimal qta) {
		this.qta = qta;
	}
	public Date getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public String getLotto() {
		return lotto;
	}
	public void setLotto(String lotto) {
		this.lotto = lotto;
	}
	public Vector getMovimenti() {
		return movimenti;
	}
	public void setMovimenti(Vector movimenti) {
		this.movimenti = movimenti;
	}
}
