package web;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import vo.DettaglioOrdine;
import vo.Ordine;
import vo.Autista;
import dao.DataAccessException;
import dao.Ordini;
import dao.Autisti;
import dao.Telefonate;

public class OrdiniAutistaList extends GenericAction {

	private static final long serialVersionUID = 1L;
	private Collection<Collection> autisti;
	private Collection<Autista> listAutisti;

	public Collection<Collection> getAutisti() {
		return autisti;
	}

	public void setAutisti(Collection<Collection> autisti) {
		this.autisti = autisti;
	}

	public Collection<Autista> getListAutisti() {
		return listAutisti;
	}

	public void setListAutisti(Collection<Autista> listAutisti) {
		this.listAutisti = listAutisti;
	}

	public String execute() throws Exception {

		autisti = new Vector<Collection>();
		autisti.add(new Autisti().listAutisti());
		return SUCCESS;
	}
}