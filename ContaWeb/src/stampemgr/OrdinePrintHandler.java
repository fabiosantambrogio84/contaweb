package stampemgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

import org.apache.struts2.ServletActionContext;

import vo.Ordine;
import vo.VOElement;
import dao.DataAccessException;
import dao.Ordini;
import dao.Settings;

public class OrdinePrintHandler  extends DocumentPrintHandler {

	private Ordine ordine = null;
	
	public OrdinePrintHandler(VOElement element) {
		ordine = (Ordine) element;
	}

	@SuppressWarnings("unchecked")
	private Map preparaParametriStampa() throws DataAccessException {
		Map params = new HashMap();
		new Ordini().completeReferences(ordine);
		params.put("progressivoCompleto",ordine.getProgressivoCompleto());
		params.put("dataCreazione", ordine.getDataCreazione());
		if(ordine.getAutista() != null) 
			params.put("autistaDescrizione", ordine.getAutista().getNome());
		params.put("note", ordine.getNote());
		params.put("clienteRs", ordine.getCliente().getRs());
		
		try {  
			params.put("image", ServletActionContext.getServletContext().getResource("/img/star.jpg"));
			params.put("image_marchio", ServletActionContext.getServletContext().getResource("/img/marchio.png"));
		} catch (MalformedURLException e) {}
		
		return params;
	}

	@SuppressWarnings("unchecked")
	private JRDataSource preparaDettagliStampa() throws DataAccessException {
		JRBeanArrayDataSource dataSource;
		dataSource = new JRBeanArrayDataSource(ordine.getDettagliOrdine().toArray());
		return dataSource;
	}
	
	public byte[] creaPDF() throws Exception {
		File file = null;
		FileOutputStream fos = null;
		try {
			InputStream jspTemplate = ServletActionContext.getServletContext().getResourceAsStream("/stampe/Ordine.jasper");
			
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
		cal.setTime(ordine.getDataCreazione());
		String ps = System.getProperty("file.separator");
		String percorsoAnno = Settings.getInstance().getValue("print.pdfRepository") + "" +
				"Ordini" + ps + cal.get(Calendar.YEAR);
		String percorsoMese = percorsoAnno + ps + (cal.get(Calendar.MONTH) + 1);
		String percorsoFile = percorsoMese + ps + "Ordine_" + ordine.getAnnoContabile() + "_n_" + ordine.getNumeroProgressivo() + ".pdf"; 
		
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
