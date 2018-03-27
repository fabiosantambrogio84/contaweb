package forms;

import dao.DataAccessException;
import dao.Fornitori;
import dao.Pagamenti;
import vo.*;

public class EditPagamento extends Edit {

	private static final long serialVersionUID = 1L;
	private Pagamento pagamento = null;
	
   public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

public String input() {
  		if (id!= null && id != 0) {
  			try {
  				if (getAction().equalsIgnoreCase("delete")) {
  					return delete();
  				}
  				setPagamento(new Pagamenti().find(id));
  			} catch (Exception e) {
  				stampaErrore("EditPagamento.input()",e);
  				return ERROR;
  			}
  		}

  		return INPUT;
	 }
	 
	 protected String store() {
		try {
			new Pagamenti().store(pagamento);

		} catch (DataAccessException e) {
			stampaErrore("EditPagamento.store()",e);
			return ERROR_DELETE;
		}
		 return SUCCESS;
	 }
	 
	 protected String delete() {
		 try {
			 pagamento = new Pagamento();
			 pagamento.setId(id);
			new Fornitori().delete(pagamento);
		} catch (DataAccessException e) {
			stampaErrore("EditPagamento.delete()",e);
			return ERROR;
		}
	 	 return SUCCESS;
	 }
}