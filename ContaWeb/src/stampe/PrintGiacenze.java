package stampe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import dao.Giacenze;
import stampemgr.GiacenzePrintHandler;
import vo.ReportGiacenza;

public class PrintGiacenze extends PrintPDF {
	
	private static final long serialVersionUID = 1195687587653512538L;
	
	private String idArticoli = null;
	
	public String getIdArticoli(){
		return idArticoli;
	}

	public void setIdArticoli(String idArticoli){
		this.idArticoli = idArticoli;
	}
	
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		Collection<ReportGiacenza> reportGiacenze = null;
		Giacenze giacenze = new Giacenze();
		
		List<String> idsList = Arrays.asList(idArticoli.split("-"));
		if(idsList != null && !idsList.isEmpty()){
			reportGiacenze = (Collection<ReportGiacenza>)giacenze.getGiacenzeByIdArticoli(idsList);
		}

		GiacenzePrintHandler handler = new GiacenzePrintHandler(new ArrayList<ReportGiacenza>(reportGiacenze));
		pdfFile = 	handler.creaPDF();
		
		return SUCCESS;		
	}

}
