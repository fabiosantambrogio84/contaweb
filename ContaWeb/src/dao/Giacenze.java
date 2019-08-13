package dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

import vo.Articolo;
import vo.BollaAcquisto;
import vo.DDT;
import vo.DettaglioBollaAcquisto;
import vo.DettaglioDDT;
import vo.Giacenza;
import vo.MovimentoGiacenza;
import vo.ReportGiacenza;

public class Giacenze extends DataAccessObject {

	public Giacenze() {
		this.elementClass = Giacenza.class;
	}

	public Giacenza find(int idGiacenza) throws DataAccessException {
		Giacenza giacenza = new Giacenza();
		giacenza.setId(idGiacenza);		
		return (Giacenza)find(giacenza);
	}
	
	protected void setCriteriaUsingFilterKey() {
		Criteria criteria = getCriteria();

		if(filterKey != null && filterKey.length() > 0)
		{
			//NON E' UNA DATA CERCO NEGLI ALTRI CAMPI
			criteria.addLike("articolo.descrizione", "%" + filterKey + "%");
			
			//FILTRO PER NUMERO
			Criteria criteriaNprog = new Criteria();
			criteriaNprog.addEqualTo("articolo.codiceArticolo", filterKey);
			criteria.addOrCriteria(criteriaNprog);
		}
	}

