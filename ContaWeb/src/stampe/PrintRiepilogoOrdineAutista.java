package stampe;

import stampemgr.StampeMgr;
import vo.Autista;

public class PrintRiepilogoOrdineAutista extends PrintPDF {
	
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
		Autista autista = new Autista();
		autista.setId(id);

		pdfFile = StampeMgr.getInstance().richiediPDFDocumentoData(autista, data);
		
		return SUCCESS;
		
	}
}
