package dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;

import utils.ENoteCreditoHelper;
import vo.Articolo;
import vo.Cliente;
import vo.DettaglioDDT;
import vo.Fornitore;
import vo.TotaliStatistica;

public class StatisticheVendite extends DataAccessObject {
	
	static final public int ANNO_CORRENTE = 0;
	static final public int MESE_CORRENTE = 1;
	static final public int PERIODO_CORRENTE = 2;
	
	private Cliente cliente = null;
	private List<Integer> clienti = null;
	
	private Fornitore fornitore = null;
	private Articolo articolo = null;
	private Integer periodo = null;
	private Date dataInizio = null;
	private Date dataFine = null;
	private String lotto = null;

	public String getLotto() {
		return lotto;
	}

	public void setLotto(String lotto) {
		this.lotto = lotto;
	}
	
	public List<Integer> getClienti(){
		return clienti;
	}
	
	public void setClienti(List<Integer> clienti){
		this.clienti = clienti;
	}
	
	private Criteria filterCriteria(Criteria criteria) {
		if (fornitore != null) {//FILTRO IL FORNITORE
			//FIXME: TOGLIERE IL CODICE SQL!
			criteria.addSql("A0.idArticolo in (select idArticolo from articoli where idFornitore =" + fornitore.getId() + ")");
		}
			
		if (articolo != null) //FILTRO L'ARTICOLO
			criteria.addColumnEqualTo("idArticolo", articolo.getId());
		
		if (lotto !=null && !lotto.equalsIgnoreCase("")) //FILTRO IL LOTTO
			criteria.addColumnEqualTo("lotto", lotto);
		
//		if (cliente != null) {
//			Criteria criteriaCliente = new Criteria();
//			criteriaCliente.addColumnEqualTo("ddt.idCliente", cliente.getId());
//			criteria.addAndCriteria(criteriaCliente);
//		}
		if(clienti != null){
			Criteria criteriaClienti = new Criteria();
			criteriaClienti.addColumnIn("ddt.idCliente", clienti);
			criteria.addAndCriteria(criteriaClienti);
		}
		return criteria;
	}
	
	public Criteria getCriteria() {
		if (criteria == null) {
			criteria = new Criteria();
			criteria = filterCriteria(criteria);
			
			if (periodo == ANNO_CORRENTE) {
				Criteria criteriaPeriodo = new Criteria();
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.MONTH,Calendar.JANUARY);
				Date inizioAnno = new java.sql.Date(cal.getTime().getTime());
				criteriaPeriodo.addGreaterOrEqualThan("ddt.data", inizioAnno);
				criteria.addAndCriteria(criteriaPeriodo);
			}
			
			if (periodo == MESE_CORRENTE) {
				Criteria criteriaPeriodo = new Criteria();
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.DAY_OF_MONTH, 1);
				Date inizioMese = new java.sql.Date(cal.getTime().getTime());
				criteriaPeriodo.addGreaterOrEqualThan("ddt.data", inizioMese);
				criteria.addAndCriteria(criteriaPeriodo);
			}
			
