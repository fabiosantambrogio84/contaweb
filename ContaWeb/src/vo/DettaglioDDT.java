package vo;

import java.math.BigDecimal;
import java.util.Vector;

public class DettaglioDDT extends VOElement {

	private static final long serialVersionUID = -7302540693232770767L;

	private Integer idArticolo;
	private String codiceArticolo;
	private String descrizioneArticolo;
	private String um;
	private BigDecimal qta;
	private BigDecimal prezzo;
	private BigDecimal sconto;
	private BigDecimal totale;
	private BigDecimal iva;
	private String lotto;
	private DDT ddt;
	private Integer idDDT;
	private Articolo articolo;
	private Integer pezzi;
	
	private Vector evasioniOrdini;
	
	public Integer getPezzi() {
		return pezzi;
	}

	public void setPezzi(Integer pezzi) {
		this.pezzi = pezzi;
	}

	public BigDecimal calcolaImponibile() {
		BigDecimal totale = new BigDecimal(0);
		
		totale = prezzo.multiply(qta).setScale(2,BigDecimal.ROUND_HALF_UP);
		BigDecimal totaleSconto = totale.multiply(sconto.divide(new BigDecimal(100).setScale(2,BigDecimal.ROUND_HALF_UP))).setScale(2,BigDecimal.ROUND_HALF_UP);
		totale = totale.subtract(totaleSconto);
		
		return totale;
	}
	
	public Articolo getArticolo() {
		return articolo;
	}

	public void setArticolo(Articolo articolo) {
		this.articolo = articolo;
	}

	public DDT getDdt() {
		return ddt;
	}

	public void setDdt(DDT ddt) {
		this.ddt = ddt;
	}

	public Integer getIdDDT() {
		return idDDT;
	}

	public void setIdDDT(Integer idDDT) {
		this.idDDT = idDDT;
	}

	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

	public String getDescrizioneArticolo() {
		return descrizioneArticolo;
	}

	public void setDescrizioneArticolo(String descrizioneArticolo) {
		this.descrizioneArticolo = descrizioneArticolo;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public String getLotto() {
		return lotto;
	}

	public void setLotto(String lotto) {
		this.lotto = lotto;
	}
	
	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	public BigDecimal getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo.setScale(2);
	}

	public BigDecimal getQta() {
		return qta;
	}

	public void setQta(BigDecimal qta) {
		this.qta = qta.setScale(3);
	}

	public BigDecimal getSconto() {
		return sconto;
	}

	public void setSconto(BigDecimal sconto) {
		this.sconto = sconto;
	}

	public BigDecimal getTotale() {
		return totale;
	}

	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}

	public Vector getEvasioniOrdini() {
		return evasioniOrdini;
	}

	public void setEvasioniOrdini(Vector evasioniOrdini) {
		this.evasioniOrdini = evasioniOrdini;
	}

	public Integer getIdArticolo() {
		return idArticolo;
	}

	public void setIdArticolo(Integer idArticolo) {
		this.idArticolo = idArticolo;
	}
}
