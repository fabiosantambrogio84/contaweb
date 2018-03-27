package web;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import dao.Autisti;
import dao.Clienti;
import dao.DataAccessException;
import dao.Telefonate;
import vo.Autista;
import vo.Telefonata;

public class TelefonateList extends GenericAction {

	private static final long serialVersionUID = 1L;
	private Collection<Telefonata> list;
	private Collection<Collection> telefonate;
	private String typeList;
	private int idTelefonata;
	private Integer filterCliente = null;
	private Boolean eseguita;
	private Collection listClienti = null;
	private Collection<Autista> listAutori;
	
	public int getIdTelefonata() {
		return idTelefonata;
	}

	public void setIdTelefonata(int idTelefonata) {
		this.idTelefonata = idTelefonata;
	}

	public Boolean getEseguita() {
		return eseguita;
	}

	public void setEseguita(Boolean eseguita) {
		this.eseguita = eseguita;
	}

	public String getTypeList() {
		return typeList;
	}

	public void setTypeList(String typeList) {
		this.typeList = typeList;
	}

	public Collection<Collection> getTelefonate() {
		return telefonate;
	}

	public void setTelefonate(Collection<Collection> telefonate) {
		this.telefonate = telefonate;
	}

	public Collection<Telefonata> getList() {
		return list;
	}

	public void setList(Collection<Telefonata> list) {
		this.list = list;
	}

	public String telefonateOggi() throws DataAccessException {
		
		Date date = new Date();
		new Telefonate().UndoTelefonate(date);
		Collection daChiamare = null;
		if(getFilterCliente() != null) daChiamare = new Telefonate().getListaDaChiamare(date, getFilterCliente());
		else daChiamare = new Telefonate().getListaDaChiamare(date);
		
		
		list = daChiamare;
		typeList = "giorno";
		return SUCCESS;
	}
	
	public String eseguita() throws DataAccessException
	{
		Telefonata telefonata = new Telefonata();
		telefonata.setId(idTelefonata);
		telefonata = (Telefonata) new Telefonate().findWithAllReferences(telefonata);
		telefonata.setEseguita(eseguita);
		new Telefonate().store(telefonata);
		
		return SUCCESS;
	}
	
	public Collection getListClienti() {
		if (listClienti == null) {
			try {
				Clienti dao = new Clienti();
				dao.setOrderByDescrizione(Clienti.ORDER_ASC);				
				listClienti = dao.getElements();
				// .*. listClienti = new Clienti().getElements();
			} catch (Exception e) {
   				stampaMessaggio("EditOrdine.getListClienti()", e);
				return null;
			}
		}
		return listClienti;
	}

	public void setListClienti(Collection listClienti) {
		this.listClienti = listClienti;
	}

	@SuppressWarnings("unchecked")
    public Collection<Autista> getListAutisti() {
		if(listAutori == null)
	  	{
			try {
				listAutori = new Autisti().getElements();
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  	}
		return listAutori;
  }
	
	public String execute() throws Exception {
		telefonate = new Vector<Collection>();
		Calendar cal = Calendar.getInstance();
		typeList = "settimana";
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		for (int i = 0; i < 6; ++i) {
			if(getFilterCliente() != null) telefonate.add(new Telefonate().getListaDaChiamare(cal.getTime(), getFilterCliente()));
			else telefonate.add(new Telefonate().getListaDaChiamare(cal.getTime()));
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}

		return SUCCESS;
	}

	public Integer getFilterCliente() {
		return filterCliente;
	}

	public void setFilterCliente(Integer filterCliente) {
		this.filterCliente = filterCliente;
	}
}
