package forms;

import java.util.*;

import dao.Clienti;
import dao.PuntiConsegna;

import vo.Cliente;
import vo.PuntoConsegna;

public class EditPC extends Edit {

	private static final long serialVersionUID = 1L;
	
	private Collection datiTabella = null;
	private Cliente cliente = null;
	private PuntoConsegna puntoConsegna = null;
	private Boolean attiva;
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getAttiva() {
		return attiva;
	}

	public void setAttiva(Boolean attiva) {
		this.attiva = attiva;
	}

	public PuntoConsegna getPuntoConsegna() {
		return puntoConsegna;
	}

	public void setPuntoConsegna(PuntoConsegna puntoConsegna) {
		this.puntoConsegna = puntoConsegna;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Collection getDatiTabella() {
		return datiTabella;
	}

	public void setDatiTabella(Collection datiTabella) {
		this.datiTabella = datiTabella ;
	}
	
	public String input() {
		try {
			
			if (getAction()!= null && getAction().equalsIgnoreCase("deletePuntoConsegna")) //CANCELLO PUNTO CONSEGNA
				delete();
			
			if (getAction()!= null && getAction().equalsIgnoreCase("storePuntoConsegna")) //MODIFICO O INSERISCO PUNTO CONSEGNA
				store();
			
			datiTabella = new PuntiConsegna(cliente.getId()).getElements();
		} catch (Exception e) {
			stampaErrore("EditPuntiConsegna.input()",e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		datiTabella = new PuntiConsegna(cliente.getId()).getElements();

		return SUCCESS;
    }

	protected String delete() {
		try {
			puntoConsegna = new PuntoConsegna();
			puntoConsegna.setId(id);
			new PuntiConsegna(cliente.getId()).delete(puntoConsegna);
		} catch (Exception e) {
			stampaErrore("EditPuntiConsegna.delete()",e);
			return ERROR;
		}
		return SUCCESS;
	}

	protected String store() {
		try {
			puntoConsegna.setIdCliente(cliente.getId());
			puntoConsegna.setCliente(new Clienti().find(cliente.getId()));
			if (puntoConsegna.getAttivato() == null)
				puntoConsegna.setAttivato(true);
			new PuntiConsegna(cliente.getId()).store(puntoConsegna);
			puntoConsegna = new PuntoConsegna();
		} catch (Exception e) {
			stampaErrore("EditPuntiConsegna.store()",e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String attiva() throws Exception {
		if (id != null) {
			PuntiConsegna pcs = new PuntiConsegna(); 
			PuntoConsegna pc = pcs.find(id);
			if (pc != null) {
				pc.setAttivato(!pc.getAttivato());
				pcs.store(pc);
				return SUCCESS;
			}	
		}
		return ERROR;		
	}
}
