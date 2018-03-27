 package vo;

import java.math.BigDecimal;
import java.util.Date;

public class DettaglioBollaAcquisto extends VOElement {

	private static final long serialVersionUID = -9089378451122908259L;

	private Integer idBollaAcquisto = null;
	private BollaAcquisto bollaAcquisto = null;
	private Integer idArticolo = null;
	private Articolo articolo = null;
	private BigDecimal qta = null;
	private BigDecimal iva = new BigDecimal(0);
	private BigDecimal prezzo = null;
	private BigDecimal sconto = null;
	private String lotto = null;
	private Date dataScadenza = null;
	
	public Date getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public Integer getIdArticolo() {
		return idArticolo;
	}
	public void setIdArticolo(Integer idArticolo) {
		this.idArticolo = idArticolo;
	}
	public Integer getIdBollaAcquisto() {
		return idBollaAcquisto;
	}
	public void setIdBollaAcquisto(Integer idBollaAcquisto) {
		this.idBollaAcquisto = idBollaAcquisto;
	}
	public String getLotto() {
		return lotto;
	}
	public void setLotto(String lotto) {
		this.lotto = lotto;
	}
	public BigDecimal getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}
	public BigDecimal getQta() {
		return qta;
	}
	public void setQta(BigDecimal qta) {
		this.qta = qta;
	}
	public BigDecimal getSconto() {
		return sconto == null ? new BigDecimal(0) : sconto;
	}
	public void setSconto(BigDecimal sconto) {
		this.sconto = sconto;
	}
	public Articolo getArticolo() {
		return articolo;
	}
	public void setArticolo(Articolo articolo) {
		this.articolo = articolo;
	}
	public BollaAcquisto getBollaAcquisto() {
		return bollaAcquisto;
	}
	public void setBollaAcquisto(BollaAcquisto bollaAcquisto) {
		this.bollaAcquisto = bollaAcquisto;
	}
	
	public BigDecimal calcolaImponibile() {
		BigDecimal totale = new BigDecimal(0);
		
		totale = prezzo.multiply(qta).setScale(2,BigDecimal.ROUND_HALF_UP);
		if(sconto == null) sconto = new BigDecimal(0);
		BigDecimal totaleSconto = totale.multiply(sconto.divide(new BigDecimal(100).setScale(2,BigDecimal.ROUND_HALF_UP))).setScale(2,BigDecimal.ROUND_HALF_UP);
		totale = totale.subtract(totaleSconto);
		
		return totale;
	}
	public BigDecimal getIva() {
		return iva;
	}
	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}
}
