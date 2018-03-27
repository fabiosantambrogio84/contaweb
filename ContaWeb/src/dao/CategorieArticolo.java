package dao;

import vo.CategoriaArticolo;

public class CategorieArticolo extends DataAccessObject {
	
	public void store(CategoriaArticolo vo) throws DataAccessException{
		super.store(vo);
	}
	
	public CategorieArticolo(){
		this.elementClass = CategoriaArticolo.class;
	}
	
	public CategoriaArticolo find(int id) throws DataAccessException {
		CategoriaArticolo autista = new CategoriaArticolo();
		autista.setId(id);
		autista = (CategoriaArticolo)find(autista);
		return autista;
	}
	
	public void setOrderByDescrizione(int order) {
		if (order == ORDER_DESC)
			getQueryByCriteria().addOrderByDescending("posizione");
		else
			getQueryByCriteria().addOrderByAscending("posizione");
	}
	
	protected void setDefaultCriteria() {
		setOrderByDescrizione(ORDER_ASC);
	}

}
