package vo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Vector;

public class NotaAccredito
  extends VOElement
{
  private static final long serialVersionUID = 3056413010720785672L;
  private Cliente cliente;
  private PuntoConsegna puntoConsegna;
  private Integer idCliente;
  private Integer idPuntoConsegna;
  private Integer numeroProgressivo;
  private String numeroProgressivo2;
  private Integer annoContabile;
  private Date data;
  private Vector dettagliNotaAccredito;
  private Integer colli;
  private String trasporto;
  private String aspettoEsteriore;
  private Date dataTrasporto;
  private Date oraTrasporto;
  private String causale;
  private Boolean pagato = null;
  private BigDecimal acconto;
  private BigDecimal totaleImponibile;
  private BigDecimal totaleImposta;
  private Integer idFattura = null;
  private Fattura fattura = null;
  private BigDecimal[][] imponibili = null;
  
  public Fattura getFattura()
  {
    return this.fattura;
  }
  
  public void setFattura(Fattura fattura)
  {
    this.fattura = fattura;
  }
  
  public Integer getIdFattura()
  {
    return this.idFattura;
  }
  
  public void setIdFattura(Integer idFattura)
  {
    this.idFattura = idFattura;
  }
  
  public Boolean getFatturato()
  {
    if (this.idFattura != null) {
      return Boolean.valueOf(true);
    }
    return Boolean.valueOf(false);
  }
  
  public String getAspettoEsteriore()
  {
    return this.aspettoEsteriore;
  }
  
  public void setAspettoEsteriore(String aspettoEsteriore)
  {
    this.aspettoEsteriore = aspettoEsteriore;
  }
  
  public String getCausale()
  {
    return this.causale;
  }
  
  public void setCausale(String causale)
  {
    this.causale = causale;
  }
  
  public Cliente getCliente()
  {
    return this.cliente;
  }
  
  public void setCliente(Cliente cliente)
  {
    this.cliente = cliente;
  }
  
  public Integer getColli()
  {
    return this.colli;
  }
  
  public void setColli(Integer colli)
  {
    this.colli = colli;
  }
  
  public Date getData()
  {
    return this.data;
  }
  
  public void setData(Date data)
  {
    this.data = data;
  }
  
  public Date getDataTrasporto()
  {
    return this.dataTrasporto;
  }
  
  public void setDataTrasporto(Date dataTrasporto)
  {
    this.dataTrasporto = dataTrasporto;
  }
  
  public Vector getDettagliAccredito()
  {
    return this.dettagliNotaAccredito;
  }
  
  public void setDettagliAccredito(Vector dettagliNotaAccredito)
  {
    this.dettagliNotaAccredito = dettagliNotaAccredito;
  }
  
  public Integer getNumeroProgressivo()
  {
    return this.numeroProgressivo;
  }
  
  public void setNumeroProgressivo(Integer numeroProgressivo)
  {
    this.numeroProgressivo = numeroProgressivo;
  }
  
  public Date getOraTrasporto()
  {
    return this.oraTrasporto;
  }
  
  public void setOraTrasporto(Date oraTrasporto)
  {
    this.oraTrasporto = oraTrasporto;
  }
  
  public PuntoConsegna getPuntoConsegna()
  {
    return this.puntoConsegna;
  }
  
  public void setPuntoConsegna(PuntoConsegna puntoConsegna)
  {
    this.puntoConsegna = puntoConsegna;
  }
  
  public String getTrasporto()
  {
    return this.trasporto;
  }
  
  public BigDecimal getTotale()
  {
    return calcolaTotale();
  }
  
  public void setTrasporto(String trasporto)
  {
    this.trasporto = trasporto;
  }
  
  public BigDecimal calcolaTotale()
  {
    return calcolaTotale(this.dettagliNotaAccredito);
  }
  
  public  BigDecimal calcolaTotale(Vector articoli)
  {
    BigDecimal totale = new BigDecimal(0);
    this.totaleImponibile = new BigDecimal(0);
    this.totaleImposta = new BigDecimal(0);
    if (articoli != null)
    {
      ListIterator itr = articoli.listIterator();
      
      this.imponibili = new BigDecimal[100][2];
      while (itr.hasNext())
      {
        DettaglioNotaAccredito dettaglio = (DettaglioNotaAccredito)itr.next();
        BigDecimal imponibile = dettaglio.calcolaImponibile();
        if(imponibile == null) continue;
        int iva = 0;
        if(dettaglio.getIva() != null)
        	iva = dettaglio.getIva().intValue();
        
        if (imponibili[iva][0] == null) {
          imponibili[iva][0] = imponibile;
        } else {
          imponibili[iva][0] = imponibili[iva][0].add(imponibile);
        }
      }
      for (int i = 0; i < 100; i++) {
        if (imponibili[i] != null)
        {
          BigDecimal imp = imponibili[i][0];
          if(imp == null) continue;
          BigDecimal totaleIva = imp.multiply(new BigDecimal(i).divide(new BigDecimal(100)).setScale(2, 4)).setScale(2, 0);
          totale = totale.add(imp.add(totaleIva));
          this.imponibili[i][1] = totaleIva;
        
          this.totaleImponibile = this.totaleImponibile.add(imp);
          this.totaleImposta = this.totaleImposta.add(totaleIva);
        }
      }
    }
    return totale;
  }
  
  public Integer getIdCliente()
  {
    return this.idCliente;
  }
  
  public void setIdCliente(Integer idCliente)
  {
    this.idCliente = idCliente;
  }
  
  public Integer getIdPuntoConsegna()
  {
    return this.idPuntoConsegna;
  }
  
  public void setIdPuntoConsegna(Integer idPuntoConsegna)
  {
    this.idPuntoConsegna = idPuntoConsegna;
  }
  
  public String getDescrizioneBreveDDT()
  {
    SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
    return "DDT n. " + this.numeroProgressivo + " del " + date.format(this.data);
  }
  
  public Integer getStato()
  {
    if ((getPagato() != null) && (getPagato().booleanValue())) {
      return Integer.valueOf(0);
    } else if(getAcconto() != null 
    		&& getAcconto().compareTo(new BigDecimal(0)) > 0) return Integer.valueOf(5);
    
    return Integer.valueOf(1);
  }
  
  public Integer getAnnoContabile()
  {
    return this.annoContabile;
  }
  
  public void setAnnoContabile(Integer annoContabile)
  {
    this.annoContabile = annoContabile;
  }
  
  public String getNumeroProgressivo2()
  {
    return this.numeroProgressivo2;
  }
  
  public void setNumeroProgressivo2(String numeroProgressivo2)
  {
    this.numeroProgressivo2 = numeroProgressivo2;
  }
  
  public String getNumeroProgressivoCompleto()
  {
    if (!this.numeroProgressivo2.equalsIgnoreCase("")) {
      return this.numeroProgressivo.toString() + "/" + this.numeroProgressivo2;
    }
    return this.numeroProgressivo.toString();
  }
  
  public void setPagato(Boolean pagato)
  {
    this.pagato = pagato;
  }
  
  public Boolean getPagato()
  {
    return this.pagato;
  }
  
  public void setAcconto(BigDecimal acconto)
  {
    this.acconto = acconto;
  }
  
  public BigDecimal getAcconto()
  {
    return this.acconto;
  }


public BigDecimal getTotaleImponibile() {
	return totaleImponibile;
}

public void setTotaleImponibile(BigDecimal totaleImponibile) {
	this.totaleImponibile = totaleImponibile;
}

public BigDecimal getTotaleImposta() {
	return totaleImposta;
}

public void setTotaleImposta(BigDecimal totaleImposta) {
	this.totaleImposta = totaleImposta;
}

public BigDecimal[][] getImponibili() {
	return imponibili;
}

public void setImponibili(BigDecimal[][] imponibili) {
	this.imponibili = imponibili;
}
}
