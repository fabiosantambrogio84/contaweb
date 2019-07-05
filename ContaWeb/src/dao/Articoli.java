package dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.ojb.broker.query.Criteria;

import vo.Articolo;
import vo.DDT;
import vo.VOElement;

public class Articoli extends DataAccessObject {

	protected void setCriteriaUsingFilterKey() {
		
		Criteria criteria = getCriteria();

		if(filterKey != null && filterKey.length() > 0)
		{
			//NON E' UNA DATA CERCO NEGLI ALTRI CAMPI
			criteria.addLike("descrizione", "%" + filterKey + "%");
			
			//FILTRO PER NUMERO
			Criteria criteriaNprog = new Criteria();
			criteriaNprog.addEqualTo("codiceArticolo", filterKey);
	
			//FILTRO PER FORNITORE
			Criteria criteriaFornitore = new Criteria();
			criteriaFornitore.addLike("fornitore.descrizione", "%" + filterKey + "%");
			criteriaNprog.addOrCriteria(criteriaFornitore);		
			criteria.addOrCriteria(criteriaNprog);
		}
		
	}
	
	public String store(VOElement element)
			throws DataAccessException
	{
		Articolo dbo = (Articolo)element;
		// dbo.setDataAggiornamento(new java.sql.Timestamp(new Date().getTime()));
		dbo.setDataAggiornamento(new Date());
		return super.store(element);
	}
	
	public Articoli() {
		this.elementClass = Articolo.class;
	}
	
	public Articolo find(Integer idArticolo) throws DataAccessException {
		if (idArticolo == null)
			return null;

		Articolo articolo = new Articolo();
		articolo.setId(idArticolo);
		articolo = (Articolo)findWithAllReferences(articolo);
		return articolo;
	}
	
	public Articolo find(String codiceArticolo) throws DataAccessException {
		Articolo articolo = new Articolo();
		articolo.setCodiceArticolo(codiceArticolo);
		articolo = (Articolo)findWithAllReferences(articolo);
		return articolo;
	}
	
	public Collection listDaCodiceParziale(String codiceArticolo) throws DataAccessException {
		useDefaultCriteria = false;
		getCriteria().addLike("codice", "%" + codiceArticolo + "%");
		getQueryByCriteria().addOrderByAscending("codiceArticolo");
		return getActiveElements();
	}
	
	public Collection listDaDescrizioneParziale(String descrizioneArticolo) throws DataAccessException {
		useDefaultCriteria = false;
		getCriteria().addLike("descrizione", descrizioneArticolo + "%");
		getQueryByCriteria().addOrderByAscending("codiceArticolo");
		return getActiveElements();
	}
	
	public Collection listDaCodiceDescrParziale(String codDescrArticolo) throws DataAccessException {
	
		useDefaultCriteria = false;
		
		getCriteria().addLike("codice", codDescrArticolo + "%");
		
		if(codDescrArticolo.length() > 2){
			Criteria description = new Criteria();
			description.addLike("descrizione", codDescrArticolo + "%");
			getCriteria().addOrCriteria(description);
		}
		getCriteria().addEqualTo("prodottoUsoInterno", false);
		getQueryByCriteria().addOrderByAscending("codiceArticolo");
		
		return getActiveElements();
	}
	
	public void setOrderByDescrizione(int order) {
		if (order == ORDER_DESC)
			getQueryByCriteria().addOrderByDescending("descrizione");
		else
			getQueryByCriteria().addOrderByAscending("descrizione");
	}
	
	public void setOrderByFornitore(int order) {
		useDefaultCriteria = false;
		if (order == ORDER_DESC)
			getQueryByCriteria().addOrderByDescending("fornitore.descrizione");
		else
			getQueryByCriteria().addOrderByAscending("fornitore.descrizione");
	}
	
	
	protected void setDefaultCriteria() {
		setOrderByDescrizione(ORDER_ASC);
	}

	public Articolo findByBarcode(String barcode) {
		try {
			Criteria incomplete = new Criteria();
			String barcodeIncomplete = barcode.substring(0, barcode.length() - 6);
			incomplete.addEqualTo("barcode", barcodeIncomplete);
			getCriteria().addEqualTo("barcode", barcode);
			getCriteria().addOrCriteria(incomplete);
			
			Collection col = getElements();
			Iterator itr = col.iterator();
			if (col.size() > 1) {
				Articolo articolo = null;
				Map<BigDecimal, Articolo> map = new HashMap<BigDecimal, Articolo>();
				while (itr.hasNext()) {
					articolo = (Articolo) itr.next();
					map.put(articolo.getQtaPredefinita(), articolo);
				}
				//Leggo qta
				double value = Double.parseDouble(barcode.substring(barcode.length() - 6, barcode.length()));
				value = value / 10000;
				BigDecimal qtaLetta = new BigDecimal(value).setScale(3,BigDecimal.ROUND_HALF_UP);
				BigDecimal bestValue = null;
				BigDecimal bestDiff = null;
				Iterator<BigDecimal> itr2 = map.keySet().iterator();
				while (itr2.hasNext()) {
					BigDecimal currentValue = itr2.next();
					BigDecimal diff = qtaLetta.subtract(currentValue).abs();
					if (bestValue == null || bestDiff.compareTo(diff) > 0)
						bestValue = currentValue;
						bestDiff = diff;
				}
				
				return map.get(bestValue);
			} else
				if (itr.hasNext())
					return (Articolo) itr.next();
			else return null;
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public Collection getActiveElements() throws DataAccessException {
		getCriteria().addEqualTo("attivo", true);
		return getElements();
	}
	
	public Collection getElements(Integer idFornitore) throws DataAccessException {
		getCriteria().addEqualTo("idFornitore", idFornitore);
		return getElements();
	}
	

	public Collection<Articolo> getListaArticoliFornitore(Integer idFornitore) throws DataAccessException {
		getCriteria().addEqualTo("idFornitore", idFornitore);
		return getElements();
	}
	
	public Collection<Articolo> getListaArticoliAttivi(Integer idFornitore, Integer idCategoria) throws DataAccessException {
		getCriteria().addEqualTo("idFornitore", idFornitore);
		getCriteria().addEqualTo("idCategoria", idCategoria);
		getCriteria().addEqualTo("attivo", true);
		
		return getElements();
	}
	public Collection<Articolo> getListaArticoliAttivi() throws DataAccessException {
		getCriteria().addEqualTo("attivo", true);
		
		return getElements();
	}
	public Collection<Articolo> getListaArticoliCategoria(Integer idCategoria) throws DataAccessException {
		getCriteria().addEqualTo("idCategoria", idCategoria);
		getCriteria().addEqualTo("attivo", true);
		
		return getElements();
	}
	
	public Collection getListaArticoliFilter(String q) throws DataAccessException{
		Criteria criteria = getCriteria();
		criteria.addEqualTo("attivo", true);
		criteria.addLike("codice", "%" + q + "%");
		criteria.addLike("descrizione", "%" + q + "%");
		
		return getElements();
	}
}
