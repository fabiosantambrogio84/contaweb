package stampemgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import org.apache.struts2.ServletActionContext;

import dao.Clienti;
import dao.DDTs;
import dao.DataAccessException;
import dao.Fatture;
import dao.Settings;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import vo.Cliente;
import vo.DDT;
import vo.DettaglioDDT;
import vo.Fattura;
import vo.VOElement;

public class FatturaPrintHandler extends DocumentPrintHandler {

	private Fattura fattura;
	
	public FatturaPrintHandler(VOElement element) {
		fattura = (Fattura) element;
	}

	@SuppressWarnings("unchecked")
	private Map preparaParametriStampaFattura(Fattura fattura) throws DataAccessException {
		Map params = new HashMap();
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yy");
		
		params.put("crs", fattura.getCliente().getRs());
		params.put("crs2", fattura.getCliente().getRs2());
		params.put("indirizzo", fattura.getCliente().getIndirizzo());
		params.put("cap", fattura.getCliente().getCap());
		params.put("localita", fattura.getCliente().getLocalita());
		params.put("prov", fattura.getCliente().getProv());
		params.put("pagamento", fattura.getCliente().getPagamento().getDescrizione());
		params.put("numeroFattura", fattura.getNumeroProgressivo() + "/" + sdf.format(fattura.getData()));
		//params.put("numeroFattura", fattura.getNumeroProgressivo());
		params.put("dataFattura", fattura.getData());
		params.put("piva", fattura.getCliente().getPiva());
		params.put("note", fattura.getNoteFattura());
		String agente = "";
		if(fattura.getCliente().getAgente() != null)
			agente = fattura.getCliente().getAgente().getCognome();
		params.put("agente", agente);
		
		params.put("sconto", fattura.getSconto());
		try {
			params.put("image", ServletActionContext.getServletContext().getResource("/img/star.jpg"));
		} catch (MalformedURLException e) {
		}
		
		//CALCOLO ARRAY DEGLI IMPONIBILI
		fattura.calcolaTotali();
		HashMap<BigDecimal, BigDecimal[]> imponibili = fattura.getImponibili();
		
		int index  = 1;
		for(BigDecimal key : imponibili.keySet())
		{
			BigDecimal[] imponibile = imponibili.get(key);
			if(imponibile == null) continue;
			if(imponibile[0] == null
				||imponibile[1] == null
				||imponibile[0].doubleValue() <= 0
				||imponibile[1].doubleValue() <= 0) continue;
			

			params.put("iva" + index, key);
			params.put("imponibile" + index, imponibile[0]);
			params.put("imposta" + index, imponibile[1]);
			
			if(index == 3) break;
			index++;					
		}
				
		params.put("totaleFattura", fattura.getTotaleFattura());
		params.put("totaleMerce", fattura.getTotaleMerce());
		params.put("totaleImponibile", fattura.getTotaleImponibile());
		params.put("totaleImposta", fattura.getTotaleImposta());
		
		return params;
	}

	@SuppressWarnings("unchecked")
	private JRDataSource preparaDettagliStampaFattura(Fattura fattura) throws DataAccessException {
		JRBeanArrayDataSource dataSource;
		
		Vector dettagli = new Vector();
		Iterator itr = fattura.getDettagliFattura().iterator();
		DDTs ddts = new DDTs();
		while (itr.hasNext()) {
			DDT ddt = (DDT) itr.next();
			ddts.completeReferences(ddt);
			//CREO LA RIGA DETTAGLIO DDT FASULLA
			DettaglioDDT dt = new DettaglioDDT();
			dt.setId(-1);
			dt.setDdt(ddt);
			dettagli.add(dt);
			Iterator itr2 = ddt.getDettagliDDT().iterator();
			//SALVO LE RIGHE NELL'ARRAY
			while (itr2.hasNext()) {
				dettagli.add(itr2.next());
			}
		}
	
		dataSource = new JRBeanArrayDataSource(dettagli.toArray());

		return dataSource;
	}
	
	public byte[] creaPDF() throws Exception {
		File file = null;
		FileOutputStream fos = null;
		
		try {
			InputStream jspTemplate = ServletActionContext.getServletContext().getResourceAsStream("/stampe/Fattura.jasper");
			Fatture fatture = new Fatture();
			Clienti clienti = new Clienti();
			
			fatture.completeReferences(fattura);
			fattura.setCliente((Cliente) clienti.completeReferences(fattura.getCliente()));
			
			JRDataSource ds = preparaDettagliStampaFattura(fattura);
			Map params = preparaParametriStampaFattura(fattura);

			params.put("REPORT_LOCALE", Locale.ITALIAN);
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
	
	public String getPDFPath(){
		return buildPDFPath(false);
	}
	
	private String buildPDFPath(boolean mkdirs) {
		File file = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(fattura.getData());
		String ps = System.getProperty("file.separator");
		String percorsoAnno = Settings.getInstance().getValue("print.pdfRepository") + "" +
				"Fatture" + ps + cal.get(Calendar.YEAR);
		String percorsoMese = percorsoAnno + ps + (cal.get(Calendar.MONTH) + 1);
		String percorsoFile = percorsoMese + ps + "Fattura_" + fattura.getAnnoContabile() + "_n_" + fattura.getNumeroProgressivo() + ".pdf"; 
		
		
		file = new File(percorsoFile);
		if (!file.isFile()) {
			if (mkdirs == false)
				return null;
			//CONTROLLO SE ESISTE L'ANNO
		
			
			file = new File(percorsoAnno);
			if (!file.isDirectory()) {
				//CREO LA DIRECTORY ANNO
				file.mkdir();
			}
			file = new File(percorsoMese);
			file.mkdir();
		}
		
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
