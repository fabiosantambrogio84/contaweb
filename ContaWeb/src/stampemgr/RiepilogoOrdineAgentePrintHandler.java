package stampemgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

import org.apache.struts2.ServletActionContext;

import vo.Agente;
import vo.DDT;
import vo.Ordine;
import vo.DettaglioOrdine;
import vo.VOElement;
import dao.DataAccessException;
import dao.Settings;
import dao.Ordini;
import dao.Agenti;

public class RiepilogoOrdineAgentePrintHandler extends DocumentPrintHandler {

	private Agente agente = null;
	private String date = null;
	private Ordine ordine = null;
	
	public RiepilogoOrdineAgentePrintHandler(VOElement element, String data) {
		agente = (Agente) element;
		date = data;
	}

	@SuppressWarnings("unchecked")
	private Map preparaParametriStampa() throws DataAccessException {
		
		String current = System.getProperty("user.dir");
		
		Map params = new HashMap();
		agente = new Agenti().find(agente.getId());
		params.put("agenteNome",agente.getNome());
		params.put("data", date);
		params.put("SUBREPORT_DIR", ServletActionContext.getServletContext().getRealPath("/stampe") + "/");
		
		try {
			params.put("image", ServletActionContext.getServletContext().getResource("/img/star.jpg"));
		} catch (MalformedURLException e) {}
		
		return params;
	}

	@SuppressWarnings("unchecked")
	private JRDataSource preparaDettagliStampa() throws DataAccessException {
		JRBeanArrayDataSource dataSource;
		Vector dettagli = new Vector();

		// ToDo da verificare / Sistemare
		/* 
		Iterator itr = new Ordini().getListaOrdiniByAgentDate(agente.getId(), date).iterator();

		while(itr.hasNext())
		{
			ordine = (Ordine)itr.next();
			Vector DNC = new Vector();
			DNC = ordine.getDettagliOrdine();
			Vector<DettaglioOrdine> dettOrd = new Vector<DettaglioOrdine>();
			dettOrd.add((DettaglioOrdine)DNC.get(0));
			ordine.setDettaglioOrdine(dettOrd);
			
			dettagli.add(ordine);
				
		}
		 */
		
		dataSource = new JRBeanArrayDataSource(dettagli.toArray());
		return dataSource;
	}
	
	public byte[] creaPDF() throws Exception {
		File file = null;
		FileOutputStream fos = null;
		try {
			InputStream jspTemplate = ServletActionContext.getServletContext().getResourceAsStream("/stampe/RiepilogoOrdineAgente.jasper");
			
			JRDataSource ds = preparaDettagliStampa();
			Map params = preparaParametriStampa();
						
			byte[] filePDF = JasperRunManager.runReportToPdf(jspTemplate, params,ds);
			String percorsoFile = buildPDFPath(true);
			
			//SALVO IL PDF
			file = new File(percorsoFile);
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(filePDF);
			fos.flush(); 
			fos.close();
			
			return filePDF;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (fos != null)
				fos.close();
		}
	}
	
	public String buildPDFPath(boolean mkdirs) {
		File file = null;
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		String ps = System.getProperty("file.separator");
		String percorsoAnno = Settings.getInstance().getValue("print.pdfRepository") + "" +
				"OrdiniAgenti"; 
		file = new File(percorsoAnno);
		if (!file.isDirectory()) {
			file.mkdir();
		}
		
		percorsoAnno += ps + cal.get(Calendar.YEAR);
		file = new File(percorsoAnno);
		if (!file.isDirectory()) {
			file.mkdir();
		}
		
		String percorsoMese = percorsoAnno + ps + (cal.get(Calendar.MONTH) + 1);
		file = new File(percorsoMese);
		if (!file.isDirectory()) {
			file.mkdir();
		}
		
		String percorsoFile = percorsoMese + ps + "Ordine_riepilogo_agente" + agente.getId() + ".pdf"; 		
		return percorsoFile;
	}

	public boolean isPDF() {
		return buildPDFPath(false) != null;
	}

	public byte[] leggiPDF() throws Exception {
		String path = buildPDFPath(false);
		File file = new File(path);
		FileInputStream rf = new FileInputStream(file);
		
		byte[] array = new byte[(int) file.length()];
		rf.read(array);
		return array;
	}

	@Override
	public void deletePDF() {
		if (isPDF()) {
			String path = buildPDFPath(false);
			File file = new File(path);
			file.delete();
		}
	}
}
