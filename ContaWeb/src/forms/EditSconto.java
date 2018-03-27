package forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dao.Articoli;
import dao.Clienti;
import dao.Fornitori;
import dao.Sconti;

import vo.Articolo;
import vo.Sconto;

public class EditSconto extends Edit {

	private static final long serialVersionUID = 1L;

	private Integer id = null;
	private Collection listFornitori = null;
	private Collection listArticoli = null;
	private Collection listClienti = null;
	private Sconto sconto = null;

	public Sconto getSconto() {
		return sconto;
	}

	public void setSconto(Sconto sconto) {
		this.sconto = sconto;
	}

	@SuppressWarnings("unchecked")
	public Collection getListArticoli() {
		if (listArticoli == null) {
			try {
				// CREO ARTICOLO FITTIZIO
				/*Articolo articolo = new Articolo();
				articolo.setCodiceArticolo("");
				articolo.setId(-1);
				articolo.setDescrizione("TUTTI GLI ARTICOLI");*/

				listArticoli = new ArrayList();
				listArticoli.addAll(new Articoli().getElements());
			} catch (Exception e) {
				return null;
			}
		}
		return listArticoli;
	}

	public void setListArticoli(Collection listArticoli) {
		this.listArticoli = listArticoli;
	}

	public Collection getListFornitori() {
		if (listFornitori == null) {
			try {
				listFornitori = new Fornitori().getElements();
			} catch (Exception e) {
				return null;
			}
		}
		return listFornitori;
	}

	public void setListFornitori(Collection listFornitori) {
		this.listFornitori = listFornitori;
	}

	@SuppressWarnings("unchecked")
	public String input() {
		try {
			if (getAction().equalsIgnoreCase("delete")) {
				return delete();
			}

			if (getAction().equalsIgnoreCase("edit"))
				sconto = new Sconti().find(id);

		} catch (Exception e) {
			stampaErrore("EditSconto.input()", e);
			return ERROR;
		}
		return INPUT;
	}

	List<String> clienti;

	public void setClienti(List<String> clienti) {
		this.clienti = clienti;
	}

	List<String> fornitori;

	public void setFornitori(List<String> fornitori) {
		this.fornitori = fornitori;
	}

	List<String> articoli;

	public void setArticoli(List<String> articoli) {
		this.articoli = articoli;
	}

	protected String store() {
		try {
			Sconti sconti = new Sconti();

			// Imposta le date
			if (sconto.getDataScontoDal() != null)
				sconto.setDataScontoDal(new java.sql.Date(sconto
						.getDataScontoDal().getTime()));
			if (sconto.getDataScontoAl() != null)
				sconto.setDataScontoAl(new java.sql.Date(sconto
						.getDataScontoAl().getTime()));

			for (String cliente : clienti) {
				sconto.setIdCliente(Integer.valueOf(cliente));

				if (fornitori != null) {
					for (String fornitore : fornitori) {
						sconto.setIdFornitore(Integer.valueOf(fornitore));
						sconto.setArticolo(null);
						sconto.setIdArticolo(null);
						sconti.completeReferences(sconto);
						sconti.store(sconto);
					}
				} else if (articoli != null) {
					for (String articolo : articoli) {
						sconto.setIdArticolo(Integer.valueOf(articolo));
						//Imposta il fornitore						
						sconti.completeReferences(sconto);
						sconto.setIdFornitore(sconto.getArticolo().getFornitore().getId());
						sconto.setFornitore(sconto.getArticolo().getFornitore());
						sconti.store(sconto);
					}
				} else {
					throw new Exception("Bisogna selezionare o dei fornitori oppure degli articoli");
				}				
			}
		} catch (Exception e) {
			stampaErrore("EditSconto.store()", e);
			return ERROR;
		}
		return SUCCESS;
	}

	protected String delete() {
		try {
			Sconti sconti = new Sconti();
			Sconto sconto = new Sconto();
			sconto.setId(id);
			sconti.delete(sconto);
		} catch (Exception e) {
			stampaErrore("EditSconto.delete()", e);
			return ERROR_DELETE;
		}
		return SUCCESS;
	}

	public Collection getListClienti() {
		if (listClienti == null) {
			try {
				Clienti clienti = new Clienti();
				clienti.setOrderByDescrizione(Clienti.ORDER_ASC);	
				listClienti = clienti.getElements();
			} catch (Exception e) {
				return null;
			}
		}
		return listClienti;
	}

	public void setListClienti(Collection listClienti) {
		this.listClienti = listClienti;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}