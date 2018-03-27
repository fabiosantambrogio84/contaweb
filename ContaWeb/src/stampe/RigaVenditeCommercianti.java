package stampe;

import java.math.BigDecimal;

import vo.Cliente;

public class RigaVenditeCommercianti {
	
	private Cliente cliente;
	
	private BigDecimal imponibile1;
	private BigDecimal imponibile2;
	private BigDecimal imponibile3;
	
	private BigDecimal iva1;
	private BigDecimal iva2;
	private BigDecimal iva3;
	
	private BigDecimal imposta1;
	private BigDecimal imposta2;
	private BigDecimal imposta3;
	
	private BigDecimal totaleFattura;

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public BigDecimal getImponibile1() {
		return imponibile1;
	}

	public void setImponibile1(BigDecimal imponibile1) {
		this.imponibile1 = imponibile1;
	}

	public BigDecimal getImponibile2() {
		return imponibile2;
	}

	public void setImponibile2(BigDecimal imponibile2) {
		this.imponibile2 = imponibile2;
	}

	public BigDecimal getImponibile3() {
		return imponibile3;
	}

	public void setImponibile3(BigDecimal imponibile3) {
		this.imponibile3 = imponibile3;
	}

	public BigDecimal getIva1() {
		return iva1;
	}

	public void setIva1(BigDecimal iva1) {
		this.iva1 = iva1;
	}

	public BigDecimal getIva2() {
		return iva2;
	}

	public void setIva2(BigDecimal iva2) {
		this.iva2 = iva2;
	}

	public BigDecimal getIva3() {
		return iva3;
	}

	public void setIva3(BigDecimal iva3) {
		this.iva3 = iva3;
	}

	public BigDecimal getImposta1() {
		return imposta1;
	}

	public void setImposta1(BigDecimal imposta1) {
		this.imposta1 = imposta1;
	}

	public BigDecimal getImposta2() {
		return imposta2;
	}

	public void setImposta2(BigDecimal imposta2) {
		this.imposta2 = imposta2;
	}

	public BigDecimal getImposta3() {
		return imposta3;
	}

	public void setImposta3(BigDecimal imposta3) {
		this.imposta3 = imposta3;
	}

	public BigDecimal getTotaleFattura() {
		return totaleFattura;
	}

	public void setTotaleFattura(BigDecimal totaleFattura) {
		this.totaleFattura = totaleFattura;
	}

}
