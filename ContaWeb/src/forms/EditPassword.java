package forms;

// import vo.Configurazione;
// import dao.DataAccessException;

import web.List;
import dao.Settings;

public class EditPassword extends Edit {

   private static final long serialVersionUID = 1L;
   private Settings settings = null;
   private static String accessLevel = "";
   public static final String accessLevelCompleto = "completo";
   public static final String accessLevelNormale = "normale";
   public static final String accessLevelLimitato = "limitato";
   private String accessPassword = "";
   private static boolean accessAllowed = false;
   // private Configurazione configurazione = new Configurazione();
   private static EditPassword editPassword = null;

    public String input() {
        return "input";
    }

    @SuppressWarnings("unchecked")
    public String execute() {
        // String tmp = null;
        
        if (settings == null) 
            settings = Settings.getInstance();

        /*try {
            tmp = settings.getValue("applet.url");
            appletUrl = tmp;
            tmp = settings.getValue("articoli.imgRepository");
            appletUrl = tmp;
        } catch (Exception e) {
            stampaErrore("EditPassword.execute()",e);
            return ERROR;
        }*/
        return SUCCESS;
    }

    public static EditPassword getInstance() {
		if (editPassword == null) {
			editPassword = new EditPassword();
		}
		return editPassword;
    }

    public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessPassword(String accessPassword) {
		this.accessPassword = accessPassword;
	}

	private String getAccessPassword() {
		return accessPassword;
	}

	public boolean getAccessAllowed() {
		return accessAllowed;
	}

    public String checkAccessPassword() {
        accessAllowed = false;
        if (settings == null) 
            settings = Settings.getInstance();

        String tmp = null;
        try {
            switch (accessLevel)
            {
            case accessLevelCompleto :
				tmp = settings.getValue("PasswordCompleto");
            	break;
            case accessLevelNormale :
                tmp = settings.getValue("PasswordNormale");
            	break;
            case accessLevelLimitato :
                tmp = settings.getValue("PasswordLimitato");
            	break;
            default : 
                return "NONE";
            }
            if (tmp == null) {
            	tmp = "EditPassword.checkAccessPassword() - settings password for " + accessLevel + " access level is unaccessible!";
            	stampaErrore(tmp);
                return ERROR;            	
            }
            int itmp = tmp.compareTo(accessPassword);
            if (itmp == 0)
            	accessAllowed = true;
        } catch (Exception e) {
            stampaErrore("EditPassword.checkAccessPassword()",e);
            return ERROR;
        }
        return SUCCESS;
    }

	@Override
	protected String store() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String delete() {
		// TODO Auto-generated method stub
		return null;
	}

}