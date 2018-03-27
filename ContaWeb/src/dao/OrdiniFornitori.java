package dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;

import vo.DettaglioOrdine;
import vo.DettaglioOrdineFornitore;
import vo.Fornitore;
import vo.OrdineFornitore;
import vo.RichiestaOrdine;

public class OrdiniFornitori extends DataAccessObject {
	
	public void setDefaultCriteria() {
		getQueryByCriteria().addOrderByDescending("id");
	}
	
	public OrdiniFornitori() {
		this.elementClass = OrdineFornitore.class;
	}
	
	public void store(OrdineFornitore ordineFornitore) throws DataAccessException {
		//GET MAX PROGRESSIVE NUMBER
		PersistenceBroker broker = null;
		try {
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.beginTransaction();
		if (ordineFornitore.getId() == null) {
			Calendar cal = Calendar.getInstance();
			ordineFornitore.setDataCreazione(new java.sql.Date(new Date().getTime()));
			ordineFornitore.setAnnoContabile(cal.get(Calendar.YEAR));
			if (ordineFornitore.getSpedito() == null)
				ordineFornitore.setSpedito(false);
			getCriteria().addEqualTo("annoContabile", ordineFornitore.getAnnoContabile());
			ReportQueryByCriteria q = QueryFactory.newReportQuery(OrdineFornitore.class, getCriteria());
			q.setAttributes(new String[] { "max(numeroProgressivo)"});
			Iterator iter = broker.getReportQueryIteratorByQuery(q);
			Object[] obj = (Object[]) iter.next();
			int n = 0;
			if (obj[0] != null)
				n = (Integer)obj[0];
			ordineFornitore.setNumeroProgressivo(++n);
		}
		broker.store(ordineFornitore);
		
		//aggiorno le qta da ordinare nei dettagli degli ordini
		if (ordineFornitore.getDettagliOrdineFornitore() != null) {
			Iterator itr = ordineFornitore.getDettagliOrdineFornitore().iterator();
			while (itr.hasNext()) {
				DettaglioOrdineFornitore det = (DettaglioOrdineFornitore) itr.next();
				if (det.getRichiesteOrdini() != null) {
					Iterator itr2 = det.getRichiesteOrdini().iterator();
					while (itr2.hasNext()) {
						RichiestaOrdine ro = (RichiestaOrdine) itr2.next();
						DettaglioOrdine det1 = new DettaglioOrdine();
						det1.setId(ro.getIdDettaglioOrdine());
						Query query = new QueryByCriteria(det1);
						det1 = (DettaglioOrdine) broker.getObjectByQuery(query);
						
						if (ro.getQta().intValue() > det1.getPezziDaOrdinare().intValue())
							det1.setPezziDaOrdinare(0);
						else
							det1.setPezziDaOrdinare(det1.getPezziDaOrdinare() - ro.getQta());

						broker.store(det1);
					}
				}
			}
		}
		
		broker.commitTransaction();
		} catch (Exception e) {
			broker.abortTransaction();
			throw new DataAccessException(e.getMessage());
		} finally {
			if (broker != null)
				broker.close();
		}
		
	}
	
