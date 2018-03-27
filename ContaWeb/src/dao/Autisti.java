package dao;

import java.util.Collection;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;

import vo.Autista;
import vo.DettaglioOrdine;
import vo.Ordine;
import dao.Ordini;

public class Autisti extends DataAccessObject {
	
	public void store(Autista autista) throws DataAccessException{
		super.store(autista);
	}
	
	public Autisti(){
		this.elementClass = Autista.class;		
	}
	
	public Autista find(int idAutista) throws DataAccessException {
		Autista autista = new Autista();
		autista.setId(idAutista);
		autista = (Autista)find(autista);
		return autista;
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

	public Collection listAutisti() throws DataAccessException {
		useDefaultCriteria = false;
		getQueryByCriteria().addOrderByAscending("nome");
		return getElements();
	}

}
