package stampe;

import stampemgr.ListinoPrintHandler;
import vo.Listino;
import dao.Listini;

public class PrintListino extends PrintPDF {
	
	private static final long serialVersionUID = 5117096769427980677L;
	private Integer id = null;
	private Integer idCategoria = null;
	private Integer idFornitore = null;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String execute() throws Exception {
		Listino listino = new Listino();
		listino.setId(id);
		listino = (Listino) new Listini().findWithAllReferences(listino);
		
		ListinoPrintHandler handler = new ListinoPrintHandler(listino);
		handler.setIdCategoria(idCategoria);
		handler.setIdFornitore(idFornitore);
		pdfFile = 	handler.creaPDF();
		
		return SUCCESS;
		
	}

	public Integer getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

	public Integer getIdFornitore() {
		return idFornitore;
	}

	public void setIdFornitore(Integer idFornitore) {
		this.idFornitore = idFornitore;
	}
}
