package stampemgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

import org.apache.struts2.ServletActionContext;

import vo.Articolo;
import vo.CategoriaArticolo;
import vo.Listino;
import vo.Prezzo;
import vo.VOElement;
import dao.Articoli;
import dao.CategorieArticolo;
import dao.DataAccessException;
import dao.Listini;
import dao.Settings;

public class ListinoPrintHandler extends DocumentPrintHandler {

	private Listino listino = null;
	private Integer idCategoria = null;
	private Integer idFornitore = null;
	
	public ListinoPrintHandler(VOElement element) {
		listino = (Listino) element;		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map preparaParametriStampa() throws DataAccessException {
		
		
		Map params = new HashMap();
		listino = new Listini().find(listino.getId());
		params.put("listino", listino.getDescrizione());
		params.put("dataCreazione", new Date());
		params.put("SUBREPORT_DIR", ServletActionContext.getServletContext().getRealPath("/stampe") + "/");
		
		try {
			params.put("image", ServletActionContext.getServletContext().getResource("/img/star.jpg"));
		} catch (MalformedURLException e) {}
		
		return params;
	}

	@SuppressWarnings("unchecked")
	private JRDataSource preparaDettagliStampa() throws DataAccessException {
		JRBeanArrayDataSource dataSource;
		@SuppressWarnings("rawtypes")
		Vector dettagli = new Vector();
		
		Collection<CategoriaArticolo> c = null;
		if(idCategoria == null) c = new CategorieArticolo().getElements();
		else {
			CategoriaArticolo vo = new CategorieArticolo().find(idCategoria);
			c = new Vector<CategoriaArticolo>();
			c.add(vo);
		}
		
		Iterator itr = c.iterator();
		
		while(itr.hasNext())
		{
			CategoriaArticolo catArticolo = (CategoriaArticolo)itr.next();
			
			Collection<Articolo> articoli = null;
			if(idFornitore == null) articoli = new Articoli().getListaArticoliCategoria(catArticolo.getId());
			else articoli = new Articoli().getListaArticoliAttivi(idFornitore, catArticolo.getId());
			
			catArticolo.setArticoli(new Vector<Prezzo>());
			for(Articolo articolo : articoli){
				if(articolo != null && !articolo.isProdottoUsoInterno()){
					new Articoli().completeReferences(articolo);
					if(articolo.getPrezzi() == null) continue;
					for(Object obj : articolo.getPrezzi())
					{
						Prezzo prezzo = (Prezzo)obj;
						prezzo.setArticolo(articolo);
						if(prezzo.getIdListino() == listino.getId())
							catArticolo.getArticoli().add(prezzo);
					}
				}
			}
			dettagli.add(catArticolo);				
		}
		
		dataSource = new JRBeanArrayDataSource(dettagli.toArray());
		return dataSource;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public byte[] creaPDF() throws Exception {
		File file = null;
		FileOutputStream fos = null;
		try {
			InputStream jspTemplate = ServletActionContext.getServletContext()
					.getResourceAsStream("/stampe/Listino.jasper");
			
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
		
		String percorsoFile = percorsoMese + ps + "Listino_" + listino.getId() + ".pdf"; 		
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
