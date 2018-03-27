package dao;

import java.text.DateFormat;
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

import vo.BollaAcquisto;
import vo.DettaglioBollaAcquisto;

public class BolleAcquisto extends DataAccessObject {
		
	public BolleAcquisto() {
		this.elementClass = BollaAcquisto.class;
	}
	
	protected void setDefaultCriteria() {
		getQueryByCriteria().addOrderByDescending("data");
	}
	
	protected void setOrderBy(String colonna, String verso) {
	    this.useDefaultCriteria = false;
	    if(verso.equalsIgnoreCase("DESC")){
	        getQueryByCriteria().addOrderByDescending(colonna);
	    }else{
	        getQueryByCriteria().addOrderByAscending(colonna);
	    }
	  }
	
	protected void setCriteriaUsingFilterKey() {
		
	    Criteria criteria = getCriteria();
		
		//FILTRO PER DATA
		try {
			DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
			Date data = format.parse(filterKey);
			criteria.addEqualTo("data", new java.sql.Date(data.getTime()));
			return;
		} catch(Exception e) {
		}

		//NON E' UNA DATA CERCO NEGLI ALTRI CAMPI
		if(filterKey != null && filterKey.length() > 0){
		    criteria.addLike("fornitore.descrizione", filterKey + "%");
		    
		    //FILTRO PER NUMERO
	        Criteria criteriaNprog = new Criteria();
	        criteriaNprog.addEqualTo("numeroBolla", filterKey);
	        criteria.addOrCriteria(criteriaNprog);
		}
		/* ordina per la colonna 'data' desc */
		setOrderBy("data", "DESC");
	}
	
	public void delete(BollaAcquisto ba) throws DataAccessException {
		PersistenceBroker broker = null;
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			broker.beginTransaction();
			//CANCELLO LE GIACENZE
			Giacenze giacenze = new Giacenze();
			completeReferences(ba);
			for(int i=0; i<ba.getInventario().size(); ++i) {
				DettaglioBollaAcquisto dt = (DettaglioBollaAcquisto) ba.getInventario().get(i);
				giacenze.delete(dt, broker, true);
			}
			broker.delete(ba);
			broker.commitTransaction();
		} catch (Exception e) {
			broker.abortTransaction();
			throw new DataAccessException(e.getMessage());
		} finally {
			if (broker != null)
				broker.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void store(BollaAcquisto ba) throws DataAccessException {
		PersistenceBroker broker = null;
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			
			//IMPOSTO LA DATA
			ba.setData(new java.sql.Date(ba.getData().getTime()));
			//IMPOSTO ANNO CONTABILE
			Calendar cal = Calendar.getInstance();
			cal.setTime(ba.getData());
			ba.setAnnoContabile(cal.get(Calendar.YEAR));
			
			//IMPOSTO EVENTUALI DATE ED ALTRI CAMPI SULLE RIGHE DI DETTAGLIO
			Iterator itr = ba.getInventario().iterator();
			while (itr.hasNext()) {
				DettaglioBollaAcquisto dba = (DettaglioBollaAcquisto) itr.next();
				if (dba.getDataScadenza() != null)
					dba.setDataScadenza(new java.sql.Date(dba.getDataScadenza().getTime()));
				dba.setIdBollaAcquisto(ba.getId());
				dba.setBollaAcquisto(ba);
				completeReferences(dba);
			}
			
			//RICAVO LA LISTA DEI DDT GIA' PRESENTI
			Vector righeDaCancellare = new Vector();
			if (ba.getId() != null) { //NON E' INSERIMENTO
				Criteria criteria = new Criteria();
				criteria.addEqualTo("idBollaAcquisto", ba.getId());
				Query query = QueryFactory.newQuery(DettaglioBollaAcquisto.class, criteria); 
				Collection righe = broker.getCollectionByQuery(query);
				Iterator itrR = righe.iterator();
				while (itrR.hasNext()) {
					DettaglioBollaAcquisto dba = (DettaglioBollaAcquisto) itrR.next();
					boolean trovato = false;
					Iterator itrR2 = ba.getInventario().iterator();
					while (itrR2.hasNext() && !trovato) {
						DettaglioBollaAcquisto dt2 = (DettaglioBollaAcquisto) itrR2.next();
						if (dt2.getId() != null && dt2.getId().intValue() == dba.getId().intValue())
							trovato = true;							
					}
					if (!trovato)
						righeDaCancellare.add(dba);
				}
			}
			
			broker.beginTransaction();
			
			broker.store(ba);
			//CANCELLO LE RIGHE NON PIU' PRESENTI
			Giacenze giacenze = new Giacenze();
			if (righeDaCancellare.size() > 0) {
				itr = righeDaCancellare.iterator();
				while (itr.hasNext()) {
					DettaglioBollaAcquisto dt = (DettaglioBollaAcquisto) itr.next();
					delete(dt);
					giacenze.delete(dt,broker,false);
				}
			}
			
			//AGGIORNO LE GIACENZE
			itr = ba.getInventario().iterator();
			while (itr.hasNext()) {
				DettaglioBollaAcquisto dt = (DettaglioBollaAcquisto) itr.next();
				giacenze.store(dt,broker);
			}
			
			broker.commitTransaction();
		} catch (Exception e) {
			broker.abortTransaction();
			e.printStackTrace();
			throw new DataAccessException(e.getMessage());
		} finally {
			if (broker != null)
				broker.close();
		}
	}

	public Collection ricercaLotto(String lotto) throws DataAccessException {
		PersistenceBroker broker = null;
		Collection results = null;
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			Criteria criteria = new Criteria();
			criteria.addEqualTo("inventario.lotto", lotto);
			QueryByCriteria query = QueryFactory.newQuery(BollaAcquisto.class, criteria);
			query.addGroupBy("numeroBolla");
			query.addGroupBy("data");
			query.addGroupBy("fornitore.descrizione");
			query.addOrderByDescending("data");
			results = broker.getCollectionByQuery(query);
			if (results != null) {
				Iterator itr = results.iterator();
				while (itr.hasNext()) {
					BollaAcquisto dt = (BollaAcquisto)itr.next();
					completeReferences(dt);
				}
			}
		} finally {
			if (broker != null)
				broker.close();
		}
		
		return results;
	}
}
