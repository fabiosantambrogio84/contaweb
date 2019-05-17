package dao;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;

import vo.Articolo;
import vo.Cliente;
import vo.Fornitore;
import vo.Sconto;

public class Sconti extends DataAccessObject {

	public Sconti() {
		this.elementClass = Sconto.class;
	}

	public Sconto find(int idSconto) throws DataAccessException {
		Sconto sconto = new Sconto();
		sconto.setId(idSconto);		
		return (Sconto)find(sconto);
	}
	
	protected void setCriteriaUsingFilterKey() {
		if(filterKey != null && filterKey.length() > 0)
		{
			//FILTRO PER RS
			getCriteria().addLike("cliente.rs", "%" + filterKey + "%");
			
			//FILTRO PER RS2
			Criteria criteriaRs2 = new Criteria();
			criteriaRs2.addLike("cliente.rs2", "%" + filterKey + "%");		
			getCriteria().addOrCriteria(criteriaRs2);
		}
	}
	
	public Sconto find(Cliente cliente, Fornitore fornitore, Articolo articolo, Date data) {
		useDefaultCriteria = false;
		Criteria criteria = getCriteria();
		criteria.addEqualTo("idCliente", cliente.getId());
		
		Criteria criteriaForn = new Criteria();
		criteriaForn.addEqualTo("idFornitore", fornitore.getId());
		criteriaForn.addIsNull("idArticolo");
		
		Criteria criteriaArt = new Criteria();
		criteriaArt.addEqualTo("idArticolo", articolo.getId());
		criteriaArt.addOrCriteria(criteriaForn);
		
		Criteria criteriaData = new Criteria();
		criteriaData.addIsNull("dataScontoDal");
		Criteria criteriaDataInt = new Criteria();
		criteriaDataInt.addLessOrEqualThan("dataScontoDal", new java.sql.Date(data.getTime()));
		criteriaDataInt.addGreaterOrEqualThan("dataScontoAl", new java.sql.Date(data.getTime()));
		criteriaData.addOrCriteria(criteriaDataInt);
		
		criteria.addAndCriteria(criteriaArt);
		criteria.addAndCriteria(criteriaData);
		
		//ORDER BY...
		getQueryByCriteria().addOrderByDescending("idArticolo");
		getQueryByCriteria().addOrderByDescending("dataScontoDal");
		
		Collection elements = null;
		try {
			elements = getElements();
		} catch (DataAccessException e) {
			return null;
		}
		
		if (elements.size() > 0)
			return (Sconto) elements.toArray()[0];
		else
			return null;
	}
	
	protected void setDefaultCriteria() {
		//Non mostrare sconti gia scaduti
		Criteria criteria = new Criteria();
		criteria.addIsNull("dataScontoAl");
		
		Criteria criteria2 = new Criteria();
		criteria2.addNotNull("dataScontoAl");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_YEAR, -2);
		criteria2.addGreaterOrEqualThan("dataScontoAl", new java.sql.Date(cal.getTime().getTime()));		
		
		criteria.addOrCriteria(criteria2);
		
		getCriteria().addAndCriteria(criteria);
		
		getQueryByCriteria().addOrderByAscending("cliente.rs");
		getQueryByCriteria().addOrderByDescending("dataDal");
	}
	
	public void setQueryDeleteBulkByCriteriaIds(List<String> ids){
		Criteria criteria = new Criteria();
		criteria.addIn("id", ids);
		
		queryDeleteBulk = QueryFactory.newQuery(elementClass, criteria);
	}
}
