package web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import applet.db.DbConnector;
import dao.Clienti;
import dao.DDTs;
import vo.Cliente;
import vo.DDT;

public class ClientiFindListAction  extends GenericAction {
	
	private Collection<Cliente> clienti;
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {  
		Clienti clientiDao = new Clienti();		
		clienti = clientiDao.getListaPerDDT();
        
		return SUCCESS;
    }

	public Collection<Cliente> getClienti() {
		return clienti;
	}
}
