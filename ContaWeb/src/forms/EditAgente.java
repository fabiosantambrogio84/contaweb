package forms;

import java.util.Collection;

import vo.Agente;
import dao.Agenti;
import dao.Clienti;

public class EditAgente extends Edit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2343419772046019464L;
	
	private Integer id = null;
	private Agente agente = null;

	@Override
	protected String store() {
		// TODO Auto-generated method stub
		try {
			Agenti agenti = new Agenti();
			agenti.store(agente);
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
			Collection clienti = new Clienti().getFiltratoAgente(id);
			if (clienti.isEmpty()){
				Agente agente = new Agente();
				agente.setId(id);
				new Agenti().delete(agente);
			} else {
				return ERROR_DELETE;
			}
		} catch (Exception e) {
			stampaErrore("Edit agente", e);
			// TODO: handle exception
			return ERROR_DELETE;
		}
		
		return SUCCESS;
	}
	
	public String input(){
		try {
  			if (getAction().equalsIgnoreCase("edit")) {
  				Agenti agenti = new Agenti();
  				agente = agenti.find(id);
  			} else if (getAction().equalsIgnoreCase("delete")){
  				return delete();
  			} else {
  				agente = new Agente();
  			}
		} catch (Exception e) {
			// TODO: handle exception
			return ERROR;
		}
		return INPUT;
	}

	public Agente getAgente() {
		return agente;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