	@SuppressWarnings("unchecked")
	public Collection getElements() throws DataAccessException {
		//RICAVO UNA LISTA RAGGRUPPATA PER ARTICOLO
		PersistenceBroker broker = null;
		Collection results = null;
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			//FILTER KEY
			if (filterKey != null && filterKey.length()>0)
				setCriteriaUsingFilterKey();

			ReportQueryByCriteria q = QueryFactory.newReportQuery(Giacenza.class, getCriteria());
			q.setAttributes(new String[] { "sum(qta)", "idArticolo", "articolo.descrizione", "articolo.codiceArticolo", "articolo.attivo", "articolo.fornitore.descrizione" });
			q.addGroupBy("idArticolo");
			q.addGroupBy("articolo.descrizione");
			q.addGroupBy("articolo.codiceArticolo");
			q.addGroupBy("articolo.attivo");
			q.addGroupBy("articolo.fornitore.descrizione");
			
			//FILTRO LE GIACENZE NULLE
			Criteria criteria = new Criteria();
			criteria.addNotEqualTo("sum(qta)", new BigDecimal(0));
			q.setHavingCriteria(criteria);

			//CASO PAGINANTE
			if ((startAtIndex != -1) && (stopAtIndex != -1)) {
				q.setStartAtIndex(startAtIndex);
				q.setEndAtIndex(stopAtIndex - 1);
			}
			
			q.addOrderByAscending("articolo.descrizione");
			
			Iterator iter = broker.getReportQueryIteratorByQuery(q);
			results = new ArrayList();
			while (iter.hasNext()) {
				Object[] obj = (Object[]) iter.next();
				ReportGiacenza reportGiacenza = new ReportGiacenza();
				reportGiacenza.setQta((BigDecimal) obj[0]);
				reportGiacenza.setId((Integer) obj[1]);
				reportGiacenza.setDescrizione((String) obj[2]);
				reportGiacenza.setCodiceArticolo((String)obj[3]);
				reportGiacenza.setAttivo((Boolean)obj[4]);
				reportGiacenza.setDescrizioneFornitore((String)obj[5]);
				results.add(reportGiacenza);
			}
			
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage());
		} finally {
			if (broker != null)
				broker.close();
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	private void aggiungiMovimento(Giacenza giacenza, PersistenceBroker broker) {
		//AGGIUNGO UN MOVIMENTO
		MovimentoGiacenza mov = new MovimentoGiacenza();
		mov.setData(new java.sql.Date(new Date().getTime()));
		if (giacenza.getMovimenti().size() == 0) {
			mov.setQta(giacenza.getQta());
			mov.setTipoMovim(MovimentoGiacenza.INSERIMENTO);
		} else {
			Vector movimenti = giacenza.getMovimenti();
			MovimentoGiacenza mov2 = (MovimentoGiacenza) movimenti.get(movimenti.size() - 1);
			if (mov2.getQtaDopoMovim().doubleValue() > giacenza.getQta().doubleValue()) {
				mov.setQta(mov2.getQtaDopoMovim().subtract(giacenza.getQta()));
				mov.setTipoMovim(MovimentoGiacenza.RIMOSSA);
			} else {
				mov.setQta(giacenza.getQta().subtract(mov2.getQtaDopoMovim()));
				mov.setTipoMovim(MovimentoGiacenza.AGGIUNTA);
			}
		}
		mov.setIdGiacenza(giacenza.getId());
		mov.setQtaDopoMovim(giacenza.getQta());
		giacenza.getMovimenti().add(mov);
		broker.store(mov);
	}

	@SuppressWarnings("unchecked")
	public void store(Giacenza giacenza) throws DataAccessException {
		PersistenceBroker broker = null;
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			broker.beginTransaction();
			//CONTROLLO SE ESISTA
			Giacenza giac = new Giacenza();
			giac.setIdArticolo(giacenza.getIdArticolo());
			giac.setLotto(giacenza.getLotto());
			Query query = new QueryByCriteria(giac);
			giac = (Giacenza) broker.getObjectByQuery(query);
			if (giac != null) {
				giacenza.setId(giac.getId());
			}
			//SALVO L'OGGETTO
			broker.retrieveAllReferences(giacenza);
			if (giacenza.getDataScadenza() != null)
				giacenza.setDataScadenza(new java.sql.Date(giacenza.getDataScadenza().getTime()));
			broker.store(giacenza);
			
			//AGGIUNGO MOVIMENTO
			aggiungiMovimento(giacenza, broker);
			broker.commitTransaction();
		} catch (Exception e) {
			broker.abortTransaction();
			throw new DataAccessException(e.getMessage());
		} finally {
			if (broker != null)
				broker.close();
		}
	}

	public void store(DettaglioDDT dt, PersistenceBroker broker) throws DataAccessException {
		Articoli articoli = new Articoli();
		Articolo articolo = null;
		try {
			articolo = new Articolo();
			articolo.setId(dt.getIdArticolo());
			articolo = (Articolo) articoli.findWithAllReferences(articolo,broker);
		} catch (Exception e) {}
		if (articolo != null) {
			try {
				Giacenza giacenza = new Giacenza();
				giacenza.setIdArticolo(articolo.getId());
				giacenza.setLotto(dt.getLotto());
				//CONTROLLO SE ESISTE GIACENZA
				completeReferences(giacenza);
				Giacenza giac = null;
				try {
					giac = (Giacenza) findWithAllReferences(giacenza,broker);
				} catch (Exception e) {}
				if (giac != null) {
					//ESISTE LA GIACENZA
					giacenza.setId(giac.getId());
					giacenza.setDataScadenza(giac.getDataScadenza());
					giacenza.setQta(giac.getQta());
					giacenza.setMovimenti(giac.getMovimenti());
					//CONTROLLO SE ESISTE UN MOVIMENTO OPPURE NO
					MovimentoGiacenza mg = new MovimentoGiacenza();
					mg.setGiacenza(giacenza);
					mg.setIdGiacenza(giacenza.getId());
					mg.setDdt(dt.getDdt());
					mg.setIdDDT(dt.getIdDDT());
					boolean movimentoTrovato = false;
					Iterator itr = giacenza.getMovimenti().iterator();
					while (itr.hasNext() && !movimentoTrovato) {
						MovimentoGiacenza mov = (MovimentoGiacenza) itr.next();
						if (mov.getIdDDT() != null && mov.getIdDDT().intValue() == dt.getIdDDT().intValue())
							movimentoTrovato = true;
					}
					
					if (movimentoTrovato) { //ESISTE IL MOVIMENTO. AGGIORNO LA GIACENZA DELLA DIFFERENZA E AGGIORNO IL MOVIMENTO
						BigDecimal vecchiaQta = getTotaleMovimentiDDT(giacenza, dt.getDdt(), broker);
						giacenza.setQta(giacenza.getQta().add(vecchiaQta).subtract(dt.getQta()));
						broker.store(giacenza);
						mg.setData(new java.sql.Date(new Date().getTime()));
						mg.setQtaDopoMovim(giacenza.getQta());
						if (vecchiaQta.compareTo(dt.getQta()) < 0) {
							mg.setTipoMovim(MovimentoGiacenza.RIMOSSA);
							mg.setQta(dt.getQta().subtract(vecchiaQta));
							broker.store(mg);
						} else if (vecchiaQta.compareTo(dt.getQta()) > 0) {
							mg.setTipoMovim(MovimentoGiacenza.AGGIUNTA);
							mg.setQta(vecchiaQta.subtract(dt.getQta()));
							broker.store(mg);
						}
					} else { //NON ESISTE IL MOVIMENTO. AGGIORNO LA GIACENZA E INSERISCO IL MOVIMENTO
						giacenza.setQta(giacenza.getQta().subtract(dt.getQta()));
						broker.store(giacenza);
						mg.setTipoMovim(MovimentoGiacenza.RIMOSSA);
						mg.setQta(dt.getQta());
						mg.setQtaDopoMovim(giacenza.getQta());
						mg.setData(new java.sql.Date(new Date().getTime()));
						broker.store(mg);
					}
				} else {
					//NON ESISTE LA GIACENZA
					giacenza.setQta(new BigDecimal(0).subtract(dt.getQta()));
					broker.store(giacenza);
					//AGGIUNGO UN MOVIMENTO
					MovimentoGiacenza mg = new MovimentoGiacenza();
					mg.setGiacenza(giacenza);
					mg.setIdGiacenza(giacenza.getId());
					mg.setTipoMovim(MovimentoGiacenza.RIMOSSA);
					mg.setQta(dt.getQta());
					mg.setQtaDopoMovim(giacenza.getQta());
					mg.setDdt(dt.getDdt());
					mg.setIdDDT(dt.getIdDDT());
					mg.setData(new java.sql.Date(new Date().getTime()));
					broker.store(mg);
				}
			} catch (Exception e) {
				broker.abortTransaction();
				throw new DataAccessException(e.getMessage());
			}
		}
	}

	public void delete(DettaglioBollaAcquisto dba, PersistenceBroker broker, boolean cancellaDocumento) throws DataAccessException {
		try {
			Giacenza giacenza = new Giacenza();
			giacenza.setIdArticolo(dba.getIdArticolo());
			giacenza.setLotto(dba.getLotto());
			Giacenza giac = null;
			try {
				giac = (Giacenza) findWithAllReferences(giacenza,broker);
			} catch (Exception e) {}
			if (giac != null) {
				giacenza.setId(giac.getId());
				giacenza.setArticolo(giac.getArticolo());
				giacenza.setDataScadenza(giac.getDataScadenza());
				giacenza.setQta(giac.getQta().subtract(dba.getQta()));
				broker.store(giacenza);
				MovimentoGiacenza mg = new MovimentoGiacenza();
				mg.setGiacenza(giacenza);
				mg.setIdGiacenza(giacenza.getId());
				mg.setBollaAcquisto(dba.getBollaAcquisto());
				mg.setIdBollaAcquisto(dba.getIdBollaAcquisto());
				if (cancellaDocumento)
					mg.setTipoMovim(MovimentoGiacenza.RIMOSSA_PER_CANCELLAZIONE_DOC);
				else
					mg.setTipoMovim(MovimentoGiacenza.RIMOSSA);
				mg.setQta(dba.getQta());
				mg.setQtaDopoMovim(giacenza.getQta());
				mg.setData(new java.sql.Date(new Date().getTime()));
				broker.store(mg);
				//AGGIORNO TUTTE LE RIGHE RIGUARDANTI LA GIACENZA E DDT. TOLGO IL RIF. AL DDT E IMPOSTO FLAG "CANCELLATO"
				if (cancellaDocumento) {
					mg = new MovimentoGiacenza();
					mg.setIdBollaAcquisto(dba.getIdBollaAcquisto());
					mg.setIdGiacenza(giacenza.getId());
					mg.setGiacenza(giacenza);
					Query query = new QueryByCriteria(mg);
					Collection movimenti = broker.getCollectionByQuery(query);
					Iterator itr = movimenti.iterator();
					while (itr.hasNext()) {
						MovimentoGiacenza mov = (MovimentoGiacenza) itr.next();
						mov.setIdBollaAcquisto(null);
						if (mov.getTipoMovim().intValue() == MovimentoGiacenza.AGGIUNTA.intValue())
							mov.setTipoMovim(MovimentoGiacenza.AGGIUNTA_PER_CANCELLAZIONE_DOC);
						if (mov.getTipoMovim().intValue() == MovimentoGiacenza.RIMOSSA.intValue())
							mov.setTipoMovim(MovimentoGiacenza.RIMOSSA_PER_CANCELLAZIONE_DOC);
						broker.store(mov);
					}
				}
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage());
		}
	}
	
	public void delete(DettaglioDDT dt, PersistenceBroker broker, boolean cancellaDocumento) throws DataAccessException {
		Articoli articoli = new Articoli();
		Articolo articolo = null;
		try {
			articolo = articoli.find(dt.getCodiceArticolo());
		} catch (Exception e) {}
		if (articolo != null) {
			try {
				Giacenza giacenza = new Giacenza();
				giacenza.setIdArticolo(articolo.getId());
				giacenza.setLotto(dt.getLotto());
				//CONTROLLO SE ESISTE GIACENZA
				completeReferences(giacenza);
				Giacenza giac = null;
				try {
					giac = (Giacenza) findWithAllReferences(giacenza,broker);
				} catch (Exception e) {}
				if (giac != null) {
					giacenza.setId(giac.getId());
					giacenza.setDataScadenza(giac.getDataScadenza());
					giacenza.setQta(giac.getQta().add(dt.getQta()));
					broker.store(giacenza);
					MovimentoGiacenza mg = new MovimentoGiacenza();
					mg.setGiacenza(giacenza);
					mg.setIdGiacenza(giacenza.getId());
					mg.setDdt(dt.getDdt());
					mg.setIdDDT(dt.getIdDDT());
					if (cancellaDocumento)
						mg.setTipoMovim(MovimentoGiacenza.AGGIUNTA_PER_CANCELLAZIONE_DOC);
					else
						mg.setTipoMovim(MovimentoGiacenza.AGGIUNTA);
					mg.setQta(dt.getQta());
					mg.setQtaDopoMovim(giacenza.getQta());
					mg.setData(new java.sql.Date(new Date().getTime()));
					broker.store(mg);
					//AGGIORNO TUTTE LE RIGHE RIGUARDANTI LA GIACENZA E DDT. TOLGO IL RIF. AL DDT E IMPOSTO FLAG "CANCELLATO"
					if (cancellaDocumento) {
						mg = new MovimentoGiacenza();
						mg.setIdDDT(dt.getIdDDT());
						mg.setIdGiacenza(giacenza.getId());
						Query query = new QueryByCriteria(mg);
						Collection movimenti = broker.getCollectionByQuery(query);
						Iterator itr = movimenti.iterator();
						while (itr.hasNext()) {
							MovimentoGiacenza mov = (MovimentoGiacenza) itr.next();
							mov.setIdDDT(null);
							if (mov.getTipoMovim().intValue() == MovimentoGiacenza.AGGIUNTA.intValue())
								mov.setTipoMovim(MovimentoGiacenza.AGGIUNTA_PER_CANCELLAZIONE_DOC);
							if (mov.getTipoMovim().intValue() == MovimentoGiacenza.RIMOSSA.intValue())
								mov.setTipoMovim(MovimentoGiacenza.RIMOSSA_PER_CANCELLAZIONE_DOC);
							broker.store(mov);
						}
					}
				}
			} catch (Exception e) {
				throw new DataAccessException(e.getMessage());
			}
		}
	}
	
	private BigDecimal getTotaleMovimentiDDT(Giacenza giacenza, DDT ddt, PersistenceBroker broker) {
		BigDecimal totale = new BigDecimal(0);
		try {
			Iterator itr = giacenza.getMovimenti().iterator();
			while (itr.hasNext()) {
				MovimentoGiacenza mg = (MovimentoGiacenza) itr.next();
				if (mg.getIdDDT() != null && mg.getIdDDT().intValue() == ddt.getId().intValue()) {
					if (mg.getTipoMovim().intValue() == MovimentoGiacenza.RIMOSSA.intValue())
						totale = totale.add(mg.getQta());
					else
						totale = totale.subtract(mg.getQta());
				}
			}
		} catch (Exception e) {}
		return totale;
	}
	
	private BigDecimal getTotaleMovimentiBollaAcquisto(Giacenza giacenza, BollaAcquisto bollaAcquisto, PersistenceBroker broker) {
		BigDecimal totale = new BigDecimal(0);
		try {
			Iterator itr = giacenza.getMovimenti().iterator();
			while (itr.hasNext()) {
				MovimentoGiacenza mg = (MovimentoGiacenza) itr.next();
				if (mg.getIdBollaAcquisto() != null && mg.getIdBollaAcquisto().intValue() == bollaAcquisto.getId().intValue()) {
					if (mg.getTipoMovim().intValue() == MovimentoGiacenza.AGGIUNTA.intValue())
						totale = totale.add(mg.getQta());
					else
						totale = totale.subtract(mg.getQta());
				}
			}
		} catch (Exception e) {}
		return totale;
	}

	public Collection getDettagliPerArticolo(Integer idArticolo) throws DataAccessException {
		getCriteria().addEqualTo("idArticolo", idArticolo);
		getCriteria().addNotEqualTo("qta", new BigDecimal(0));
		Collection results = super.getElements();
		Iterator itr = results.iterator();
		while (itr.hasNext()) {
			Giacenza giac = (Giacenza)itr.next();
			completeReferences(giac);
			Iterator itr2 = giac.getMovimenti().iterator();
			while (itr2.hasNext())
				completeReferences((MovimentoGiacenza)itr2.next());
		}
		return results;
	}

	public void store(DettaglioBollaAcquisto dba, PersistenceBroker broker) throws DataAccessException {
		try {
			Giacenza giacenza = new Giacenza();
			giacenza.setIdArticolo(dba.getIdArticolo());
			giacenza.setLotto(dba.getLotto());
			//CONTROLLO SE ESISTE GIACENZA
			completeReferences(giacenza);
			Giacenza giac = null;
			try {
				giac = (Giacenza) findWithAllReferences(giacenza,broker);
			} catch (Exception e) {}
			if (giac != null) {
				//ESISTE LA GIACENZA
				giacenza.setId(giac.getId());
				giacenza.setDataScadenza(giac.getDataScadenza());
				giacenza.setQta(giac.getQta());
				giacenza.setMovimenti(giac.getMovimenti());
				//CONTROLLO SE ESISTE UN MOVIMENTO OPPURE NO
				MovimentoGiacenza mg = new MovimentoGiacenza();
				mg.setGiacenza(giacenza);
				mg.setIdGiacenza(giacenza.getId());
				mg.setIdBollaAcquisto(dba.getIdBollaAcquisto());
				mg.setBollaAcquisto(dba.getBollaAcquisto());
				/*MovimentoGiacenza tmp = null;
				try {
					tmp = (MovimentoGiacenza) findWithAllReferences(mg);
				} catch (Exception e) {}*/
				boolean movimentoTrovato = false;
				Iterator itr = giacenza.getMovimenti().iterator();
				while (itr.hasNext() && !movimentoTrovato) {
					MovimentoGiacenza mov = (MovimentoGiacenza) itr.next();
					if (mov.getIdBollaAcquisto() != null && mov.getIdBollaAcquisto().intValue() == dba.getIdBollaAcquisto().intValue())
						movimentoTrovato = true;
				}
				if (movimentoTrovato) { //ESISTE IL MOVIMENTO. AGGIORNO LA GIACENZA DELLA DIFFERENZA E AGGIORNO IL MOVIMENTO
					BigDecimal vecchiaQta = getTotaleMovimentiBollaAcquisto(giacenza, dba.getBollaAcquisto(), broker);
					giacenza.setQta(giacenza.getQta().subtract(vecchiaQta).add(dba.getQta()));
					if (dba.getDataScadenza() != null)
						giacenza.setDataScadenza(new java.sql.Date(dba.getDataScadenza().getTime()));
					broker.store(giacenza);
					mg.setData(new java.sql.Date(new Date().getTime()));
					mg.setQtaDopoMovim(giacenza.getQta());
					if (vecchiaQta.compareTo(dba.getQta()) < 0) { //VECCHIA QTA MINORE NUOVA QTA
						mg.setTipoMovim(MovimentoGiacenza.AGGIUNTA);
						mg.setQta(dba.getQta().subtract(vecchiaQta));
						broker.store(mg);
					} else if (vecchiaQta.compareTo(dba.getQta()) > 0) { //VECCHIA QTA MAGGIORE NUOVA QTA
						mg.setTipoMovim(MovimentoGiacenza.RIMOSSA);
						mg.setQta(vecchiaQta.subtract(dba.getQta()));
						broker.store(mg);
					}
				} else { //NON ESISTE IL MOVIMENTO. AGGIORNO LA GIACENZA E INSERISCO IL MOVIMENTO
					giacenza.setQta(giacenza.getQta().add(dba.getQta()));
					if (dba.getDataScadenza() != null)
						giacenza.setDataScadenza(new java.sql.Date(dba.getDataScadenza().getTime()));
					broker.store(giacenza);
					mg.setTipoMovim(MovimentoGiacenza.AGGIUNTA);
					mg.setQta(dba.getQta());
					mg.setQtaDopoMovim(giacenza.getQta());
					mg.setData(new java.sql.Date(new Date().getTime()));
					broker.store(mg);
				}
			} else {
				//NON ESISTE LA GIACENZA
				giacenza.setQta(dba.getQta());
				if (dba.getDataScadenza() != null)
					giacenza.setDataScadenza(new java.sql.Date(dba.getDataScadenza().getTime()));
				broker.store(giacenza);
				//AGGIUNGO UN MOVIMENTO
				MovimentoGiacenza mg = new MovimentoGiacenza();
				mg.setGiacenza(giacenza);
				mg.setIdGiacenza(giacenza.getId());
				mg.setTipoMovim(MovimentoGiacenza.AGGIUNTA);
				mg.setQta(dba.getQta());
				mg.setQtaDopoMovim(giacenza.getQta());
				mg.setIdBollaAcquisto(dba.getIdBollaAcquisto());
				mg.setBollaAcquisto(dba.getBollaAcquisto());
				mg.setData(new java.sql.Date(new Date().getTime()));
				broker.store(mg);
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage());
		}
	}
	
	public void setQueryDeleteBulkByCriteriaIdArticoli(List<String> idArticoli){
		Criteria criteria = new Criteria();
		criteria.addIn("idArticolo", idArticoli);
		
		queryDeleteBulk = QueryFactory.newQuery(elementClass, criteria);
	}
}
