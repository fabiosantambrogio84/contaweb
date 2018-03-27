package dao;

import java.util.Collection;

/* ***
    import org.apache.ojb.broker.query.Criteria;
    import org.apache.ojb.broker.query.Query;
    import org.apache.ojb.broker.query.QueryFactory;
*** */	

import vo.Agente;
/* **.
import vo.DettaglioOrdine;
import vo.Ordine;
import dao.Ordini;
**. */

public class Agenti extends DataAccessObject {
	public void store(Agente agente) throws DataAccessException{
		super.store(agente);
	}
	
	public Agenti(){
		this.elementClass = Agente.class;		
	}
	
	public Agente find(int idAgente) throws DataAccessException {
		Agente agente = new Agente();
		agente.setId(idAgente);
		agente = (Agente)find(agente);
		return agente;
	}
	
	public void setOrderByDescrizione(int order){
		if (order == ORDER_DESC)
			getQueryByCriteria().addOrderByDescending("nome");
		else
			getQueryByCriteria().addOrderByAscending("nome");
	}
	
	protected void setDefaultCriteria() {
		setOrderByDescrizione(ORDER_ASC);
	}

	public Collection listAgenti() throws DataAccessException {
		useDefaultCriteria = false;
		getQueryByCriteria().addOrderByAscending("nome");
		return getElements();
	}

}
