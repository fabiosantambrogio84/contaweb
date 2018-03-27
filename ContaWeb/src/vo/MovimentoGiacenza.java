 package vo;

import java.math.BigDecimal;
import java.util.Date;

public class MovimentoGiacenza extends VOElement {

	private static final long serialVersionUID = -7089278446122908253L;
	
	public static final Integer INSERIMENTO = 0;
	public static final Integer AGGIUNTA = 1;
	public static final Integer RIMOSSA = 2;
	public static final Integer AGGIUNTA_PER_CANCELLAZIONE_DOC = 3;
	public static final Integer RIMOSSA_PER_CANCELLAZIONE_DOC = 4;

	protected Integer idGiacenza = null;
	protected Giacenza giacenza = null;
	protected Integer tipoMovim = null;
	protected BigDecimal qta = null;
	protected BigDecimal qtaDopoMovim = null;
	protected Date data = null;
	protected Integer idDDT = null;
	protected DDT ddt = null;
	protected Integer idBollaAcquisto = null;
	protected BollaAcquisto bollaAcquisto = null;

	public BollaAcquisto getBollaAcquisto() {
		return bollaAcquisto;
	}
	public void setBollaAcquisto(BollaAcquisto bollaAcquisto) {
		this.bollaAcquisto = bollaAcquisto;
	}
	public Integer getIdBollaAcquisto() {
		return idBollaAcquisto;
	}
	public void setIdBollaAcquisto(Integer idBollaAcquisto) {
		this.idBollaAcquisto = idBollaAcquisto;
	}
	public DDT getDdt() {
		return ddt;
	}
	public void setDdt(DDT ddt) {
		this.ddt = ddt;
	}
	public Giacenza getGiacenza() {
		return giacenza;
	}
	public void setGiacenza(Giacenza giacenza) {
		this.giacenza = giacenza;
	}
	public Integer getIdDDT() {
		return idDDT;
	}
	public void setIdDDT(Integer idDDT) {
		this.idDDT = idDDT;
	}
	public Integer getIdGiacenza() {
		return idGiacenza;
	}
	public void setIdGiacenza(Integer idGiacenza) {
		this.idGiacenza = idGiacenza;
	}
	public BigDecimal getQta() {
		return qta;
	}
	public void setQta(BigDecimal qta) {
		this.qta = qta;
	}
	public BigDecimal getQtaDopoMovim() {
		return qtaDopoMovim;
	}
	public void setQtaDopoMovim(BigDecimal qtaDopoMovim) {
		this.qtaDopoMovim = qtaDopoMovim;
	}
	public Integer getTipoMovim() {
		return tipoMovim;
	}
	public void setTipoMovim(Integer tipoMovim) {
		this.tipoMovim = tipoMovim;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
}
