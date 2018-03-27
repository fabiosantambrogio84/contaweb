package forms;

import java.util.Collection;

import vo.Agente;
import vo.Iva;
import dao.Agenti;
import dao.Clienti;
import dao.Ivas;

public class EditIva extends Edit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2343419772046019464L;
	
	private Integer id = null;
	private Iva aliquota = null;

	@Override
	protected String store() {
		// TODO Auto-generated method stub
		try {
			Ivas agenti = new Ivas();
			agenti.store(aliquota);
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
				Iva agente = new Iva();
				agente.setId(id);
				new Ivas().delete(agente);
			} else {
				return ERROR_DELETE;
			}
		} catch (Exception e) {
			stampaErrore("Edit Agente", e);
			// TODO: handle exception
			return ERROR_DELETE;
		}
		
		return SUCCESS;
	}
	
	public String input(){
		try {
  			if (getAction().equalsIgnoreCase("edit")) {
  				Ivas Agenti = new Ivas();
  				aliquota = Agenti.find(id);
  			} else if (getAction().equalsIgnoreCase("delete")){
  				return delete();
  			} else {
  				aliquota = new Iva();
  			}
		} catch (Exception e) {
			// TODO: handle exception
			return ERROR;
		}
		return INPUT;
	}

	public Iva getIva() {
		return aliquota;
	}

	public void setIva(Iva aliquota) {
		this.aliquota = aliquota;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
