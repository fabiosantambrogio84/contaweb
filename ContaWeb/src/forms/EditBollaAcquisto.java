package forms;

import dao.BolleAcquisto;
import dao.Settings;
import vo.*;

public class EditBollaAcquisto extends Edit {

	private static final long serialVersionUID = 1L;
	
	private Integer id = null;
	private String appletUrl = null;

	public String getAppletUrl() {
		return appletUrl;
	}

	public void setAppletUrl(String appletUrl) {
		this.appletUrl = appletUrl;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String input() {
  		try {
  			appletUrl = Settings.getInstance().getValue("applet.url");
			if (getAction().equalsIgnoreCase("delete")) {
				return delete();
			}
		} catch (Exception e) {
			stampaErrore("EditBollaAcquisto.input()",e);
			return ERROR;
		}

  		return INPUT;
	 }
	 
	protected String delete() {
		 try {
			 BollaAcquisto ba = new BollaAcquisto();
			 ba.setId(id);
			 new BolleAcquisto().delete(ba);
		} catch (Exception e) {
			stampaErrore("EditBollaAcquisto.delete()",e);
			return ERROR_DELETE;
		}
	 	 return SUCCESS;
	}

	@Override
	protected String store() {
		return null;
	}
}