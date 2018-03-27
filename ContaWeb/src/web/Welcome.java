package web;

import forms.EditProperties;

public class Welcome  extends GenericAction {

	private static final long serialVersionUID = 1L;

	public String execute() throws Exception {
        EditProperties tmpEditProperties = new EditProperties();
        if (tmpEditProperties.autoBackupDB() != SUCCESS)
            return "warning";
		return SUCCESS;
    }
}