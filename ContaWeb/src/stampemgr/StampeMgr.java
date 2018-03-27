package stampemgr;

// import java.util.List;

// import dao.RiepilogoFatture;
// import stampe.PrintFatture;
// import vo.Agente;
import vo.DDT;
import vo.Fattura;
import vo.Listino;
import vo.NotaAccredito;
import vo.Ordine;
import vo.OrdineFornitore;
import vo.VOElement;





public class StampeMgr {
	
	static private StampeMgr sm = null;
	
	
	static public StampeMgr getInstance() {
		if (sm == null) {
			sm = new StampeMgr();
		}
		return sm;
	}
	
	private DocumentPrintHandler getHandler(VOElement element) {
		if (element instanceof Fattura)
			return new FatturaPrintHandler(element);
		
		if (element instanceof NotaAccredito)
			return new NotaAccreditoPrintHandler(element);
		
		if (element instanceof OrdineFornitore)
			return new OrdineFornitorePrintHandler(element);
		
		if (element instanceof Listino)
			return new ListinoPrintHandler(element);
		
		if (element instanceof DDT)
			return new DDTPrintHandler(element);
		
		if (element instanceof Ordine)
			return new OrdinePrintHandler(element);
		
		return null;
	}

	public byte[] richiediPDFDocumento(VOElement element) throws Exception {
		DocumentPrintHandler doc = getHandler(element);
		
		/*if (doc.isPDF()) {
			return doc.leggiPDF();
		} else
			return doc.creaPDF();*/
		
		//IL PDF VIENE SEMPRE RIGENERATO, ANCHE QUANDO NE ESISTE UNA COPIA
		return doc.creaPDF();
	}
	
	public byte[] richiediPDFDocumentoList(VOElement element) throws Exception {
		DocumentPrintHandler doc = new FatturePrintHandler(element);
		
		/*if (doc.isPDF()) {
			return doc.leggiPDF();
		} else
			return doc.creaPDF();*/
		
		//IL PDF VIENE SEMPRE RIGENERATO, ANCHE QUANDO NE ESISTE UNA COPIA
		return doc.creaPDF();
	}
	
	public byte[] richiediPDFDDTList(VOElement element) throws Exception {
		DocumentPrintHandler doc = new DDTsPrintHandler(element);
		
		/*if (doc.isPDF()) {
			return doc.leggiPDF();
		} else
			return doc.creaPDF();*/
		
		//IL PDF VIENE SEMPRE RIGENERATO, ANCHE QUANDO NE ESISTE UNA COPIA
		return doc.creaPDF();
	}
	
	public byte[] richiediPDFDocumentoData(VOElement element, String data) throws Exception {
		// DocumentPrintHandler doc = new RiepilogoOrdineAgentePrintHandler(element, data);
		DocumentPrintHandler doc = new RiepilogoOrdineAutistaPrintHandler(element, data);
		
		/*if (doc.isPDF()) {
			return doc.leggiPDF();
		} else
			return doc.creaPDF();*/
		
		//IL PDF VIENE SEMPRE RIGENERATO, ANCHE QUANDO NE ESISTE UNA COPIA
		return doc.creaPDF();
	}
	
	public void cancellaPDFDocumento(VOElement element) {
		DocumentPrintHandler doc = getHandler(element);
		doc.deletePDF();
	}
}
