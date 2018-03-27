package stampe;

import stampemgr.StampeMgr;
import vo.Ordine;
import dao.Ordini;

public class PrintOrdineAgente extends PrintPDF {
	
	private static final long serialVersionUID = 5117096769427980677L;
	private Integer id = null;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String execute() throws Exception {
		Ordine ordine = new Ordine();
		ordine.setId(id);
		ordine = (Ordine) new Ordini().findWithAllReferences(ordine);
		
		pdfFile = StampeMgr.getInstance().richiediPDFDocumento(ordine);
		
		return SUCCESS;
		
	}
}
