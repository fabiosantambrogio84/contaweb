package forms;

import dao.DataAccessException;
import dao.DBUtilities;
import dao.Settings;
import forms.EditPassword;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;
//import java.util.Date;
import java.util.Properties;

import vo.Configurazione;
import web.List;

public class EditProperties extends Edit { // List {

    private static final long serialVersionUID = 1L;
    private static String NOTPRESENT = "notPresent";
    private static String PRESENT = "present";

    private String appletUrl = "";
    private String articoliImgRepository = "";
    private String backupLocation = "";
    private Configurazione configurazione = new Configurazione();
    private EditPassword editPassword = null;
    private String fattureFromAddress = "";
    private String fattureMailContent = "";
    private String fattureMailSubjectPrefix = "";
    private File fileAppend = null;
    private String listElementi = "";
    private String mailRepositoryFatture = "";
    private String mailRepositoryOrdiniFornitori = "";
    private String mailSmtpHost = "";
    private String mailSmtpPassword = "";
    private String mailSmtpUser = "";
    private String ordiniFornitoriFromAddress = "";
    private String ordiniFornitoriMailContent = "";
    private String ordiniFornitoriMailSubjectPrefix = "";
    private String pecSmtpHost = "";
    private String pecSmtpPassword = "";
    private String pecSmtpUser = "";
    private String printPdfRepository = "";
    private String passwordAccessoCompleto = "";
    private String passwordAccessoNormale = "";
    private String passwordAccessoLimitato = "";
    private Settings settings = null;

    private String tmpString = "";

    @SuppressWarnings("unchecked")
    public String input() {
        if (editPassword == null)
            editPassword = EditPassword.getInstance();
        
        if ((editPassword.getAccessLevel().compareTo("completo") != 0) | 
            (editPassword.getAccessAllowed() != true))
            return "passwordRequest";

        updateData();
        return "input";
    }

    @SuppressWarnings("unchecked")
    public String execute() {

    	updateData();
        String tmp = null;
        if (editPassword == null) 
            editPassword = EditPassword.getInstance();

        if ((editPassword.getAccessLevel() != "completo") | 
            (editPassword.getAccessAllowed() != true)) 
            return "passwordRequest";

        if (settings == null) 
            settings = Settings.getInstance();

        try {
            return SUCCESS;
        } catch (Exception e) {
            stampaErrore("EditProperties.execute()",e);
            return ERROR;
        }
    }

    public String getAppletUrl() throws DataAccessException {
    	try {
			updateAppletUrl();
		} catch (DataAccessException e) {
	        stampaErrore("EditProperties.getAppletUrl()",e);
			return ERROR;
		}
    	return appletUrl;  
    }
    
    public void setAppletUrl(String appletUrl) {
    	this.appletUrl = appletUrl;
    }

    public String getArticoliImgRepository() throws DataAccessException {
    	try {
			updateArticoliImgRepository();
		} catch (DataAccessException e) {
	        stampaErrore("EditProperties.getArticoliImgRepository()",e);
			return ERROR;
		}
		return articoliImgRepository;
	}

	public void setArticoliImgRepository(String articoliImgRepository) {
		this.articoliImgRepository = articoliImgRepository;
	}

	/**
	 * @return the fattureFromAddress
	 */
    public String getFattureFromAddress() throws DataAccessException {
    	try {
			updateFattureFromAddress();
		} catch (DataAccessException e) {
	        stampaErrore("EditProperties.getFattureFromAddress()",e);
			return ERROR;
		}
		return fattureFromAddress;
	}

	/**
	 * @param fattureFromAddress the fattureFromAddress to set
	 */
	public void setFattureFromAddress(String fattureFromAddress) {
		this.fattureFromAddress = fattureFromAddress;
	}

	public String getFattureMailContent() {
		return fattureMailContent;
	}

	public void setFattureMailContent(String fattureMailContent) {
		this.fattureMailContent = fattureMailContent;
	}

	public String getFattureMailSubjectPrefix() {
		return fattureMailSubjectPrefix;
	}

