package stampemgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import dao.DataAccessException;
import dao.Settings;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import vo.ReportGiacenza;

public class GiacenzePrintHandler extends DocumentPrintHandler {

	private List<ReportGiacenza> reportGiacenze = null;
	
	public GiacenzePrintHandler(List<ReportGiacenza> reportGiacenze) {
		this.reportGiacenze = reportGiacenze;		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map preparaParametriStampa() throws DataAccessException {
		
		Map params = new HashMap();
		params.put("dataCreazione", new Date());
		params.put("SUBREPORT_DIR", ServletActionContext.getServletContext().getRealPath("/stampe") + "/");
		
		try {
			params.put("image", ServletActionContext.getServletContext().getResource("/img/star.jpg"));
		} catch (MalformedURLException e) {}
		
		return params;
	}

	private JRDataSource preparaDettagliStampa() throws DataAccessException {
		JRBeanArrayDataSource dataSource;

        dataSource = new JRBeanArrayDataSource(reportGiacenze.toArray());
        return dataSource;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public byte[] creaPDF() throws Exception {
		File file = null;
		FileOutputStream fos = null;
		try {
			InputStream jspTemplate = ServletActionContext.getServletContext()
					.getResourceAsStream("/stampe/Giacenze.jasper");
			
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
		String percorsoAnno = Settings.getInstance().getValue("print.pdfRepository");
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
		
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyyMMdd_HHmmss");
		
		String percorsoFile = percorsoMese + ps + "Giacenze_" + sdf.format(now) + ".pdf"; 		
		return percorsoFile;
	}

	public boolean isPDF() {
		return buildPDFPath(false) != null;
	}

	public byte[] leggiPDF() throws Exception {
		FileInputStream rf = null;
		try {
			String path = buildPDFPath(false);
			File file = new File(path);
			rf = new FileInputStream(file);
			
			byte[] array = new byte[(int) file.length()];
			rf.read(array);
			return array;
			
		} finally {
			if(rf != null) {
				rf.close();
			}
		}
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
