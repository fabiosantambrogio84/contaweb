package forms;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import vo.Articolo;
import vo.Cliente;
import vo.NotaAccredito;
import vo.PuntoConsegna;
import dao.Articoli;
import dao.Clienti;
import dao.DataAccessException;
import dao.Ivas;
import dao.Listini;
import dao.NoteAccredito;
import dao.Settings;

public class EditNotaAccredito extends Edit {
	private static final long serialVersionUID = 1L;
	
	private Integer id = null;
	private Integer idCliente = null;
	private Integer idOrdineCliente = null;
	private String codiceArticolo = null;
	private String barcode = null;
	private Date data = null;
	private NotaAccredito ddt = null;
	private String appletUrl = null;

	public String getAppletUrl() {
		return appletUrl;
	}

	public void setAppletUrl(String appletUrl) {
		this.appletUrl = appletUrl;
	}

	public NotaAccredito getDdt() {
		return ddt;
	}

	public void setNotaAccredito(NotaAccredito ddt) {
		this.ddt = ddt;
	}

	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String input() {
  		try {
  			appletUrl = Settings.getInstance().getValue("applet.url");
			if (getAction().equalsIgnoreCase("delete")) {
				return delete();
			}			
		} catch (Exception e) {
			stampaErrore("EditDDT.input()",e);
			return ERROR;
		}

  		return INPUT;
	 }
	 
	public String store() {
		try {
			NoteAccredito ddts = new NoteAccredito();
			ddts.store(ddt);
			//SERIALIZZO L'ID PER L'APPLET
			setSerializedObject(ddt.getId());
		} catch (Exception e) {
			stampaErrore("EditNoteAccredito.store()",e);
			e.printStackTrace();
			setSerializedObject(new Exception("DB: Errore nel salvataggio della nota di accredito."));
		}
		 return SUCCESS;
	}
	
	public String pagato() {
		try {
			NoteAccredito ddts = new NoteAccredito();
			ddt = new NotaAccredito();
			ddt.setId(id);
			ddt = (NotaAccredito)ddts.find(ddt);
			ddt.setPagato(true);
			ddts.store(ddt);
			//SERIALIZZO L'ID PER L'APPLET
			setSerializedObject(ddt.getId());
		} catch (Exception e) {
			stampaErrore("EditNoteAccredito.store()",e);
			e.printStackTrace();
			setSerializedObject(new Exception("DB: Errore nel salvataggio della nota di accredito."));
		}
		 return SUCCESS;
	}
	 
	protected String delete() {
		 try {
			NotaAccredito ddt = new NotaAccredito();
			ddt.setId(id);
			new NoteAccredito().delete(ddt);
		} catch (Exception e) {
			stampaErrore("EditNotaAccredito.delete()",e);
			return ERROR_DELETE;
		}
	 	 return SUCCESS;
	}
	
	
	
	/* ----------------- AZIONI RICHIESTE DALLA APPLET -------------------- */
	@SuppressWarnings("unchecked")
	public String listaClienti() {
		try {
			Collection clienti = new Clienti().getListaPerDDT();
			setSerializedObject(clienti);
		} catch (DataAccessException e) {
			return ERROR;
		}
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String listaIva() {
		try {
			Collection ivas = new Ivas().getElements();
			setSerializedObject(ivas);
		} catch (DataAccessException e) {
			return ERROR;
		}
		return SUCCESS;
	}	
	
	public String getCliente() {
		try {
			Cliente cliente = new Cliente();
			cliente.setId(idCliente);
			cliente = (Cliente) new Clienti().findWithAllReferences(cliente);
			
			//Cancella dal cliente tutti i punti di consegna non attivi
			Iterator<PuntoConsegna> itr = cliente.getPuntiConsegna().iterator();
			while (itr.hasNext()) {
				if (!itr.next().getAttivato())
					itr.remove();
			}
			
			setSerializedObject(cliente);
		} catch (DataAccessException e) {
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String getNotaAccredito() {
		try {
			NoteAccredito ddts = new NoteAccredito();
			NotaAccredito ddt = new NotaAccredito();
			ddt.setId(id);
			ddt = (NotaAccredito) ddts.findWithAllReferences(ddt);
			new Clienti().completeReferences(ddt.getCliente());					
			setSerializedObject(ddt);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String getPrezzoArticolo() {
		try {
			Articolo articolo = new Articolo();
			articolo.setCodiceArticolo(codiceArticolo);
			articolo = (Articolo) new Articoli().findWithAllReferences(articolo);			
			Cliente cliente = new Cliente();
			cliente.setId(idCliente);
			cliente = (Cliente) new Clienti().findWithAllReferences(cliente);			
			setSerializedObject(new Listini().getPrezzoConSconto(articolo, cliente, data));
		} catch (DataAccessException e) {
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String getPrezzoArticoloDaBC() {
		try {
			Cliente cliente = new Cliente();
			cliente.setId(idCliente);
			cliente = (Cliente) new Clienti().findWithAllReferences(cliente);
			Articolo articolo = new Articoli().findByBarcode(barcode);
			new Articoli().completeReferences(articolo);
			if (articolo == null) //ARTICOLO NON TROVATO
				throw new Exception("Articolo non trovato");
			setSerializedObject(new Listini().getPrezzoConSconto(articolo, cliente, data));
		} catch (Exception e) {
			setSerializedObject(e);
		}
		return SUCCESS;
	}
	/* --------------- FINE AZIONI RICHIESTE DALLA APPLET ----------------- */

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Integer getIdOrdineCliente() {
		return idOrdineCliente;
	}

	public void setIdOrdineCliente(Integer idOrdineCliente) {
		this.idOrdineCliente = idOrdineCliente;
	}
}