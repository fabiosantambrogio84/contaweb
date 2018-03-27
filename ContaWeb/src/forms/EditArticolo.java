package forms;

import vo.*;
import dao.*;

import java.io.File;
import java.util.*;
import org.apache.commons.io.FileUtils;
// import org.apache.struts2.ServletActionContext;

public class EditArticolo extends Edit {

	private static final long serialVersionUID = 1L;
	
	private Integer id = null;
	
	private Articolo articolo = null;
	
	private Collection listIvas = null;
	private Collection listFornitori = null;
	private Collection listCategorie = null;
	private Collection listDestinazioni = null;
	
	private File[] fileUpload;
	private String[] fileUploadContentType;
	private String[] fileUploadFileName;

	private Vector listaPrezzi = new Vector();
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public File[] getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(File[] fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String[] getFileUploadContentType() {
		return fileUploadContentType;
	}

	public void setFileUploadContentType(String[] fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}

	public String[] getFileUploadFileName() {
		return fileUploadFileName;
	}

	public void setFileUploadFileName(String[] fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}

	public Vector getListaPrezzi() {
		return listaPrezzi;
	}
	
	public void setListaPrezzi(Vector listaPrezzi) {
		this.listaPrezzi = listaPrezzi;
	}
	
	public Collection getListCategorie() {
		if (listCategorie == null) {
			try {
				listCategorie = new CategorieArticolo().getElements();
			} catch (Exception e) {
				return null;
			}
		}
		return listCategorie;
	}
	
	public Collection getListDestinazioni() {
		if (listDestinazioni == null) {
			try {
				listDestinazioni = new DestinazioniArticolo().getElements();
			} catch (Exception e) {
				return null;
			}
		}
		return listDestinazioni;
	}
	
	public void setListDestinazioni(Collection lst) {
		this.listDestinazioni = lst;
	}
	
	public void setListCategorie(Collection lst) {
		this.listCategorie = lst;
	}
	
	public Collection getListFornitori() {
		if (listFornitori == null) {
			try {
				listFornitori = new Fornitori().getElements();
			} catch (Exception e) {
				return null;
			}
		}
		return listFornitori;
	}
	
	public void setListFornitori(Collection listFornitori) {
		this.listFornitori = listFornitori;
	}
	
	public Collection getListIvas() {
		if (listIvas == null) {
			try {
				listIvas = new Ivas().getElements();
			} catch (Exception e) {
				return null;
			}
		}
		return listIvas;
	}
	
	public void setListIvas(Collection listIvas) {
		this.listIvas = listIvas;
	}

	public void setArticolo(Articolo articolo) {
		this.articolo = articolo;
	}
	
	public Articolo getArticolo() {
		return articolo;
	}
	
    public String input() {
    	if (getAction().equalsIgnoreCase("delete")) //AZIONE DELETE
  			return delete();
    	
    	try {
  			Vector prezzi = null;
	  		if (getAction().equalsIgnoreCase("insert")) {
	  			articolo = new Articolo();
	  			prezzi = new Listini().getListiniByPrezzi();
	  		} else if (getAction().equalsIgnoreCase("edit")) {
	  			Articoli articoli = new Articoli();	
	  			articolo = articoli.find(id);
	  			prezzi = new Listini().getListiniByPrezzi(articolo.getPrezzi());
	  		}
	  		articolo.setPrezzi(prezzi);		

			} catch (Exception e) {
				stampaErrore("EditArticolo.input()",e);
				return ERROR;
			}
  		return INPUT;
	 }
	 
    protected String store() {
	 	try {
	 		articolo.setIva(new Ivas().find(articolo.getIdIva()));
	 		articolo.setFornitore(new Fornitori().find(articolo.getIdFornitore()));
	 		articolo.setPrezzi(listaPrezzi);
	 		articolo.setData(new java.sql.Date(new Date().getTime()));
	 		//IMPOSTO IL CODICE ARTICOLO ASSOCIATO AI PREZZI
		 	for(int i=0;i<articolo.getPrezzi().size();++i)
		 		((Prezzo)articolo.getPrezzi().get(i)).setIdArticolo(articolo.getId());
			
		 	String ps = System.getProperty("file.separator");
		 	
		 	for (int i = 0; i < fileUpload.length; i++) 
		 	{
		 		File uploadedFile = fileUpload[i];		
		 		if(!uploadedFile.exists()) continue;
		 		String filePath = Settings.getInstance()
		 				.getValue("articoli.imgRepository") + articolo.getCodiceArticolo();		 		
		 		File fileToCreate = new File(filePath, this.fileUploadFileName[i]);  
		 		FileUtils.copyFile(uploadedFile, fileToCreate);
		 		
		 		String basePath = "img/articoli/" 
		 				+ articolo.getCodiceArticolo() + "/";
		 		filePath = basePath += this.fileUploadFileName[i];
		 		
		 		if(i == 0) {
		 			
		 			if(articolo.getImmagine1() != null)
		 			{
		 				File old_file = new File(filePath, articolo.getImmagine1());
		 				if(old_file.exists()) old_file.delete();
		 			}
		 			
		 			articolo.setImmagine1(filePath);		 			
		 		}
		 		else if(i == 1) {
		 			
		 			if(articolo.getImmagine2() != null)
		 			{
		 				File old_file = new File(filePath, articolo.getImmagine2());
		 				if(old_file.exists()) old_file.delete();
		 			}
		 			
		 			articolo.setImmagine2(filePath);		 		
		 		}
		 		else if(i == 2) {
		 			
		 			if(articolo.getImmagine3() != null)
		 			{
		 				File old_file = new File(filePath, articolo.getImmagine3());
		 				if(old_file.exists()) old_file.delete();
		 			}
		 			
		 			articolo.setImmagine3(filePath);		 		
		 		}
		 		else if(i == 3) {
		 			
		 			if(articolo.getImmagine4() != null)
		 			{
		 				File old_file = new File(filePath, articolo.getImmagine4());
		 				if(old_file.exists()) old_file.delete();
		 			}
		 			
		 			articolo.setImmagine4(filePath);		 		
		 		}
		 		else if(i == 4) {
		 			
		 			if(articolo.getImmagine5() != null)
		 			{
		 				File old_file = new File(filePath, articolo.getImmagine5());
		 				if(old_file.exists()) old_file.delete();
		 			}
		 			
		 			articolo.setImmagine5(filePath);		 		
		 		}
		 		else if(i == 5) {
		 			
		 			if(articolo.getImmagine6() != null)
		 			{
		 				File old_file = new File(filePath, articolo.getImmagine6());
		 				if(old_file.exists()) old_file.delete();
		 			}
		 			
		 			articolo.setImmagine6(filePath);		 		
		 		}
		 	}
		 	
			Articoli articoli = new Articoli();
			articoli.store(articolo); 	
		} catch (Exception e) {
			stampaErrore("EditArticolo.store()",e);
			return ERROR;
		}
	 	return SUCCESS;
	 }
	 
	 protected String delete() {
	 	try {
			articolo = new Articolo();
			articolo.setId(id);
			new Articoli().delete(articolo);
		} catch (DataAccessException e) {
			stampaErrore("EditArticolo.delete()",e);
			return ERROR_DELETE;
		}
	 	return SUCCESS;
	 }
	 
	 public void validate() {
	 }
}