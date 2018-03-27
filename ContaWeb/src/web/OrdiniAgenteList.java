package web;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import vo.DettaglioOrdine;
import vo.Ordine;
import vo.Agente;
import dao.DataAccessException;
import dao.Ordini;
import dao.Agenti;
import dao.Telefonate;

public class OrdiniAgenteList extends GenericAction {

	private static final long serialVersionUID = 1L;
	private Collection<Collection> agenti;
	private Collection<Agente> listAgent;

	public Collection<Collection> getAgenti() {
		return agenti;
	}

	public void setAgenti(Collection<Collection> agenti) {
		this.agenti = agenti;
	}

	public Collection<Agente> getListAgenti() {
		return listAgent;
	}

	public void setListAgenti(Collection<Agente> listAgent) {
		this.listAgent = listAgent;
	}

	public String execute() throws Exception {

		agenti = new Vector<Collection>();
		agenti.add(new Agenti().listAgenti());
		return SUCCESS;
	}
}