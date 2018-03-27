package vo;

import java.math.BigDecimal;
import java.util.Date;

public class PagamentoEseguito
  extends VOElement
{
  private static final long serialVersionUID = 4932182164185788296L;
  private Date data = null;
  private String descrizione;
  private String notes;
  private BigDecimal importo;
  private Integer idPagamento;
  private Pagamento pagamento;
  private Integer idCliente;
  private Cliente cliente;
  private Integer idFattura;
  private Fattura fattura;
  private Integer idDDT;
  private DDT ddt;
  
  public DDT getDdt()
  {
    return this.ddt;
  }
  
  public void setDdt(DDT ddt)
  {
    this.ddt = ddt;
  }
  
  public Date getData()
  {
    return this.data;
  }
    
  public void setData(Date data)
  {
    this.data = data;
  }
  
  public String getDescrizione()
  {
    return this.descrizione;
  }
  
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  
  public BigDecimal getImporto()
  {
    return this.importo;
  }
  
  public void setImporto(BigDecimal importo)
  {
    this.importo = importo;
  }
  
  public Integer getIdPagamento()
  {
    return this.idPagamento;
  }
  
  public void setIdPagamento(Integer idpagamento)
  {
    this.idPagamento = idpagamento;
  }
  
  public void setIdCliente(Integer idCliente)
  {
    this.idCliente = idCliente;
  }
  
  public Integer getIdCliente()
  {
    return this.idCliente;
  }
  
  public void setPagamento(Pagamento pagamento)
  {
    this.pagamento = pagamento;
  }
  
  public Pagamento getPagamento()
  {
    return this.pagamento;
  }
  
  public void setCliente(Cliente cliente)
  {
    this.cliente = cliente;
  }
  
  public Cliente getCliente()
  {
    return this.cliente;
  }
  
  public void setIdFattura(Integer idFattura)
  {
    this.idFattura = idFattura;
  }
  
  public Integer getIdFattura()
  {
    return this.idFattura;
  }
  
  public void setFattura(Fattura fattura)
  {
    this.fattura = fattura;
  }
  
  public Fattura getFattura()
  {
    return this.fattura;
  }
  
  public void setIdDDT(Integer idDDT)
  {
    this.idDDT = idDDT;
  }
  
  public Integer getIdDDT()
  {
    return this.idDDT;
  }

  public String getNotes() 
  {
	return notes;
  }

  public void setNotes(String notes) 
  {
	this.notes = notes;
  }
}
