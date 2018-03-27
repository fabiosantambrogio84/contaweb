package stampe;

import java.util.Collection;
import java.util.Date;

import dao.Clienti;
import dao.Fatture;
import formatters.ConadFactory;
import formatters.ConadParser;
import vo.Cliente;
import web.GenericAction;

// CLAUDIO: modifiche CONAD
public class ExportConad extends GenericAction {
	private Date daData = null;
	private Date alData = null;
	private Integer idCliente = null;
	protected String textResponse = null;
	
	public String execute(){
		Fatture fat = new Fatture();
		try {
			Collection fatture = fat.getPerDataCliente(daData, alData, idCliente);
			Clienti clienti = new Clienti();
			Cliente cliente = clienti.find(idCliente);
			ConadParser parser = ConadFactory.getNewConadParser(cliente.getFormatoConad());
			textResponse = parser.format(fatture);
		} catch (Exception e) {
			// TODO: handle exception
			textResponse = "Messaggio di errore - "+e.getMessage();
			return "success";
		}
		
		return "success";
	}

	public Date getDaData() {
		return daData;
	}

	public void setDaData(Date daData) {
		this.daData = daData;
	}

	public Date getAlData() {
		return alData;
	}

	public void setAlData(Date alData) {
		this.alData = alData;
	}
	
	
	public String getTextResponse() {
		return textResponse;
	}
	
	public void setTextResponse(String textResponse){
		this.textResponse = textResponse;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}


}
