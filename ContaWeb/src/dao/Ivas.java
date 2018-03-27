package dao;

import vo.Iva;

public class Ivas extends DataAccessObject {

	public Iva find(Integer idIva) throws DataAccessException {
		Iva iva = new Iva();
		iva.setId(idIva);
		return (Iva)find(iva);
	}
	
		
	public Ivas() {
		this.elementClass = Iva.class;
	}
	
	public void setOrderByDescrizione(int order) {
		useDefaultCriteria = false;
		if (order == ORDER_DESC)
			getQueryByCriteria().addOrderByDescending("valore");
		else
			getQueryByCriteria().addOrderByAscending("valore");
	}
}
