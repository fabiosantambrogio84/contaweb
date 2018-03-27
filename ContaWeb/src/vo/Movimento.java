package vo;

import java.math.BigDecimal;
import java.util.Date;

public class Movimento extends VOElement {
	private static final long serialVersionUID = -7089278446122908253L;
	
	private Integer id;
	private Integer idCliente;
	private BigDecimal importo;
	private Integer idDDT;
	private Integer idFattura;
	private Date	data;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public Integer getIdDDT() {
		return idDDT;
	}
	public void setIdDDT(Integer idDDT) {
		this.idDDT = idDDT;
	}
	public Integer getIdFattura() {
		return idFattura;
	}
	public void setIdFattura(Integer idFattura) {
		this.idFattura = idFattura;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
}