	public void delete(OrdineFornitore ordineFornitore) throws DataAccessException {
		ordineFornitore = (OrdineFornitore) findWithAllReferences(ordineFornitore);
		if (ordineFornitore.getSpedito())
			throw new DataAccessException("Non � possibile cancellare ordini giˆ spediti");
		
		PersistenceBroker broker = null;
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			broker.beginTransaction();

			//aggiorno le qta da ordinare nei dettagli degli ordini
			Iterator itr = ordineFornitore.getDettagliOrdineFornitore().iterator();
			while (itr.hasNext()) {
				DettaglioOrdineFornitore det = (DettaglioOrdineFornitore) itr.next();
				if (det.getRichiesteOrdini() != null) {
					Iterator itr2 = det.getRichiesteOrdini().iterator();
					while (itr2.hasNext()) {
						RichiestaOrdine ro = (RichiestaOrdine) itr2.next();
						broker.retrieveReference(ro, "dettaglioOrdine");
						DettaglioOrdine det1 = ro.getDettaglioOrdine();
						det1.setPezziDaOrdinare(det1.getPezziDaOrdinare() + ro.getQta());
						broker.store(det1);
					}
				}
			}
		
			broker.delete(ordineFornitore);
			broker.commitTransaction();
		} catch (Exception e) {
			broker.abortTransaction();
			throw new DataAccessException(e.getMessage());
		} finally {
			if (broker != null)
				broker.close();
		}
	}

	public Vector getDettagliFromOrdiniClienti(OrdineFornitore ordineFornitore,
			Date dataDal, Date dataAl) throws DataAccessException {
		Vector<DettaglioOrdineFornitore> vector = new Vector<DettaglioOrdineFornitore>();
		
		if (dataDal == null || dataAl == null || ordineFornitore == null || ordineFornitore.getIdFornitore() == null)
			return vector;

		Criteria criteria = new Criteria();
		criteria.addEqualTo("articolo.idFornitore", ordineFornitore.getIdFornitore());
		criteria.addGreaterOrEqualThan("ordine.dataCreazione", dataDal);
		criteria.addLessOrEqualThan("ordine.dataCreazione", dataAl);
		criteria.addGreaterThan("pezziDaOrdinare", 0);
		
		PersistenceBroker broker = null;
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			QueryByCriteria qc = QueryFactory.newQuery(DettaglioOrdine.class, criteria);
			Iterator itr = broker.getIteratorByQuery(qc);
			HashMap<Integer, Collection<DettaglioOrdine>> map = new HashMap<Integer, Collection<DettaglioOrdine>>();
			Articoli articoli = new Articoli();
			while (itr.hasNext()) {
				DettaglioOrdine det = (DettaglioOrdine) itr.next();
				Integer idArticolo = det.getIdArticolo();
				if (map.containsKey(idArticolo)) {
					map.get(idArticolo).add(det);
				} else {
					ArrayList<DettaglioOrdine> al = new ArrayList<DettaglioOrdine>();
					al.add(det);
					map.put(idArticolo, al);
				}
				
			}
			
			Iterator itr2 = map.keySet().iterator();
			while (itr2.hasNext()) {
				DettaglioOrdineFornitore dof = new DettaglioOrdineFornitore();
				dof.setOrdineFornitore(ordineFornitore);
				dof.setIdOrdineFornitore(ordineFornitore.getId());
				dof.setRichiesteOrdini(new ArrayList());

				Integer idArticolo = (Integer) itr2.next();
				Collection col = map.get(idArticolo);
				Iterator itr3 = col.iterator();
				Integer sum = 0;
				while (itr3.hasNext()) {
					DettaglioOrdine det = (DettaglioOrdine) itr3.next();
					sum += det.getPezziDaOrdinare();
					RichiestaOrdine ro = new RichiestaOrdine();
					ro.setDettaglioOrdine(det);
					ro.setIdDettaglioOrdine(det.getId());
					ro.setDettaglioOrdineFornitore(dof);
					ro.setIdDettaglioOrdineFornitore(dof.getId());
					ro.setQta(det.getPezziDaOrdinare());
					dof.getRichiesteOrdini().add(ro);
				}
				
				dof.setIdArticolo(idArticolo);
				dof.setArticolo(articoli.find(idArticolo));
				dof.setPezziOrdinati(sum);
				vector.add(dof);
			}
			
			
			/*ReportQueryByCriteria q = QueryFactory.newReportQuery(DettaglioOrdine.class, criteria);
			q.addGroupBy("idArticolo");
			q.setAttributes(new String[] { "idArticolo","sum(pezziDaEvadere)"});
			Iterator itr = broker.getReportQueryIteratorByQuery(q);
			Articoli articoli = new Articoli();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				DettaglioOrdineFornitore dof = new DettaglioOrdineFornitore();
				dof.setOrdineFornitore(ordineFornitore);
				dof.setIdOrdineFornitore(ordineFornitore.getId());
				dof.setIdArticolo((Integer)obj[0]);
				dof.setArticolo(articoli.find((Integer)obj[0]));
				dof.setPezziOrdinati(((BigDecimal)obj[1]).intValue());
				vector.add(dof);
			}*/
		} finally {
			if (broker != null)
				broker.close();
		}
		return vector;
	}
	
	public Vector getDettagliFromOrdiniClienti(Date dataDal, Date dataAl) throws DataAccessException {
		Vector<DettaglioOrdineFornitore> vector = new Vector<DettaglioOrdineFornitore>();
		
		if (dataDal == null || dataAl == null)
			return vector;
		
		Criteria criteria = new Criteria();
		criteria.addGreaterOrEqualThan("ordine.dataCreazione", dataDal);
		criteria.addLessOrEqualThan("ordine.dataCreazione", dataAl);
		criteria.addGreaterThan("pezziDaOrdinare", 0);
		
		PersistenceBroker broker = null;
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			QueryByCriteria qc = QueryFactory.newQuery(DettaglioOrdine.class, criteria);
			Iterator itr = broker.getIteratorByQuery(qc);
			HashMap<Integer, Collection<DettaglioOrdine>> map = new HashMap<Integer, Collection<DettaglioOrdine>>();
			
			Articoli articoli = new Articoli();
			while (itr.hasNext()) {
				DettaglioOrdine det = (DettaglioOrdine) itr.next();
				Integer idArticolo = det.getIdArticolo();
				if (map.containsKey(idArticolo)) {
					map.get(idArticolo).add(det);
				} else {
					ArrayList<DettaglioOrdine> al = new ArrayList<DettaglioOrdine>();
					al.add(det);
					map.put(idArticolo, al);
				}
				
			}
			
			Iterator itr2 = map.keySet().iterator();
			while (itr2.hasNext()) {
				OrdineFornitore of = new OrdineFornitore();
				DettaglioOrdineFornitore dof = new DettaglioOrdineFornitore();
				dof.setOrdineFornitore(of);
				dof.setIdOrdineFornitore(of.getId());
				dof.setRichiesteOrdini(new ArrayList());

				Integer idArticolo = (Integer) itr2.next();
				Collection col = map.get(idArticolo);
				Iterator itr3 = col.iterator();
				Integer sum = 0;
				while (itr3.hasNext()) {
					DettaglioOrdine det = (DettaglioOrdine) itr3.next();
					sum += det.getPezziDaOrdinare();
					RichiestaOrdine ro = new RichiestaOrdine();
					ro.setDettaglioOrdine(det);
					ro.setIdDettaglioOrdine(det.getId());
					ro.setDettaglioOrdineFornitore(dof);
					ro.setIdDettaglioOrdineFornitore(dof.getId());
					ro.setQta(det.getPezziDaOrdinare());
					dof.getRichiesteOrdini().add(ro);
				}
				
				dof.setIdArticolo(idArticolo);
				dof.setArticolo(articoli.find(idArticolo));
				of.setFornitore(dof.getArticolo().getFornitore());
				of.setIdFornitore(dof.getArticolo().getIdFornitore());
				dof.setPezziOrdinati(sum);
				vector.add(dof);
			}
			
			
		} finally {
			if (broker != null)
				broker.close();
		}
		return vector;
	}

	public void setSpedito(OrdineFornitore ordineFornitore) throws DataAccessException {
		ordineFornitore.setSpedito(true);
		store(ordineFornitore);
	}
}
