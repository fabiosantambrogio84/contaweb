package stampe;

import dao.Clienti;
import dao.Fatture;

import formatters.ConadParser;
import formatters.CommerciantiParser;
import formatters.ConadFactory;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import vo.Fattura;
import vo.DDT;
import vo.Cliente;

import web.GenericAction;

// CLAUDIO: modifiche CONAD
public class ExportCommercianti extends GenericAction {
	private Date daData = null;
	private Date alData = null;
	protected String textResponse = null;
	
	public String execute(){
		Fatture fat = new Fatture();
		if(daData != null
				&& alData != null)
		{
			fat.setFilterDataDa(daData);
			fat.setFilterDataA(alData);
			fat.setOrderByCliente();
			try {
				Collection fatture = fat.getElements();
				CommerciantiParser parser = new CommerciantiParser();
				textResponse = parser.format(fatture);
			} catch (Exception e) {
				// TODO: handle exception
				textResponse = "Messaggio di errore - "+e.getMessage();
				return "success";
			}
		}
		else
		{			
			textResponse = "Messaggio di errore - Devi impostare le date Da e A";
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

}
