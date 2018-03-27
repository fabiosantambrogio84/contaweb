package stampemgr;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

import org.apache.struts2.ServletActionContext;

import vo.DDT;
import vo.VOElement;

public class DDTPrintHandler extends DocumentPrintHandler {

	DDT ddt;
	
	public DDTPrintHandler(VOElement element) {
		ddt = (DDT) element;
	}

	@Override
	public byte[] creaPDF() throws Exception {
		try {
			String resource;
			if (ddt.getCliente().getNascondiPrezzi()) 
			    resource = "/stampe/DDT-no-prezzi.jasper";
			else
				resource = "/stampe/DDT-prezzi.jasper";
			InputStream jspTemplate = ServletActionContext.getServletContext().getResourceAsStream(resource);
		
			JRDataSource ds = new JRBeanArrayDataSource(ddt.getDettagliDDT().toArray());
			Map params = preparaParametri(ddt);

			params.put("REPORT_LOCALE", Locale.ITALIAN);
			byte[] filePDF = JasperRunManager.runReportToPdf(jspTemplate, params, ds);
			
			return filePDF;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Map preparaParametri(DDT ddt) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yy");
		
		Map params = new HashMap();
		
		try {
			params.put("image", ServletActionContext.getServletContext().getResource("/img/star.jpg"));
		} catch (MalformedURLException e) {
			return null;
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
