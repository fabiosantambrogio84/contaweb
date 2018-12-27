package dao;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;

import stampemgr.StampeMgr;
import vo.Articolo;
import vo.DettaglioDDT;
import vo.DettaglioNotaAccredito;
import vo.NotaAccredito;
import vo.PuntoConsegna;
import vo.VOElement;

public class NoteAccredito
  extends DataAccessObject
{
  public NoteAccredito()
  {
    this.elementClass = NotaAccredito.class;
  }
  
  protected Date filterDataDa = null;  
  protected Date filterDataA = null;
  protected String filterStato = null;
  protected String filterPagamento = null;
  protected String filterArticolo = null;
  
  
  public String getFilterPagamento() {
	return filterPagamento;
	}
	
	public void setFilterPagamento(String filterPagamento) {
		this.filterPagamento = filterPagamento;
	}
	
	public String getFilterArticolo() {
		return filterArticolo;
	}
	
	public void setFilterArticolo(String filterArticolo) {
		this.filterArticolo = filterArticolo;
	}
	
	public Date getFilterDataDa() {
		return filterDataDa;
	}

	public void setFilterDataDa(Date filterDataDa) {
		this.filterDataDa = filterDataDa;
	}
	
	public Date getFilterDataA() {
		return filterDataA;
	}
	
	public void setFilterDataA(Date filterDataA) {
		this.filterDataA = filterDataA;
	}
	
	public String getFilterStato() {
		return filterStato;
	}
	
	public void setFilterStato(String filterStato) {
		this.filterStato = filterStato;
	}


  
  public void setOrderByNprog(int order)
  {
    getQueryByCriteria().addOrderByDescending("annoContabile");
    if (order == ORDER_DESC) {
      getQueryByCriteria().addOrderByDescending("numeroProgressivo");
    } else {
      getQueryByCriteria().addOrderByAscending("numeroProgressivo");
    }
  }
  
  public void setOrderByCliente() {
      this.useDefaultCriteria = false;
      getQueryByCriteria().addOrderByAscending("cliente.rs");
  }
  
  protected void setDefaultCriteria()
  {
    setOrderByNprog(ORDER_DESC);
  }
  
  public NotaAccredito completeReferences(NotaAccredito ddt)
    throws DataAccessException
  {
	  NotaAccredito ddt2 = (NotaAccredito)super.completeReferences(ddt);
    
    Iterator itr = ddt2.getDettagliAccredito().iterator();
    while (itr.hasNext())
    {
      DettaglioNotaAccredito dt = (DettaglioNotaAccredito)itr.next();
      super.completeReferences(dt);
    }
    return ddt2;
  }
  
  public Collection<?> getNoteAccredito(java.util.Date dataInizio, java.util.Date dataFine) throws DataAccessException {
      getCriteria().addBetween("data", dataInizio, dataFine);
      return getElements();
  }
  
  public Collection getElements()
	    throws DataAccessException
  {
	setCriteriaUsingFilterKey();
    Collection elements = super.getElements();
    return elements;
  }
  
  protected void setCriteriaUsingFilterKey()
  {
	
	  Criteria criteria = getCriteria();
	    
	    if(this.filterKey != null && this.filterKey.length() > 0)
	    {
	    
		    criteria.addLike("cliente.rs", this.filterKey + "%");
		    
		    Criteria criteriaAgente = new Criteria();
	        criteriaAgente.addLike("cliente.agente.cognome",  this.filterKey + "%");
	        criteria.addOrCriteria(criteriaAgente);
	        
	        
		    try
		    {
		        NumberFormat nf = NumberFormat.getNumberInstance();
		        Number number = nf.parse(this.filterKey);
		        
		        Criteria criteriaNprog = new Criteria();
		        criteriaNprog.addEqualTo("numeroProgressivo", number);
		        criteria.addOrCriteria(criteriaNprog);		       
		        
		    }
		    catch (Exception localException1)
		    {
		    	
		    }
	    }
	    
	    DateFormat DF = new SimpleDateFormat("yyyy-M-dd", Locale.ITALIAN);
	    DF.setLenient(false);
	    
	    if(this.filterDataDa != null)
	    	criteria.addGreaterOrEqualThan("data", this.filterDataDa);
		
	    if(this.filterDataA != null)
	    	criteria.addLessOrEqualThan("data", this.filterDataA);
		
	    if(this.filterPagamento != null && this.filterPagamento.length() > 0)
	    {
	    	criteria.addLike("cliente.pagamento.descrizione", "%" + this.filterPagamento + "%");
	    }
	    
	    if(this.filterArticolo != null  && this.filterArticolo.length() > 0)
	    {
	    	Criteria subCriteria = new Criteria();
	    	subCriteria.addEqualToField("idDDT", Criteria.PARENT_QUERY_PREFIX + "id");
	    	subCriteria.addLike("descrizioneArticolo", "%" + this.filterArticolo + "%");
	    	
	    	criteria.addExists(new QueryByCriteria(DettaglioNotaAccredito.class, subCriteria));
	    }
	    
	    if(this.filterStato != null && this.filterStato.length() > 0)
	    {
	    	if(filterStato.equals("01")) criteria.addEqualTo("pagato", true);
	    	else if(filterStato.equals("02")) criteria.addEqualTo("pagato", false);
	    }
  }
  
  public String printRiepilogo() throws Exception { //STAMPA LISTA DOCUMENTI
	  	
	  	BigDecimal totale = new BigDecimal(0);
	  	
	  	Collection colFatture = getElements();
		Iterator itr = colFatture.iterator();
		ArrayList listaFatture = new ArrayList();
		
		while (itr.hasNext()) {
			NotaAccredito fattura = (NotaAccredito) itr.next();
			listaFatture.add(fattura);
			totale = totale.add(fattura.getTotale());
		}
	  	
		DateFormat DF = new SimpleDateFormat("yyyy-M-dd", Locale.ITALIAN);
	    DF.setLenient(false);
	    
	    Date today = new Date();
	    Calendar c = Calendar.getInstance(); 
	    c.setTime(today); 
	    c.add(Calendar.MONTH, -1);
	    Date start = c.getTime();
	    
	  	RiepilogoDDTs pf = new RiepilogoDDTs();
	  	if(this.filterDataDa != null) pf.setDataDal(getFilterDataDa());
	  	else pf.setDataDal(start);
	  	if(this.filterDataA != null) pf.setDataAl(getFilterDataA());
	  	else pf.setDataAl(today);
		pf.setListaDDTs(listaFatture);
		pf.setTotale(totale);
		pf.setTipo("F");
		
		pdfFile = StampeMgr.getInstance().richiediPDFDDTList(pf);
		
		return "success";
}
  
  public void delete(NotaAccredito ddt)
    throws DataAccessException
  {
    ddt = (NotaAccredito)findWithAllReferences(ddt);
    if (!ddt.getFatturato().booleanValue())
    {
      PersistenceBroker broker = null;
      try
      {
        broker = PersistenceBrokerFactory.defaultPersistenceBroker();
        broker.beginTransaction();             
        broker.delete(ddt);
        broker.commitTransaction();
      }
      catch (Exception e)
      {
        broker.abortTransaction();
        throw new DataAccessException(e.getMessage());
      }
      finally
      {
        if (broker != null) {
          broker.close();
        }
      }
    }
    else
    {
      throw new DataAccessException("Non è possibile cancellare DDT fatturati");
    }
  }
  
  public String store(VOElement element)
    throws DataAccessException
  {
    PersistenceBroker broker = null;
    String result = ERROR;
    NotaAccredito ddt = (NotaAccredito)element;
    if(ddt.getPagato() == null) ddt.setPagato(false);

    Calendar cal = Calendar.getInstance();
    cal.setTime(ddt.getData());
    Integer annoContabile = Integer.valueOf(cal.get(1));
    if (ddt.getAnnoContabile() == null) {
      ddt.setAnnoContabile(annoContabile);
    }
    try
    {
      if (ddt.getAcconto() == null || ddt.getAcconto().compareTo(new BigDecimal(0)) < 0) {
        ddt.setAcconto(BigDecimal.ZERO);
      }
      ddt.setData(new java.sql.Date(ddt.getData().getTime()));
      
      Iterator itr = ddt.getDettagliAccredito().iterator();
      while (itr.hasNext())
      {
        DettaglioNotaAccredito dt = (DettaglioNotaAccredito)itr.next();
        dt.setTotale(dt.calcolaImponibile());
        dt.setNotaAccredito(ddt);
      }
      Vector righeDaCancellare = new Vector();
      Vector righeAggiornate = new Vector();
      if (ddt.getId() != null)
      {
        broker = PersistenceBrokerFactory.defaultPersistenceBroker();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("idNotaAccredito", ddt.getId());
        Query query = QueryFactory.newQuery(DettaglioNotaAccredito.class, criteria);
        Collection righe = broker.getCollectionByQuery(query);
        Iterator itrR = righe.iterator();
        while (itrR.hasNext())
        {
          DettaglioNotaAccredito dt = (DettaglioNotaAccredito)itrR.next();
          boolean trovato = false;
          Iterator itrR2 = ddt.getDettagliAccredito().iterator();
          while ((itrR2.hasNext()) && (!trovato))
          {
            DettaglioNotaAccredito dt2 = (DettaglioNotaAccredito)itrR2.next();
            if ((dt2.getId() != null) && (dt2.getId().intValue() == dt.getId().intValue())) {
              trovato = true;
            }
          }
          if (!trovato)
          {
            righeDaCancellare.add(dt);
            completeReferences(dt);
          }
          else
          {
            righeAggiornate.add(dt);
          }
        }
        broker.close();
      }
      
      if (righeDaCancellare.size() > 0)
      {
        itr = righeDaCancellare.iterator();
        while (itr.hasNext())
        {
        	DettaglioNotaAccredito dt = (DettaglioNotaAccredito)itr.next();
          delete(dt);          
        }
      }
      
      broker = PersistenceBrokerFactory.defaultPersistenceBroker();
      broker.beginTransaction();
      if (ddt.getNumeroProgressivo() == null)
      {
        Criteria cri = new Criteria();
        cri.addEqualTo("annoContabile", annoContabile);
        cri.addEqualTo("numeroProgressivo2", "");
        ReportQueryByCriteria q = QueryFactory.newReportQuery(NotaAccredito.class, new String[] { "max(numeroProgressivo)" }, cri, true);
        Iterator iter = broker.getReportQueryIteratorByQuery(q);
        Object[] obj = (Object[])iter.next();
        if (obj[0] != null)
        {
          Integer n = (Integer)obj[0];
          ddt.setNumeroProgressivo(n = Integer.valueOf(n.intValue() + 1));
        }
        else
        {
          ddt.setNumeroProgressivo(Integer.valueOf(1));
        }
        ddt.setNumeroProgressivo2("");
      }
      
      Vector<DettaglioNotaAccredito> det = ddt.getDettagliAccredito();
      Iterator itd = ddt.getDettagliAccredito().iterator();
      
      while(itd.hasNext())
      {
    	  DettaglioNotaAccredito dett = (DettaglioNotaAccredito)itd.next();
    	  Articolo art = new Articolo();
    	  art.setId(dett.getIdArticolo());
    	  dett.setArticolo((Articolo)findWithAllReferences(art));
      }
      
      ddt.setDettagliAccredito(det);
      
      if(ddt.getPuntoConsegna()==null)
      {
    	  
    	  PuntoConsegna pc = new PuntoConsegna();
    	  pc.setIdCliente(ddt.getIdCliente());
		  pc = (PuntoConsegna)findWithAllReferences(pc);
		  
		  ddt.setPuntoConsegna(pc);
      }
      
      broker.store(ddt);
                
      
      itr = ddt.getDettagliAccredito().iterator();
      while (itr.hasNext())
      {
        DettaglioNotaAccredito dt = (DettaglioNotaAccredito)itr.next();
        Integer pezzi = dt.getPezzi();
        if (pezzi == null) {
          pezzi = Integer.valueOf(0);
        }
        boolean trovato = false;
        Iterator itr3 = righeAggiornate.iterator();
        while ((itr3.hasNext()) && (!trovato))
        {
          DettaglioNotaAccredito dt2 = (DettaglioNotaAccredito)itr3.next();
          if (dt2.getId().intValue() == dt.getId().intValue())
          {
            trovato = true;
            if (dt2.getPezzi() != null) {
              pezzi = Integer.valueOf(pezzi.intValue() - dt2.getPezzi().intValue());
            }
          }
        }
               
      }
      broker.commitTransaction();
      result = SUCCESS;
    }
    catch (Exception e)
    {
      broker.abortTransaction();
      throw new DataAccessException(e.getMessage());
    }
    finally
    {
      if (broker != null) {
        broker.close();
      }
    }
    return result;
  }
}
