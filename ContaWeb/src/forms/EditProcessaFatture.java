package forms;

import java.util.Date;

import dao.Fatture;

import web.GenericAction;

public class EditProcessaFatture extends GenericAction {

   private static final long serialVersionUID = 1L;

   private Boolean processoCompletato = null;
   private Integer percAvanzamento = null;
   private Date dataLimite;
   private String messaggioErrore;	
   
   public Integer getPercAvanzamento() {
	return percAvanzamento;
   }

	public void setPercAvanzamento(Integer percAvanzamento) {
		this.percAvanzamento = percAvanzamento;
	}

	public Boolean getProcessoCompletato() {
		return processoCompletato;
	  }

	public void setProcessoCompletato(Boolean processoCompletato) {
		this.processoCompletato = processoCompletato;
	}
	
	/* METODO CHE PROCESSA TUTTE LE FATTURE PER UN PERIODO INDICATO */
	public String execute() {
		try {
			if (dataLimite == null)
				throw new Exception("Data non impostata");
			Fatture fatture = new Fatture();
			fatture.setDataLimite(dataLimite);
			Thread thread = new Thread(fatture);
			thread.run();
		} catch (Exception e) {
			setTextResponse(e.getMessage());
		}
		return SUCCESS;
	}
	
	public String statoProcesso() {
		Fatture fatture = new Fatture();

		percAvanzamento = fatture.getAvanzamentoProcesso();
		processoCompletato = !fatture.getProcessoAttivo();
		messaggioErrore = fatture.getMessaggioErrore();
		
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String input() {
  		return INPUT;
	 }

	public Date getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(Date dataLimite) {
		this.dataLimite = dataLimite;
	}

	public String getMessaggioErrore() {
		return messaggioErrore;
	}

	public void setMessaggioErrore(String messaggioErrore) {
		this.messaggioErrore = messaggioErrore;
	}
}