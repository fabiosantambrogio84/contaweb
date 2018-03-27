package forms;

import java.util.Collection;

import vo.Autista;
import dao.Autisti;
import dao.Clienti;

public class EditAutista extends Edit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2343419772046019464L;
	
	private Integer id = null;
	private Autista autista = null;

	@Override
	protected String store() {
		// TODO Auto-generated method stub
		try {
			Autisti autisti = new Autisti();
			autisti.store(autista);
		} catch (Exception e) {
			// TODO: handle exception
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	

	@Override
	protected String delete() {
		// TODO Auto-generated method stub
		try {
			Collection clienti = new Clienti().getFiltratoAutista(id);
			if (clienti.isEmpty()){
				Autista autista = new Autista();
				autista.setId(id);
				new Autisti().delete(autista);
			} else {
				return ERROR_DELETE;
			}
		} catch (Exception e) {
			stampaErrore("Edit autista", e);
			// TODO: handle exception
			return ERROR_DELETE;
		}
		
		return SUCCESS;
	}
	
	public String input(){
		try {
  			if (getAction().equalsIgnoreCase("edit")) {
  				Autisti autisti = new Autisti();
  				autista = autisti.find(id);
  			} else if (getAction().equalsIgnoreCase("delete")){
  				return delete();
  			} else {
  				autista = new Autista();
  			}
		} catch (Exception e) {
			// TODO: handle exception
			return ERROR;
		}
		return INPUT;
	}

	public Autista getAutista() {
		return autista;
	}

	public void setAutista(Autista autista) {
		this.autista = autista;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
