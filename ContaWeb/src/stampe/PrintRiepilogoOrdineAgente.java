package stampe;

// import javax.servlet.http.HttpServletRequest;

import stampemgr.StampeMgr;
// import vo.Agente;
// import dao.Agenti;

import vo.Autista;


public class PrintRiepilogoOrdineAgente extends PrintPDF {
	
	private static final long serialVersionUID = 5117096769427980677L;
	private Integer id = null;
	private String data = null;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
   
	public String execute() throws Exception {
		/* ***
			Agente agente = new Agente();
			agente.setId(id);
			agente.setData(data);
		*** */

		Autista autista = new Autista();
		autista.setId(id);

        // agente = (Agente) new Agenti().findWithAllReferences(agente);
		
		// pdfFile = StampeMgr.getInstance().richiediPDFDocumentoData(agente, data);
		pdfFile = StampeMgr.getInstance().richiediPDFDocumentoData(autista, data);
		
		return SUCCESS;
		
	}
}
