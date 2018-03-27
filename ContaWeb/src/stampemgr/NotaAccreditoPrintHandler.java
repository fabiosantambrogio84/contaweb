package stampemgr;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

import org.apache.struts2.ServletActionContext;

import vo.NotaAccredito;
import vo.VOElement;

public class NotaAccreditoPrintHandler extends DocumentPrintHandler {

	NotaAccredito  ddt;
	
	public NotaAccreditoPrintHandler(VOElement element) {
		ddt = (NotaAccredito) element;
	}

	@Override
	public byte[] creaPDF() throws Exception {
		try {
			String resource;
			
			resource = "/stampe/NotaAccredito.jasper";
			InputStream jspTemplate = ServletActionContext.getServletContext().getResourceAsStream(resource);
		
			JRDataSource ds = new JRBeanArrayDataSource(ddt.getDettagliAccredito().toArray());
			Map params = preparaParametri(ddt);

			params.put("REPORT_LOCALE", Locale.ITALIAN);
			byte[] filePDF = JasperRunManager.runReportToPdf(jspTemplate, params,ds);
			
			return filePDF;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Map preparaParametri(NotaAccredito ddt) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yy");
		
		Map params = new HashMap();
		
		try {
			params.put("image", ServletActionContext.getServletContext().getResource("/img/star.jpg"));
		} catch (MalformedURLException e) {
		}
		
		params.put("rs", ddt.getCliente().getRs());
		params.put("rs2", ddt.getCliente().getRs2());
		params.put("indirizzo", ddt.getCliente().getIndirizzo());
		params.put("prov", ddt.getCliente().getProv());
		params.put("cap", ddt.getCliente().getCap());
		params.put("loc", ddt.getCliente().getLocalita());
		
		params.put("dnome", ddt.getPuntoConsegna().getNome());
		params.put("dindirizzo", ddt.getPuntoConsegna().getIndirizzo());
		params.put("dcap", ddt.getPuntoConsegna().getCap());
		params.put("dloc", ddt.getPuntoConsegna().getLocalita());
		params.put("dprov", ddt.getPuntoConsegna().getProv());
		
		String agente = "";
		if(ddt.getCliente().getAgente() != null)
			agente = ddt.getCliente().getAgente().getCognome();
		params.put("agente", agente);
		
		params.put("nprog", ddt.getNumeroProgressivoCompleto() + "/" + sdf.format(ddt.getData()));
		params.put("data", ddt.getData());
		params.put("piva", ddt.getCliente().getPiva());
		params.put("codiceFiscale", ddt.getCliente().getCodiceFiscale());
		params.put("causale", ddt.getCausale());
		
		params.put("trasporto", ddt.getTrasporto());
		params.put("dataTrasporto", ddt.getDataTrasporto());
		params.put("oraTrasporto", ddt.getOraTrasporto());
		params.put("totaleDDT", ddt.calcolaTotale());
		params.put("colli", ddt.getColli());
		
		if(ddt.getCliente() != null && ddt.getCliente().getPagamento() != null)
			params.put("pagamento", ddt.getCliente().getPagamento().getDescrizione());
		
		//CALCOLO ARRAY DEGLI IMPONIBILI
		ddt.calcolaTotale();
		BigDecimal[][] imponibili = ddt.getImponibili();
		
		int i = 0;
		boolean trovato = false;
		while(i<100 && !trovato) {
			if (imponibili[i][0] != null)
				trovato = true;
			else
				++i;
		}
		
		if (trovato) { //RIGA 1
			params.put("iva1", i);
			params.put("imponibile1", imponibili[i][0]);
			params.put("imposta1", imponibili[i][1]);
		}
		
		i++;
		trovato = false;
		while(i<100 && !trovato) {
			if (imponibili[i][0] != null)
				trovato = true;
			else
				++i;
		}
		
		if (trovato) { //RIGA 2
			params.put("iva2", i);
			params.put("imponibile2", imponibili[i][0]);
			params.put("imposta2", imponibili[i][1]);
		}
		
		i++;
		trovato = false;
		while(i<100 && !trovato) {
			if (imponibili[i][0] != null)
				trovato = true;
			else
				++i;
		}
		
		if (trovato) { //RIGA 3
			params.put("iva3", i);
			params.put("imponibile3", imponibili[i][0]);
			params.put("imposta3", imponibili[i][1]);
		}
		
		params.put("totaleFattura", ddt.getTotale());		
		params.put("totaleImponibile", ddt.getTotaleImponibile());
		params.put("totaleImposta", ddt.getTotaleImposta());
	
		
		return params;
	}

	@Override
	public void deletePDF() {}

	@Override
	public boolean isPDF() {
		return false;
	}

	@Override
	public byte[] leggiPDF() throws Exception {
		return null;
	}
}