	public void setFattureMailSubjectPrefix(String fattureMailSubjectPrefix) {
		this.fattureMailSubjectPrefix = fattureMailSubjectPrefix;
	}

	public String getListElementi() {
		return listElementi;
	}

	public void setListElementi(String listElementi) {
		this.listElementi = listElementi;
	}

	public String getMailRepositoryFatture() {
		return mailRepositoryFatture;
	}

	public void setMailRepositoryFatture(String mailRepositoryFatture) {
		this.mailRepositoryFatture = mailRepositoryFatture;
	}

	public String getMailRepositoryOrdiniFornitori() {
		return mailRepositoryOrdiniFornitori;
	}

	public void setMailRepositoryOrdiniFornitori(String mailRepositoryOrdiniFornitori) {
		this.mailRepositoryOrdiniFornitori = mailRepositoryOrdiniFornitori;
	}

	public String getMailSmtpHost() {
		return mailSmtpHost;
	}

	public void setMailSmtpHost(String mailSmtpHost) {
		this.mailSmtpHost = mailSmtpHost;
	}

	public String getMailSmtpPassword() {
		return mailSmtpPassword;
	}

	public void setMailSmtpPassword(String mailSmtpPassword) {
		this.mailSmtpPassword = mailSmtpPassword;
	}

	public String getMailSmtpUser() {
		return mailSmtpUser;
	}

	public void setMailSmtpUser(String mailSmtpUser) {
		this.mailSmtpUser = mailSmtpUser;
	}

	public String getOrdiniFornitoriFromAddress() {
		return ordiniFornitoriFromAddress;
	}

	public void setOrdiniFornitoriFromAddress(String ordiniFornitoriFromAddress) {
		this.ordiniFornitoriFromAddress = ordiniFornitoriFromAddress;
	}

	public String getOrdiniFornitoriMailContent() {
		return ordiniFornitoriMailContent;
	}

	public void setOrdiniFornitoriMailContent(String ordiniFornitoriMailContent) {
		this.ordiniFornitoriMailContent = ordiniFornitoriMailContent;
	}

	public String getOrdiniFornitoriMailSubjectPrefix() {
		return ordiniFornitoriMailSubjectPrefix;
	}

	public void setOrdiniFornitoriMailSubjectPrefix(String ordiniFornitoriMailSubjectPrefix) {
		this.ordiniFornitoriMailSubjectPrefix = ordiniFornitoriMailSubjectPrefix;
	}

	public String getPecSmtpHost() {
		return pecSmtpHost;
	}

	public void setPecSmtpHost(String pecSmtpHost) {
		this.pecSmtpHost = pecSmtpHost;
	}

	public String getPecSmtpPassword() {
		return pecSmtpPassword;
	}

	public void setPecSmtpPassword(String pecSmtpPassword) {
		this.pecSmtpPassword = pecSmtpPassword;
	}

	public String getPecSmtpUser() {
		return pecSmtpUser;
	}

	public void setPecSmtpUser(String pecSmtpUser) {
		this.pecSmtpUser = pecSmtpUser;
	}

	public String getPrintPdfRepository() {
		return printPdfRepository;
	}

	public void setPrintPdfRepository(String printPdfRepository) {
		this.printPdfRepository = printPdfRepository;
	}

	public String getPasswordAccessoCompleto() {
		return passwordAccessoCompleto;
	}

	public void setPasswordAccessoCompleto(String passwordAccessoCompleto) {
		this.passwordAccessoCompleto = passwordAccessoCompleto;
	}

	public String getPasswordAccessoNormale() {
		return passwordAccessoNormale;
	}

	public void setPasswordAccessoNormale(String passwordAccessoNormale) {
		this.passwordAccessoNormale = passwordAccessoNormale;
	}

	public String getPasswordAccessoLimitato() {
		return passwordAccessoLimitato;
	}

	public void setPasswordAccessoLimitato(String passwordAccessoLimitato) {
		this.passwordAccessoLimitato = passwordAccessoLimitato;
	}

