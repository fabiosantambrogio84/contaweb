package web;

import java.util.Collection;
import java.util.Vector;

import vo.Articolo;
import dao.DataAccessException;
import dao.Articoli;

public class ArticoliFornitoriList extends GenericAction{

	private static final long serialVersionUID = 1L;
	private Collection<Articolo> list;
	private Collection<Collection> articoli;
	private Integer fornitore;

	public Collection<Collection> getArticoli() {
		return articoli;
	}

	public void setArticoli(Collection<Collection> articoli) {
		this.articoli = articoli;
	}

	public Collection<Articolo> getList() {
		return list;
	}

	public void setList(Collection<Articolo> list) {
		this.list = list;
	}
	
	public String execute() throws Exception {
		
		articoli = new Vector<Collection>();
		
		articoli.add(new Articoli().getListaArticoliFornitore(fornitore));
		
		return SUCCESS;
    }
	
}
