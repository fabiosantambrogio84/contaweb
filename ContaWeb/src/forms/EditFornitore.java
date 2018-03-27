package forms;

import dao.DataAccessException;
import dao.Fornitori;
import vo.*;

public class EditFornitore extends Edit {

	private static final long serialVersionUID = 1L;
	private Fornitore fornitore = null;
		
	public void setFornitore(Fornitore fornitore) {
		this.fornitore = fornitore;
	}
	
	public Fornitore getFornitore() {
		return fornitore;
	}
	
   public String input() {
  		if (id != null && id != 0) {
  			try {
  				if (getAction().equalsIgnoreCase("delete")) {
  					return delete();
  				}
  				
  				setFornitore(new Fornitori().find(id));
  			} catch (Exception e) {
  				stampaErrore("EditFornitore.input()",e);
  				return ERROR;
  			}
  		}

  		return INPUT;
	 }
	 
	 protected String store() {
		try {
			new Fornitori().store(fornitore);

		} catch (DataAccessException e) {
			stampaErrore("EditFornitore.store()",e);
			return ERROR;
		}
		 return SUCCESS;
	 }
	 
	 protected String delete() {
		 try {
			fornitore = new Fornitore();
			fornitore.setId(id);
			new Fornitori().delete(fornitore);
		} catch (DataAccessException e) {
			stampaErrore("EditFornitore.delete()",e);
			return ERROR_DELETE;
		}
	 	 return SUCCESS;
	 }
}