	public String writeAppletUrl() throws DataAccessException {
        String tmp = null;
		tmp = writeData("applet.url", this.appletUrl);
      	return tmp;
    }

	public String writeArticoliImgRepository() throws DataAccessException {
        String tmp = null;
		tmp = writeData("articoli.imgRepository", this.articoliImgRepository);
      	return tmp;
    }

	public String writeFattureFromAddress() throws DataAccessException {
        String tmp = null;
		tmp = writeData("fatture.fromAddress", this.fattureFromAddress);
      	return tmp;
    }

	public String writeFattureMailContent() throws DataAccessException {
        String tmp = null;
		tmp = writeData("fatture.mailContent", this.fattureMailContent);
		updateData();
      	return tmp;
    }

	public String writeFattureMailSubjectPrefix() throws DataAccessException {
        String tmp = null;
		tmp = writeData("fatture.mailSubjectPrefix", this.fattureMailSubjectPrefix);
		updateData();
      	return tmp;
    }

	public String writeListElementi() throws DataAccessException {
        String tmp = null;
		tmp = writeData("list.Elementi", this.listElementi);
		updateData();
      	return tmp;
    }

	public String writeMailRepositoryFatture() throws DataAccessException {
        String tmp = null;
		tmp = writeData("mail.repositoryFatture", this.mailRepositoryFatture);
      	return tmp;
    }

	public String writeMailRepositoryOrdiniFornitori() throws DataAccessException {
        String tmp = null;
		tmp = writeData("mail.repositoryOrdiniFornitori", this.mailRepositoryOrdiniFornitori);
      	return tmp;
    }

	public String writeMailSmtpHost() throws DataAccessException {
        String tmp = null;
		tmp = writeData("mail.smtpHost", this.mailSmtpHost);
      	return tmp;
    }

	public String writeMailSmtpPassword() throws DataAccessException {
        String tmp = null;
		tmp = writeData("mail.smtpPassword", this.mailSmtpPassword);
      	return tmp;
    }

	public String writeMailSmtpUser() throws DataAccessException {
        String tmp = null;
		tmp = writeData("mail.smtpUser", this.mailSmtpUser);
      	return tmp;
    }

	public String writeOrdiniFornitoriFromAddress() throws DataAccessException {
        String tmp = null;
		tmp = writeData("ordiniFornitori.fromAddress", this.ordiniFornitoriFromAddress);
      	return tmp;
    }

	public String writeOrdiniFornitoriMailContent() throws DataAccessException {
        String tmp = null;
		tmp = writeData("ordiniFornitori.mailContent", this.ordiniFornitoriMailContent);
      	return tmp;
    }

	public String writeOrdiniFornitoriMailSubjectPrefix() throws DataAccessException {
        String tmp = null;
		tmp = writeData("ordiniFornitori.mailSubjectPrefix", this.ordiniFornitoriMailSubjectPrefix);
      	return tmp;
    }

	public String writePecSmtpHost() throws DataAccessException {
        String tmp = null;
		tmp = writeData("pec.smtpHost", this.pecSmtpHost);
      	return tmp;
    }

	public String writePecSmtpPassword() throws DataAccessException {
        String tmp = null;
		tmp = writeData("pec.smtpPassword", this.pecSmtpPassword);
      	return tmp;
    }

	public String writePecSmtpUser() throws DataAccessException {
        String tmp = null;
		tmp = writeData("pec.smtpUser", this.pecSmtpUser);
      	return tmp;
    }

	public String writePrintPdfRepository() throws DataAccessException {
        String tmp = null;
		tmp = writeData("print.pdfRepository", this.printPdfRepository);
      	return tmp;
    }

	public String writePasswordAccessoCompleto() throws DataAccessException {
        String tmp = null;
		tmp = writeData("password.accessoCompleto", this.passwordAccessoCompleto);
      	return tmp;
    }

	public String writePasswordAccessoNormale() throws DataAccessException {
        String tmp = null;
		tmp = writeData("password.accessoNormale", this.passwordAccessoNormale);
      	return tmp;
    }