			if (periodo == PERIODO_CORRENTE) {
				Criteria criteriaPeriodo = new Criteria();
				criteriaPeriodo.addBetween("ddt.data", new java.sql.Date(dataInizio.getTime()),new java.sql.Date(dataFine.getTime()));
				criteria.addAndCriteria(criteriaPeriodo);
			}
		}
		return criteria;
	}

	private Collection calcolaDettagli(boolean groupArticoli) throws DataAccessException {
		PersistenceBroker broker = null;
		Collection results = null;
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			if (!groupArticoli) {
				query = QueryFactory.newQuery(DettaglioDDT.class, getCriteria());
				results = broker.getCollectionByQuery(query);
			} else {
				ReportQueryByCriteria q = QueryFactory.newReportQuery(DettaglioDDT.class, getCriteria());
				q.setAttributes(new String[] { "articolo.codiceArticolo", "articolo.descrizione", "count(*)", "sum(qta)", "sum(totale)","avg(totale)"});
				q.addGroupBy("idArticolo");
				q.addOrderByDescending("sum(totale)");
				Iterator itr = broker.getReportQueryIteratorByQuery(q);
				results = new ArrayList<Object[]>();
				while (itr.hasNext()) {					
					results.add(itr.next());
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException(e.getMessage());
		} finally {
			if (broker != null)
				broker.close();
		}
		return results;
	}

	public Articolo getArticolo() {
		return articolo;
	}

	public void setArticolo(Articolo articolo) {
		this.articolo = articolo;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Fornitore getFornitore() {
		return fornitore;
	}

	public void setFornitore(Fornitore fornitore) {
		this.fornitore = fornitore;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@SuppressWarnings("unchecked")
	public Vector calcolaValoriGrafico() {
		Vector vector = new Vector();
		PersistenceBroker broker = null;
		try {
			Calendar now = Calendar.getInstance();
			Calendar cal = Calendar.getInstance();
			
			int offset = Calendar.MONTH;
			
			if (periodo == ANNO_CORRENTE) {
				offset = Calendar.MONTH;
				cal.set(Calendar.MONTH, 0);
				cal.set(Calendar.DAY_OF_MONTH, 1);
			}
	
			if (periodo == MESE_CORRENTE) {
				offset = Calendar.WEEK_OF_MONTH;
				cal.set(Calendar.DAY_OF_MONTH, 1);
			}
	
			if (periodo == PERIODO_CORRENTE) {
				offset = Calendar.DAY_OF_YEAR;
				cal.setTime(dataInizio);
				now.setTime(dataFine);
			}
			
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			while (cal.getTime().compareTo(now.getTime())<=0) {
				try {
					Criteria criteriaPeriodo = new Criteria();
					if (periodo == PERIODO_CORRENTE) {
						criteriaPeriodo.addEqualTo("ddt.data", new java.sql.Date(cal.getTime().getTime()));
						cal.add(offset, 1);
					}
					if (periodo == ANNO_CORRENTE) {
						java.sql.Date dataInizio = new java.sql.Date(cal.getTime().getTime());
						cal.add(offset, 1);
						cal.add(Calendar.DAY_OF_YEAR, -1);
						java.sql.Date dataFine = new java.sql.Date(cal.getTime().getTime());
						cal.add(Calendar.DAY_OF_YEAR, 1);
						criteriaPeriodo.addBetween("ddt.data", dataInizio, dataFine);
					}
					if (periodo == MESE_CORRENTE) {
						java.sql.Date dataInizio = new java.sql.Date(cal.getTime().getTime());
						cal.add(offset, 1);
						cal.add(Calendar.DAY_OF_YEAR, -1);
						java.sql.Date dataFine = new java.sql.Date(cal.getTime().getTime());
						cal.add(Calendar.DAY_OF_YEAR, 1);
						criteriaPeriodo.addBetween("ddt.data", dataInizio, dataFine);
					}
					
					Criteria criteria = new Criteria();
					criteria = filterCriteria(criteria);
					criteria.addAndCriteria(criteriaPeriodo);
					
					ReportQueryByCriteria q = QueryFactory.newReportQuery(DettaglioDDT.class, criteria);
					q.setAttributes(new String[] { "sum(totale)" });
					Iterator iter = broker.getReportQueryIteratorByQuery(q);
					Object[] obj = (Object[]) iter.next();
					vector.add((BigDecimal) obj[0]);
				} catch (Exception e) {
					vector.add(new BigDecimal(0));
				}
			}
			
		} finally {
			if (broker != null)
				broker.close();			
		}

		return vector;
	}
	
	public TotaliStatistica calcolaTotali(boolean dettagli, boolean groupArticoli) throws DataAccessException {
		TotaliStatistica tot = new TotaliStatistica();
		PersistenceBroker broker = null;
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			ReportQueryByCriteria q = QueryFactory.newReportQuery(DettaglioDDT.class, getCriteria());
			q.setAttributes(new String[] { "sum(totale)","avg(totale)","sum(qta)"});
			Iterator iter = broker.getReportQueryIteratorByQuery(q);
			
			if (iter != null && iter.hasNext()) {
				Object[] obj = (Object[]) iter.next();
				if (obj[0] == null) return null;
				tot.setTotale((BigDecimal) obj[0]);
				tot.setMedia(((BigDecimal) obj[1]).setScale(2, BigDecimal.ROUND_HALF_UP));
				tot.setQtaVenduta((BigDecimal) obj[2]);
			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException(e.getMessage());
		} finally {
			if (broker != null)
				broker.close();
		}
		if (dettagli)
			tot.setDettagli(calcolaDettagli(groupArticoli));
		return tot;
	}

	public Collection calcolaArtVendutiSettimana() throws DataAccessException {
		return calcolaArtVenduti(true, null, null);
	}
	
	public Collection calcolaArtVendutiSettimana(Integer idCliente,
			Integer idPuntoConsegna) throws DataAccessException {
		return calcolaArtVenduti(true, idCliente, idPuntoConsegna);
	}
	
	public Collection calcolaArtVendutiMese() throws DataAccessException {
		return calcolaArtVenduti(false, null, null);
	}
	
	public Collection calcolaArtVendutiMese(Integer idCliente,
			Integer idPuntoConsegna) throws DataAccessException {
		return calcolaArtVenduti(false, idCliente, idPuntoConsegna);
	}

	public Collection calcolaArtVenduti(boolean settimana, Integer idCliente,
			Integer idPuntoConsegna) throws DataAccessException {
		
		PersistenceBroker broker = null;
		ArrayList col = new ArrayList();
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			Criteria criteria = new Criteria();
			
			if (idCliente != null)
				criteria.addEqualTo("ddt.idCliente", idCliente);
			
			if (idPuntoConsegna != null && idPuntoConsegna != -1)
				criteria.addEqualTo("ddt.idPuntoConsegna", idPuntoConsegna);

			criteria.addNotNull("idArticolo");
			
			Calendar cal = Calendar.getInstance(Locale.ITALIAN);
			Date dataInizio = null,dataFine = null;
			if (settimana) {
				cal.add(Calendar.WEEK_OF_YEAR, -1);
				cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
				dataInizio = cal.getTime();
				cal.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
				dataFine = cal.getTime();
				
			} else {
				cal.add(Calendar.MONTH, -1);
				cal.set(Calendar.DAY_OF_MONTH,1);
				dataInizio = cal.getTime();
				cal.add(Calendar.MONTH, 1);
				cal.add(Calendar.DAY_OF_YEAR, - 1);
				dataFine = cal.getTime();
			}
			
			criteria.addGreaterOrEqualThan("ddt.data", new java.sql.Date(dataInizio.getTime()));
			criteria.addLessOrEqualThan("ddt.data", new java.sql.Date(dataFine.getTime()));

			ReportQueryByCriteria q = QueryFactory.newReportQuery(DettaglioDDT.class, criteria);
			q.addGroupBy("idArticolo");
			q.setAttributes(new String[] { "idArticolo","sum(totale)"});
			q.addOrderByDescending("sum(totale)");
			q.setEndAtIndex(15);
			
			Iterator iter = broker.getReportQueryIteratorByQuery(q);
			Articoli articoli = new Articoli();
			
			while (iter.hasNext()) {
				Object[] objs = (Object[]) iter.next();
				Articolo articolo = articoli.find((Integer)objs[0]);
				col.add(articolo);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException(e.getMessage());
		} finally {
			if (broker != null)
				broker.close();
		}
		
		return col;
		
	}
}
