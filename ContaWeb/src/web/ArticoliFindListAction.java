package web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import applet.db.DbConnector;
import dao.Articoli;
import dao.Clienti;
import dao.DDTs;
import vo.Articolo;
import vo.Cliente;
import vo.DDT;

public class ArticoliFindListAction  extends GenericAction {
	
	private Collection<Articolo> articoli;
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {  
		Articoli articoliDao = new Articoli();		
		articoli = articoliDao.getActiveElements();
        
		return SUCCESS;
    }

	public Collection<Articolo> getArticoli() {
		return articoli;
	}
}