	public String writePasswordAccessoLimitato() throws DataAccessException {
        String tmp = null;
		tmp = writeData("password.accessoLimitato", this.passwordAccessoLimitato);
      	return tmp;
    }

	public String writeBackupLocation() throws DataAccessException {
        String tmp = null;
		tmp = writeData("backup.location", this.backupLocation);
      	return tmp;
    }

	public String writeData(String key, String value) throws DataAccessException {
    	if (settings == null) 
            settings = Settings.getInstance();

        Configurazione template = new Configurazione();
        template.setElemento(key);
        //template.setValue(value);
        try {
        	String tmp = null;
        	tmp = settings.setValue(key, value);
        	tmp = settings.getValue(key);
           	if (tmp != value)
           		return ERROR;
    		template = (Configurazione) settings.findWithAllReferences(template);
            if (template == null) {
            	template = new Configurazione();
                template.setElemento(key);
                template.setId(19); // TODO introdurre una funzione per rilevare l'ultimo ID in uso e applicare il successivo.
            }
    		template.setValue(value);
    		if (settings.store(template) != SUCCESS)
    			return ERROR;
        } catch (Exception e) {
            stampaErrore("EditProperties.writeData(key, value)",e);
            return ERROR;
        }
        updateData();
   	    return SUCCESS;
    }

    public String updateAppletUrl() throws DataAccessException {
    	String tmp = null;
    	try {
			tmp = updateData("applet.url");
		} catch (DataAccessException e) {
	        stampaErrore("EditProperties.updateAppletUrl()",e);
			return ERROR;
		}
    	return tmp;
    }

    public String updateArticoliImgRepository() throws DataAccessException {
    	String tmp = null;
    	try {
			tmp = updateData("articoli.imgRepository");
		} catch (DataAccessException e) {
	        stampaErrore("EditProperties.updateArticoliImgRepository()",e);
			return ERROR;
		}
    	return tmp;
    }
    
    public String updateFattureFromAddress() throws DataAccessException {
    	String tmp = null;
    	try {
			tmp = updateData("fatture.fromAddress");
		} catch (DataAccessException e) {
	        stampaErrore("EditProperties.updateFattureFromAddress()",e);
			return ERROR;
		}
    	return tmp;
    }

    public String updateBackupLocation() throws DataAccessException {
    	String tmp = null;
    	try {
			tmp = updateData("backup.location");
		} catch (DataAccessException e) {
	        stampaErrore("EditProperties.updateBackupLocation()",e);
			return ERROR;
		}
    	return tmp;
    }
    
    public String updateData() {
        try {
			updateData("applet.url");
	    	updateData("articoli.imgRepository");
	    	updateData("fatture.fromAddress");
	    	updateData("fatture.mailContent");
	    	updateData("fatture.mailSubjectPrefix");
	    	updateData("list.elementi");
	    	updateData("mail.repositoryFatture");
	    	updateData("mail.repositoryOrdiniFornitori");
	    	updateData("mail.smtpHost");
	    	updateData("mail.smtpPassword");
	    	updateData("mail.smtpUser");
	    	updateData("ordiniFornitori.fromAddress");
	    	updateData("ordiniFornitori.mailContent");
	    	updateData("ordiniFornitori.mailSubjectPrefix");
	    	updateData("pec.smtpHost");
	    	updateData("pec.smtpPassword");
	    	updateData("pec.smtpUser");
	    	updateData("print.pdfRepository");
            updateData("password.accessoCompleto");
	    	updateData("password.accessoNormale");
	    	updateData("password.accessoLimitato");
	    	updateData("backup.location");
		} catch (DataAccessException e) {
            stampaErrore("EditProperties.updateData()",e);
            return ERROR;
		}
    	return SUCCESS;
    }
    
