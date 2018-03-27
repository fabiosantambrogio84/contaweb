package stampe;

import dao.OrdiniFornitori;
import stampemgr.StampeMgr;
import vo.OrdineFornitore;

public class PrintOrdineFornitore extends PrintPDF {
	
	private static final long serialVersionUID = 5117096769427980677L;
	private Integer id = null;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String execute() throws Exception {
		OrdineFornitore ordineFornitore = new OrdineFornitore();
		ordineFornitore.setId(id);
		ordineFornitore = (OrdineFornitore) new OrdiniFornitori().findWithAllReferences(ordineFornitore);
		
		pdfFile = StampeMgr.getInstance().richiediPDFDocumento(ordineFornitore);
		
		return SUCCESS;
		
	}
}
