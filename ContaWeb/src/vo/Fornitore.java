package vo;

public class Fornitore extends VOElement {

	private static final long serialVersionUID = 2767346264394280662L;

	private String descrizione;
	private String emailAddress = null;
	
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}