    private String updateData(String key) throws DataAccessException {
    	if (settings == null) 
            settings = Settings.getInstance();

    	if (settings.getStato() == settings.UPDATE_REQUEST)
    		settings.updateDB();

        try {
        	String tmp = null;
        	tmp = settings.getValue(key);
        	if (tmp == null)
        		return ERROR;
        	switch (key) {
    		case "applet.url":
    			this.appletUrl = tmp;
    			return SUCCESS;
    		case "articoli.imgRepository":
    			this.articoliImgRepository = tmp;
    			return SUCCESS;
    		case "fatture.fromAddress":
    			this.fattureFromAddress = tmp;
    			return SUCCESS;
    		case "fatture.mailContent":
    			this.fattureMailContent = tmp;
    			return SUCCESS;
    		case "fatture.mailSubjectPrefix":
    			this.fattureMailSubjectPrefix = tmp;
    			return SUCCESS;
    		case "list.elementi":
    			this.listElementi = tmp;
    			return SUCCESS;
    		case "mail.repositoryFatture":
    			this.mailRepositoryFatture = tmp;
    			return SUCCESS;
    		case "mail.repositoryOrdiniFornitori":
    			this.mailRepositoryOrdiniFornitori = tmp;
    			return SUCCESS;
    		case "mail.smtpHost":
    			this.mailSmtpHost = tmp;
    			return SUCCESS;
    		case "mail.smtpPassword":
    			this.mailSmtpPassword = tmp;
    			return SUCCESS;
    		case "mail.smtpUser":
    			this.mailSmtpUser = tmp;
    			return SUCCESS;
    		case "ordiniFornitori.fromAddress":
    			this.ordiniFornitoriFromAddress = tmp;
    			return SUCCESS;
    		case "ordiniFornitori.mailContent":
    			this.ordiniFornitoriMailContent = tmp;
    			return SUCCESS;
    		case "ordiniFornitori.mailSubjectPrefix":
    			this.ordiniFornitoriMailSubjectPrefix = tmp;
    			return SUCCESS;
    		case "pec.smtpHost":
    			this.pecSmtpHost = tmp;
    			return SUCCESS;
    		case "pec.smtpPassword":
    			this.pecSmtpPassword = tmp;
    			return SUCCESS;
    		case "pec.smtpUser":
    			this.pecSmtpUser = tmp;
    			return SUCCESS;
    		case "print.pdfRepository":
    			this.printPdfRepository = tmp;
    			return SUCCESS;
    		case "password.accessoCompleto":
    			this.passwordAccessoCompleto = tmp;
    			return SUCCESS;
    		case "password.accessoNormale":
    			this.passwordAccessoNormale = tmp;
    			return SUCCESS;
    		case "password.accessoLimitato":
    			this.passwordAccessoLimitato = tmp;
    			return SUCCESS;
    		case "backup.location":
    			this.backupLocation = tmp;
    			return SUCCESS;
            }
			return ERROR;
        } catch (Exception e) {
            stampaErrore("EditProperties.updateData(String)",e);
            return ERROR;
        }
    }

	@Override
    public String store() {
		// TODO Auto-generated method stub
		return ERROR;
    }

	@Override
	protected String delete() {
		// TODO Auto-generated method stub
		return ERROR;
	}

    public String autoBackupDB() {
        String result = ERROR;
        if (NOTPRESENT.equalsIgnoreCase(backupDBIsPresent()))
            result = backupDB();
        else 
        	result = SUCCESS;
        return result;
    }

