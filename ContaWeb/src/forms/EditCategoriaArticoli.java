package forms;

import java.util.Collection;

import vo.Agente;
import vo.CategoriaArticolo;
import dao.Agenti;
import dao.Articoli;
import dao.CategorieArticolo;
import dao.Clienti;

public class EditCategoriaArticoli extends Edit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2343419772046019464L;
	
	private Integer id = null;
	private CategoriaArticolo categoria = null;

	@Override
	protected String store() {
		// TODO Auto-generated method stub
		try {
			CategorieArticolo categorie = new CategorieArticolo();
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
				CategoriaArticolo categoria = new CategoriaArticolo();
				categoria.setId(id);
				new CategorieArticolo().delete(categoria);
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
			CategorieArticolo categorie = new CategorieArticolo();
  			if (getAction().equalsIgnoreCase("edit")) {
  				categoria = categorie.find(id);
  			} else if (getAction().equalsIgnoreCase("delete")){
  				return delete();
  			} else {
  				categoria = new CategoriaArticolo();
  			}
		} catch (Exception e) {
			// TODO: handle exception
			return ERROR;
		}
		return INPUT;
	}

	public CategoriaArticolo getCategoriaArticolo() {
		return categoria;
	}

	public void setCategoriaArticolo(CategoriaArticolo categoria) {
		this.categoria = categoria;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
