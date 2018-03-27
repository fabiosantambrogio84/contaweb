package stampemgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.text.SimpleDateFormat;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

import org.apache.struts2.ServletActionContext;

import dao.Clienti;
import dao.DDTs;
import dao.DataAccessException;
import dao.Fatture;
import dao.RiepilogoDDTs;
import dao.RiepilogoFatture;
import dao.Settings;
import stampe.PrintFatture;
import vo.Cliente;
import vo.DDT;
import vo.DettaglioDDT;
import vo.Fattura;
import vo.VOElement;

public class DDTsPrintHandler extends DocumentPrintHandler {

	private RiepilogoDDTs  ddts;
	
	public DDTsPrintHandler(VOElement element) {
		ddts = (RiepilogoDDTs ) element;
	}

	@SuppressWarnings("unchecked")
	private Map preparaParametriStampaFattura(RiepilogoDDTs fattura) throws DataAccessException {
		Map params = new HashMap();
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yy");
		
		params.put("dataDal", fattura.getDataDal());
		params.put("dataAl", fattura.getDataAl());
		params.put("totale", fattura.getTotale());
				
		return params;
	}

	@SuppressWarnings("unchecked")
	private JRDataSource preparaDettagliStampaFattura(RiepilogoDDTs fattura) throws DataAccessException {
		JRBeanArrayDataSource dataSource;
		
		dataSource = new JRBeanArrayDataSource(fattura.getListaDDTs().toArray());

		return dataSource;
	}
	
	public byte[] creaPDF() throws Exception {
		File file = null;
		FileOutputStream fos = null;
		
		try {
			
			InputStream jspTemplate = null;
			
			jspTemplate = ServletActionContext.getServletContext().getResourceAsStream("/stampe/RiepilogoDDTs.jasper");
			
			JRDataSource ds = preparaDettagliStampaFattura(ddts);
			Map params = preparaParametriStampaFattura(ddts);

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
//		cal.setTime(fattura.getDataDal());
		String ps = System.getProperty("file.separator");
		String percorsoAnno = Settings.getInstance().getValue("print.pdfRepository") + "" +
				"RiepilogoDDT" + ps + cal.get(Calendar.YEAR);
		String percorsoMese = percorsoAnno + ps + (cal.get(Calendar.MONTH) + 1);
		
		cal.setTime(ddts.getDataDal());
		
		String annoDAL = "" + cal.get(Calendar.YEAR);
		String meseDAL = "" + cal.get(Calendar.MONTH)+1;
		String giornoDAL = "" + cal.get(Calendar.DAY_OF_MONTH);
		
		String dal = giornoDAL+"-"+meseDAL+"-"+annoDAL;
		
		cal.setTime(ddts.getDataAl());
		
		String annoAL = "" + cal.get(Calendar.YEAR);
		String meseAL = "" + cal.get(Calendar.MONTH)+1;
		String giornoAL = "" + cal.get(Calendar.DAY_OF_MONTH);
		
		String al = giornoAL+"-"+meseAL+"-"+annoAL;
		
		String percorsoFile = "";
		percorsoFile = percorsoMese + ps + "RiepilogoDDT_" + dal + "_" + al + ".pdf";
		
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