    private String backupDBIsPresent() {
        String result = NOTPRESENT;
        String tmpFileName = "";
        Calendar cal = Calendar.getInstance();
        try {
        	tmpString = updateBackupLocation();         	
			if(!SUCCESS.equalsIgnoreCase(tmpString))
				return ERROR;
		} catch (DataAccessException e) {
	        stampaErrore("EditProperties.backupDBIsPresent() - updateBackupLocation(): ",e);
			return ERROR;
		}
        if (backupLocation != null) {
            if (backupLocation != "") {
                // tmpFileName = backupLocation + "/ContaWebDB_" + cal.YEAR + cal.MONTH + cal.DAY_OF_MONTH + "_*.msql";
            	tmpFileName = DBUtilities.calculateFilename(backupLocation, DBUtilities.FilenameFileOnly);
            	File tmpDir = new File(DBUtilities.calculateFilename(backupLocation, DBUtilities.FilenamePathOnly));
            	//String tmpName = tmpFileName.substring(tmpDir..length(), tmpFileName.length());
            	String[] paths;
            	// DBUtilities.calculateFilename(backupLocation, DBUtilities.FilenameFileOnly);

            	/* tmpFileName = backupLocation + "/ContaWebDB_" + cal.get(cal.YEAR);
            	if (cal.get(cal.MONTH)<10) 
            		tmpFileName += "0";
            	tmpFileName += (cal.get(cal.MONTH) + 1);
            	if (cal.get(cal.DAY_OF_MONTH)<10)
            		tmpFileName += "0";
            	tmpFileName += cal.get(cal.DAY_OF_MONTH) + "_*.msql"; */
            	
    			// fileAppend = new File(tmpFileName);
    			/* FilenameFilter filter = new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						// TODO Auto-generated method stub
						return false;
					}
    			}; */
    			
    			// filter.accept(tmpDir, fileAppend.getName());
    			//filter.accept(tmpDir, tmpName);
    			// paths = fileAppend.list(filter);
    			paths = tmpDir.list(); // fileAppend.list();
    			for (String currentFile:paths) {
    				String tmpString = tmpFileName.substring(0, tmpFileName.length()-9);
    				if (currentFile.contains(tmpString))
    					result = PRESENT;
    			}
            }
        }
        return result;
    }
        

    public String backupDB() {
        // ToDo: backupDB
        // Verificare che il path: "bacupLocation" sia accessibile.
        // Accedere al DB MySql e creare un dump nel path indicato.
        // Il nome del file pu� seguire le seguenti caratteristiche 
        // "ContaWebDB_aaaammgg_hhmn" dove:
        // - aaaa = anno
        // - mm = mese
        // - gg = giorno
        // - hh = ora
        // - mn = minuti
        // Date data = new Date();
        Calendar cal = Calendar.getInstance();
        try {
        	tmpString = updateBackupLocation();         	
			if(!SUCCESS.equalsIgnoreCase(tmpString))
				return ERROR;
		} catch (DataAccessException e) {
	        stampaErrore("EditProperties.backupDB() - updateBackupLocation(): ",e);
			return ERROR;
		}
       
        String tmpString = DBUtilities.dumpDB(backupLocation); 
		        /* + "/ContaWebDB_" + data.getYear() + 
        		data.getMonth() +  data.getDay() + "_" + data.getHours() + data.getMinutes() + ".msql"); */
        if (tmpString == null) {
            stampaMessaggio("EditProperties.backupDB() - tmpDBUtilities.dumpDB() returns a null pointer");
            return ERROR;
        }
        if (tmpString == ERROR) {
            stampaMessaggio("EditProperties.backupDB() - tmpDBUtilities.dumpDB() returns an error status");
            return ERROR;
        }
        System.out.println("DB Backup retutn " + tmpString + " status.");
        return SUCCESS;
    }

    public String retrieveDB() {
        // ToDo: retrieveDB
        // Verificare che il path: "bacupLocation" sia accessibile e contenga almeno un
        // backup valido. 
        // Permettere all'utente di selezionare il Backup.
        // Importare il backup DB in MySql.
        return SUCCESS;
    }

	/**
	 * @return the backupLocation
	 */
	public String getBackupLocation() throws DataAccessException {
    	try {
        	tmpString = updateBackupLocation();         	
			if(!SUCCESS.equalsIgnoreCase(tmpString))
				return ERROR;
		} catch (DataAccessException e) {
	        stampaErrore("EditProperties.getBackupLocation()",e);
			return ERROR;
		}
		return backupLocation;
	}

	/**
	 * @param backupLocation the backupLocation to set
	 */
	public void setBackupLocation(String backupLocation) {
		this.backupLocation = backupLocation;
	}


}