package dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import vo.DDT;
import vo.Fattura;
import vo.VOElement;

public class RiepilogoDDTs extends VOElement
{

	private static final long serialVersionUID = 2767346264394280662L;
	private Date dataDal = null;
	private Date dataAl = null;
	private BigDecimal totale = null;
	private BigDecimal totaleAcconto = null;
	
	private List<DDT> listaDDTs = null;
	private String tipo = null;
	
	public Date getDataAl() {
		return dataAl;
	}

	public void setDataAl(Date dataAl) {
		this.dataAl = dataAl;
	}

	public Date getDataDal() {
		return dataDal;
	}

	public void setDataDal(Date dataDal) {
		this.dataDal = dataDal;
	}
	
	public void setListaDDTs(List<DDT> fatture)
	{
		this.listaDDTs = fatture;
	}
	
	public List<DDT> getListaDDTs() {
		return listaDDTs;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getTotale() {
		return totale;
	}

	public void setTotale(BigDecimal totale) {	
		this.totale = totale;
	}
	
	public BigDecimal getTotaleAcconto() {
		return totaleAcconto;
	}

	public void setTotaleAcconto(BigDecimal totaleAcconto) {	
		this.totaleAcconto = totaleAcconto;
	}
	
}
