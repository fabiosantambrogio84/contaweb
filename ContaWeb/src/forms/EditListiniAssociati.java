package forms;

import java.util.Collection;
import java.util.Vector;

import dao.Clienti;
import dao.DataAccessException;
import dao.Listini;

import vo.Cliente;

public class EditListiniAssociati extends Edit {

	private static final long serialVersionUID = 4628179447206708374L;

	private Cliente cliente = null;
	private Collection listListini = null;
	private Collection listListiniAssociati = null;
	private Vector listiniSelezionati = new Vector();

	public Collection getListListiniAssociati() {
		return listListiniAssociati;
	}

	public void setListListiniAssociati(Collection listListiniAssociati) {
		this.listListiniAssociati = listListiniAssociati;
	}

	public String input() {
		try {
			listListini = new Listini().getElements();
			listListiniAssociati = new Listini().getListiniAssociatiByCliente(cliente);
			return INPUT;
		} catch (DataAccessException e) {
			stampaErrore("EditListiniAssociati.input()",e);
			return ERROR;
		}
	}
	
	@Override
	protected String delete() {
		return ERROR_DELETE;
	}

	@Override
	protected String store() {
		Clienti clienti = new Clienti();
		try {
			clienti.salvaListiniAssociati(cliente.getId(), listiniSelezionati);
		} catch (DataAccessException e) {
			stampaErrore("EditListiniAssociati.store()",e);
			return ERROR;
		}
		return SUCCESS;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Collection getListListini() {
		return listListini;
	}

	public void setListListini(Collection listListini) {
		this.listListini = listListini;
	}

	public Vector getListiniSelezionati() {
		return listiniSelezionati;
	}

	public void setListiniSelezionati(Vector listiniSelezionati) {
		this.listiniSelezionati = listiniSelezionati;
	}
}