 package vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ListIterator;
import java.util.Vector;

public class BollaAcquisto extends VOElement {

	private static final long serialVersionUID = -9089378451122908253L;

	private Integer idFornitore = null;
	private Fornitore fornitore = null;
	private String numeroBolla = null;
	
	private Integer annoContabile = null;
	private Date data = null;
	private Integer colli = null;
	private Vector inventario = null;
	
	public Vector getInventario() {
		return inventario;
	}
	public void setInventario(Vector inventario) {
		this.inventario = inventario;
	}
	public Integer getAnnoContabile() {
		return annoContabile;
	}
	public void setAnnoContabile(Integer annoContabile) {
		this.annoContabile = annoContabile;
	}
	public Integer getColli() {
		return colli;
	}
	public void setColli(Integer colli) {
		this.colli = colli;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Fornitore getFornitore() {
		return fornitore;
	}
	public void setFornitore(Fornitore fornitore) {
		this.fornitore = fornitore;
	}
	public Integer getIdFornitore() {
		return idFornitore;
	}
	public void setIdFornitore(Integer idFornitore) {
		this.idFornitore = idFornitore;
	}
	public String getNumeroBolla() {
		return numeroBolla;
	}
	public void setNumeroBolla(String numeroBolla) {
		this.numeroBolla = numeroBolla;
	}
	
//	public Integer getNumeroBollaInt() {
//        return numeroBollaInt;
//    }
//	
//    public void setNumeroBollaInt(String numeroBolla) {
//        this.numeroBollaInt = Integer.valueOf(numeroBolla);
//    }
	
	public BigDecimal getTotale()
	{
	    return calcolaTotale(this.inventario);
	}
	
	public static BigDecimal calcolaTotale(Vector articoli)
	{
	    BigDecimal totale = new BigDecimal(0);
	    if (articoli != null)
	    {
	      ListIterator itr = articoli.listIterator();
	      
	      BigDecimal[] imponibili = new BigDecimal[100];
	      while (itr.hasNext())
	      {
	    	  DettaglioBollaAcquisto dba = (DettaglioBollaAcquisto) itr.next();
	    	  BigDecimal imponibile = dba.calcolaImponibile();
		      if (imponibili[dba.getIva().intValue()] == null) {
		    	  imponibili[dba.getIva().intValue()] = imponibile;
		      } else {
		    	  imponibili[dba.getIva().intValue()] = imponibili[dba.getIva().intValue()].add(imponibile);
		      }
	      }
	      
	      for (int i = 0; i < 100; i++) {
	        if (imponibili[i] != null)
	        {
	          BigDecimal imp = imponibili[i];
	          BigDecimal totaleIva = imp.multiply(new BigDecimal(i).divide(new BigDecimal(100)).setScale(2, 4)).setScale(2, 0);
	          totale = totale.add(imp.add(totaleIva));
	        }
	      }
	    }
	    return totale;
	  }
    
}
