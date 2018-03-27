package dao;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;

import vo.Autista;
import vo.Cliente;
import vo.Telefonata;
import vo.VOElement;

public class Telefonate extends DataAccessObject {
	
	public void store(Telefonata value) throws DataAccessException {
		//Check if it exists
		/*Telefonata tel = new Telefonata();
		tel.setIdCliente(value.getIdCliente());
		tel.setIdPuntoConsegna(value.getIdPuntoConsegna());
		tel.setGiorno(value.getGiorno());
		tel = (Telefonata) find(tel);
		value.setId(tel.getId());*/
		
		if(value.getEseguita() == null)
			value.setEseguita(false);
		super.store(value);
	}
	
	public Telefonate() {
		this.elementClass = Telefonata.class;
	}
	
	protected void setDefaultCriteria() {
		
		getQueryByCriteria().addOrderByAscending("orario");		
	}


	public Collection getListaDaChiamare(Date date) throws DataAccessException {
		Calendar cal = Calendar.getInstance(Locale.ITALY);
		cal.setTime(date);
		Integer today = cal.get(Calendar.DAY_OF_WEEK) - 2;
		
		getCriteria().addEqualTo("giorno", today);
		List<Telefonata> lst = (List<Telefonata>)getElements();
		
		Comparator<Telefonata> comparator = new Comparator<Telefonata>() {
		    public int compare(Telefonata c1, Telefonata c2) {
		        String autista1 = "";
		        String autista2 = "";
		        if(c1.getAutista() != null)
		        	autista1 = c1.getAutista().getNome();
		        if(c2.getAutista() != null)
		        	autista2 = c2.getAutista().getNome();
		        
		        int retValue = autista2.compareTo(autista1);
		        if(retValue == 0) return c1.getOrario() - c2.getOrario();
		        
		        return retValue;
		    }
		};
		
		Collections.sort(lst, comparator);
		
		return lst;
	}
	
	public Collection getListaDaChiamare(Date date, Integer idCliente) throws DataAccessException {
		Calendar cal = Calendar.getInstance(Locale.ITALY);
		cal.setTime(date);
		Integer today = cal.get(Calendar.DAY_OF_WEEK) - 2;
		
		getCriteria().addEqualTo("giorno", today);
		criteria.addEqualTo("cliente.id", idCliente);
		List<Telefonata> lst = (List<Telefonata>)getElements();
		
		Comparator<Telefonata> comparator = new Comparator<Telefonata>() {
		    public int compare(Telefonata c1, Telefonata c2) {
		        String autista1 = "";
		        String autista2 = "";
		        if(c1.getAutista() != null)
		        	autista1 = c1.getAutista().getNome();
		        if(c2.getAutista() != null)
		        	autista2 = c2.getAutista().getNome();
		        
		        int retValue = autista2.compareTo(autista1);
		        if(retValue == 0) return c1.getOrario() - c2.getOrario();
		        
		        return retValue;
		    }
		};
		
		Collections.sort(lst, comparator);
		
		return lst;
	}
	
	public Collection UndoTelefonate(Date date) throws DataAccessException {
		PersistenceBroker broker = null;
		Collection results = null;
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			
			Calendar cal = Calendar.getInstance(Locale.ITALY);
			cal.setTime(date);
			Integer today = cal.get(Calendar.DAY_OF_WEEK) - 2;
			Criteria criteria = new Criteria();
			criteria.addNotEqualTo("giorno", today);
			/* *** 
				criteria.addEqualTo("eseguita", true);
		    *** */
			QueryByCriteria qry = QueryFactory.newQuery(elementClass, criteria);
			
			results = broker.getCollectionByQuery(qry);
			broker.beginTransaction();
			for(Object el : results)
			{
				Telefonata tel = (Telefonata)el;
				tel.setEseguita(false);
				broker.store(tel);				
			}
			broker.commitTransaction();
			broker.close();
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage());
		} finally {
			if (broker != null)
				broker.close();
		}

		return results;
	}

	public Collection<Telefonata> getListaTelefonate(Integer idCliente, Integer idPuntoConsegna) throws DataAccessException {
		getCriteria().addEqualTo("idCliente", idCliente);
		if (idPuntoConsegna != null)
			getCriteria().addEqualTo("idPuntoConsegna", idPuntoConsegna);
		return getElements();
	}
}
