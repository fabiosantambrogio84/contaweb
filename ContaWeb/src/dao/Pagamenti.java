package dao;

import java.util.Collection;

import vo.Pagamento;

public class Pagamenti extends DataAccessObject {

	public Collection listPagamenti() throws DataAccessException {
		getQueryByCriteria().addOrderByAscending("descrizione");
		return getElements();
	}
	
	public Pagamento find(Integer idPagamento) throws DataAccessException {
		Pagamento pagamento = new Pagamento();
		pagamento.setId(idPagamento);
		return (Pagamento)find(pagamento);
	}
		
	public Pagamenti() {
		this.elementClass = Pagamento.class;
	}
}
