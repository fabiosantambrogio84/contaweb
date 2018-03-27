package dao;

import vo.Fornitore;

public class Fornitori extends DataAccessObject {
	
	public Fornitore find(int idFornitore) throws DataAccessException {
		Fornitore fornitore = new Fornitore();
		fornitore.setId(idFornitore);
		
		return (Fornitore)find(fornitore);
	}
	
	public Fornitori() {
		this.elementClass = Fornitore.class;
	}
	
	public void setOrderByDescrizione(int order) {
		useDefaultCriteria = false;
		if (order == ORDER_DESC)
			getQueryByCriteria().addOrderByDescending("descrizione");
		else
			getQueryByCriteria().addOrderByAscending("descrizione");
	}
}
