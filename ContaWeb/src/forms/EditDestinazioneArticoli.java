package forms;

import vo.DestinazioneArticolo;
import dao.DestinazioniArticolo;

public class EditDestinazioneArticoli extends Edit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2343419772046019464L;
	
	private Integer id = null;
	private DestinazioneArticolo categoria = null;

	@Override
	protected String store() { 
		// TODO Auto-generated method stub
		try {
			DestinazioniArticolo categorie = new DestinazioniArticolo();
			categorie.store(categoria);
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
			//Collection clienti = new Articoli().get
			//if (clienti.isEmpty()){
				DestinazioneArticolo categoria = new DestinazioneArticolo();
				categoria.setId(id);
				new DestinazioniArticolo().delete(categoria);
			//} else {
			//	return ERROR_DELETE;
			//}
		} catch (Exception e) {
			stampaErrore("Edit Agente", e);
			// TODO: handle exception
			return ERROR_DELETE;
		}
		
		return SUCCESS;
	}
	
	public String input(){
		try {
			DestinazioniArticolo categorie = new DestinazioniArticolo();
  			if (getAction().equalsIgnoreCase("edit")) {
  				categoria = categorie.find(id);
  			} else if (getAction().equalsIgnoreCase("delete")){
  				return delete();
  			} else {
  				categoria = new DestinazioneArticolo();
  			}
		} catch (Exception e) {
			// TODO: handle exception
			return ERROR;
		}
		return INPUT;
	}

	public DestinazioneArticolo getCategoriaArticolo() {
		return categoria;
	}

	public void setCategoriaArticolo(DestinazioneArticolo categoria) {
		this.categoria = categoria;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
