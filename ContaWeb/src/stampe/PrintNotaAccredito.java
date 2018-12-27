package stampe;

import dao.NoteAccredito;
import stampemgr.StampeMgr;
import vo.NotaAccredito;

public class PrintNotaAccredito extends PrintPDF {
	
	private static final long serialVersionUID = 5417096769427980677L;
	
	private Integer id = null;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public String execute() throws Exception {
		try {
			NotaAccredito ddt = new NotaAccredito();
			ddt.setId(id);
			ddt = (NotaAccredito) new NoteAccredito().findWithAllReferences(ddt);
			
			pdfFile = StampeMgr.getInstance().richiediPDFDocumento(ddt); 
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}

}
